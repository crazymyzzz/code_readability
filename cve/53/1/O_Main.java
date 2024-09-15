

public class Main {

    public static void main(String[] args) throws Exception {
        int exitCode = 0;

        FlywayTelemetryManager flywayTelemetryManager = null;
        if (!StringUtils.hasText(System.getenv("REDGATE_DISABLE_TELEMETRY"))) {
            flywayTelemetryManager = new FlywayTelemetryManager(pluginRegister);
            flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), null, false));
        }

        try {
            JavaVersionPrinter.printJavaVersion();
            CommandLineArguments commandLineArguments = new CommandLineArguments(pluginRegister, args);
            LOG = initLogging(Main.class, commandLineArguments);

            try {
                ReportDetails reportDetails = new ReportDetails();

                commandLineArguments.validate();

                if (printHelp(commandLineArguments)) {
                    return;
                }

                Configuration configuration = new ConfigurationManagerImpl().getConfiguration(commandLineArguments);

                if (flywayTelemetryManager != null) {
                    flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), configuration, LicenseGuard.getPermit(configuration).isRedgateEmployee()));
                }

                if (!commandLineArguments.skipCheckForUpdate()) {
                    MavenVersionChecker.checkForVersionUpdates();
                }

                LocalDateTime executionTime = LocalDateTime.now();
                OperationResult result = executeFlyway(flywayTelemetryManager, commandLineArguments, configuration);

                OperationResult filteredResults = filterHtmlResults(result);
                if (filteredResults != null) {
                    reportDetails = writeReport(configuration, filteredResults, executionTime);

                    Exception aggregate = getAggregateExceptions(filteredResults);
                    if (aggregate != null) {
                        throw aggregate;
                    }
                }

                if (commandLineArguments.shouldOutputJson()) {
                    printJson(commandLineArguments, result, reportDetails);
                }
            } catch (FlywayLicensingException e) {
                OperationResult errorOutput = ErrorOutput.toOperationResult(e);
                printError(commandLineArguments, e, errorOutput);
                exitCode = 35;
            } catch (Exception e) {
                OperationResult errorOutput = ErrorOutput.toOperationResult(e);
                printError(commandLineArguments, e, errorOutput);
                exitCode = 1;
            } finally {
                flushLog(commandLineArguments);
            }
        } finally {
            if (flywayTelemetryManager != null) {
                flywayTelemetryManager.close();
            }
        }

        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    
}
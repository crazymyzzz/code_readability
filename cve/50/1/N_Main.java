

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
            initLogging(commandLineArguments);

            try {
                commandLineArguments.validate();

                if (commandLineArguments.hasOperation("help") || commandLineArguments.shouldPrintUsage()) {
                    StringBuilder helpText = new StringBuilder();
                    boolean helpAsVerbWithOperation = commandLineArguments.hasOperation("help") && commandLineArguments.getOperations().size() > 1;
                    boolean helpAsFlagWithOperation = commandLineArguments.shouldPrintUsage() && commandLineArguments.getOperations().size() > 0;
                    if (helpAsVerbWithOperation || helpAsFlagWithOperation) {
                        for (String operation : commandLineArguments.getOperations()) {
                            String helpTextForOperation = pluginRegister.getPlugins(CommandExtension.class).stream()
                                                                        .filter(e -> e.handlesCommand(operation))
                                                                        .map(CommandExtension::getHelpText)
                                                                        .collect(Collectors.joining("\n\n"));

                            if (StringUtils.hasText(helpTextForOperation)) {
                                helpText.append(helpTextForOperation).append("\n\n");
                            }
                        }
                    }
                    if (!StringUtils.hasText(helpText.toString())) {
                        printUsage();
                    } else {
                        LOG.info(helpText.toString());
                    }
                    return;
                }

                Configuration configuration = useModernConfig(commandLineArguments) ? getConfiguration(commandLineArguments) : getLegacyConfiguration(commandLineArguments);

                if (flywayTelemetryManager != null) {
                    flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), configuration, isRedgateEmployee(pluginRegister, configuration)));
                }

                if (!commandLineArguments.skipCheckForUpdate()) {
                    MavenVersionChecker.checkForVersionUpdates();
                }

                LocalDateTime executionTime = LocalDateTime.now();

                OperationResult result = executeFlyway(flywayTelemetryManager, commandLineArguments, configuration);

                Exception aggregate = null;
                String jsonReportFilename = null;
                String htmlReportFilename = null;

                OperationResult filteredResults = filterHtmlResults(result);
                if (filteredResults != null) {
                    aggregate = getAggregateExceptions(filteredResults);
                    if (configuration.isReportEnabled()) {
                        CompositeResult<HtmlResult> htmlCompositeResult = flattenHtmlResults(filteredResults);

                        htmlCompositeResult.individualResults.forEach(r -> r.setTimestamp(executionTime));

                        String reportFilename = configuration.getReportFilename();
                        String baseReportFilename = getBaseFilename(reportFilename);

                        String tmpJsonReportFilename = baseReportFilename + JSON_REPORT_EXTENSION;
                        String tmpHtmlReportFilename = baseReportFilename + (reportFilename.endsWith(HTM_REPORT_EXTENSION) ? HTM_REPORT_EXTENSION : HTML_REPORT_EXTENSION);

                        try {
                            htmlCompositeResult = JsonUtils.appendIfExists(tmpJsonReportFilename, htmlCompositeResult, new CompositeResultDeserializer(configuration.getPluginRegister()));
                            jsonReportFilename = createJsonReport(htmlCompositeResult, tmpJsonReportFilename);
                            htmlReportFilename = createHtmlReport(configuration, htmlCompositeResult, tmpHtmlReportFilename);
                        } catch (FlywayException e) {
                            if (DEFAULT_REPORT_FILENAME.equals(configuration.getReportFilename())) {
                                LOG.warn("Unable to create default report files.");
                                if (LOG.isDebugEnabled()) {
                                    e.printStackTrace(System.out);
                                }
                            } else {
                                LOG.error("Unable to create report files", e);
                            }
                        }

                        if (htmlReportFilename != null) {
                            LOG.info("A Flyway report has been generated here: " + htmlReportFilename);
                        }
                    }
                }
                if (commandLineArguments.isCommunityFallback()) {
                    LOG.warn("A Flyway License was not provided; fell back to Community Edition. Please contact sales at sales@flywaydb.org for license information.");
                }

                if (aggregate != null) {
                    throw aggregate;
                }
                if (commandLineArguments.shouldOutputJson()) {
                    printJson(commandLineArguments, result, jsonReportFilename, htmlReportFilename);
                }
            } catch (DbMigrate.FlywayMigrateException e) {
                MigrateErrorResult errorResult = ErrorOutput.fromMigrateException(e);
                printError(commandLineArguments, e, errorResult);
                exitCode = 1;
            } catch (Exception e) {
                ErrorOutput errorOutput = ErrorOutput.fromException(e);
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
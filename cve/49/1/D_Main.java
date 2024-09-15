@@ -36,8 +36,6 @@ import org.flywaydb.core.api.output.*;
 import org.flywaydb.core.extensibility.CommandExtension;
 import org.flywaydb.core.extensibility.EventTelemetryModel;
 import org.flywaydb.core.extensibility.InfoTelemetryModel;
-import org.flywaydb.core.extensibility.RgDomainChecker;
-import org.flywaydb.core.extensibility.RootTelemetryModel;
 import org.flywaydb.core.internal.command.DbMigrate;
 import org.flywaydb.core.internal.configuration.ConfigUtils;
 import org.flywaydb.core.internal.configuration.TomlUtils;
@@ -45,11 +43,8 @@ import org.flywaydb.core.internal.configuration.models.ConfigurationModel;
 import org.flywaydb.core.internal.configuration.models.EnvironmentModel;
 import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
-import org.flywaydb.core.internal.database.base.Database;
 import org.flywaydb.core.internal.info.MigrationInfoDumper;
 
-import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
-import org.flywaydb.core.internal.license.VersionPrinter;
 
 import org.flywaydb.core.internal.logging.EvolvingLog;
 import org.flywaydb.core.internal.logging.buffered.BufferedLog;
@@ -70,6 +65,15 @@ import java.util.*;
 import java.util.stream.Collectors;
 import java.util.stream.Stream;
 
+import static org.flywaydb.commandline.utils.OperationsReportUtils.createHtmlReport;
+import static org.flywaydb.commandline.utils.OperationsReportUtils.createJsonReport;
+import static org.flywaydb.commandline.utils.OperationsReportUtils.filterHtmlResults;
+import static org.flywaydb.commandline.utils.OperationsReportUtils.flattenHtmlResults;
+import static org.flywaydb.commandline.utils.OperationsReportUtils.getAggregateExceptions;
+import static org.flywaydb.commandline.utils.OperationsReportUtils.getBaseFilename;
+import static org.flywaydb.commandline.utils.TelemetryUtils.isRedgateEmployee;
+import static org.flywaydb.commandline.utils.TelemetryUtils.populateRootTelemetry;
+
 public class Main {
     private static Log LOG;
     private static final PluginRegister pluginRegister = new PluginRegister();
@@ -99,10 +103,10 @@ public class Main {
     }
 
     public static void main(String[] args) throws Exception {
-        int exitCode=0;
+        int exitCode = 0;
 
         FlywayTelemetryManager flywayTelemetryManager = null;
-        if(!StringUtils.hasText(System.getenv("REDGATE_DISABLE_TELEMETRY"))) {
+        if (!StringUtils.hasText(System.getenv("REDGATE_DISABLE_TELEMETRY"))) {
             flywayTelemetryManager = new FlywayTelemetryManager(pluginRegister);
         }
 
@@ -139,27 +143,9 @@ public class Main {
                 }
 
                 Configuration configuration = useModernConfig(commandLineArguments) ? getConfiguration(commandLineArguments) : getLegacyConfiguration(commandLineArguments);
-
-                Flyway flyway = Flyway.configure(configuration.getClassLoader()).configuration(configuration).load();
-
-                if (flywayTelemetryManager != null) {
-                    RootTelemetryModel rootTelemetryModel = flywayTelemetryManager.getRootTelemetryModel();
-                    rootTelemetryModel.setApplicationVersion(VersionPrinter.getVersion());
-                    rootTelemetryModel.setApplicationEdition(VersionPrinter.EDITION.getDescription());
-                    RgDomainChecker domainChecker = pluginRegister.getPlugin(RgDomainChecker.class);
-                    if (domainChecker != null) {
-                        rootTelemetryModel.setRedgateEmployee(domainChecker.isInDomain(configuration));
-                    }
-                    ClassicConfiguration classicConfiguration = new ClassicConfiguration(configuration);
-                    if(classicConfiguration.getDataSource() != null) {
-                        try (JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(configuration.getDataSource(), configuration, null);
-                             Database database = jdbcConnectionFactory.getDatabaseType().createDatabase(configuration, false, jdbcConnectionFactory, null)) {
-                            rootTelemetryModel.setDatabaseEngine(database.getDatabaseType().getName());
-                        }
-                    } else {
-                        rootTelemetryModel.setDatabaseEngine("UNKNOWN");
-                    }
-
+                
+                if(flywayTelemetryManager != null) {
+                    flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), configuration, isRedgateEmployee(pluginRegister, configuration)));
                 }
 
                 if (!commandLineArguments.skipCheckForUpdate()) {
@@ -167,56 +153,45 @@ public class Main {
                 }
 
                 LocalDateTime executionTime = LocalDateTime.now();
-                CompositeResult<HtmlResult> htmlCompositeResult = new CompositeResult<>();
-                OperationResult result;
-                if (commandLineArguments.getOperations().size() == 1) {
-                    String operation = commandLineArguments.getOperations().get(0);
-                    result = executeOperation(flyway, operation, commandLineArguments, flywayTelemetryManager);
-                } else {
-                    CompositeResult<OperationResult> compositeResult = new CompositeResult<>();
+                
+                OperationResult result = executeFlyway(flywayTelemetryManager, commandLineArguments, configuration);
 
-                        for (String operation : commandLineArguments.getOperations()) {
-                            compositeResult.individualResults.add(executeOperation(flyway, operation, commandLineArguments, flywayTelemetryManager));
-                        }
-                    result = compositeResult;
-                }
                 Exception aggregate = null;
-                if (result instanceof CompositeResult<?>) {
-                    CompositeResult<?> compositeResult = (CompositeResult<?>) result;
-                    for(OperationResult individualResult : compositeResult.individualResults) {
-                        if (individualResult instanceof HtmlResult) {
-                            HtmlResult htmlResult = (HtmlResult) individualResult;
-                            htmlCompositeResult.individualResults.add(htmlResult);
-                            if(aggregate == null) {
-                                aggregate = htmlResult.exceptionObject;
-                            } else {
-                                aggregate.addSuppressed(htmlResult.exceptionObject);
-                            }
+                String jsonReportFilename = null;
+                String htmlReportFilename = null;
 
-                        }
-                    }
-                } else if (result instanceof HtmlResult) {
-                    HtmlResult htmlResult = (HtmlResult) result;
-                    htmlCompositeResult.individualResults.add(htmlResult);
-                    aggregate = htmlResult.exceptionObject;
-                }
+                OperationResult filteredResults = filterHtmlResults(result);
+                if (filteredResults != null) {
+                    aggregate = getAggregateExceptions(filteredResults);
+                    CompositeResult<HtmlResult> htmlCompositeResult = flattenHtmlResults(filteredResults);
+
+                    htmlCompositeResult.individualResults.forEach(r -> r.setTimestamp(executionTime));
+
+                    String reportFilename = configuration.getReportFilename();
+                    String baseReportFilename = getBaseFilename(reportFilename);
+
+                    String tmpJsonReportFilename = baseReportFilename + ".json";
+                    String tmpHtmlReportFilename = baseReportFilename + (reportFilename.endsWith(".htm") ? ".htm" : ".html");
 
-                htmlCompositeResult.individualResults.forEach(r -> r.timestamp = executionTime);
+                    htmlCompositeResult = JsonUtils.appendIfExists(tmpJsonReportFilename, htmlCompositeResult, new CompositeResultDeserializer(configuration.getPluginRegister()));
 
-                htmlCompositeResult = JsonUtils.appendIfExists(configuration.getReportFilename() + ".json", htmlCompositeResult, new CompositeResultDeserializer(configuration.getPluginRegister()));
-                String jsonReportFilename = JsonUtils.jsonToFile(configuration.getReportFilename() + ".json", htmlCompositeResult);
-                String htmlReportFilename = HtmlUtils.toHtmlFile(configuration.getReportFilename() + ".html", htmlCompositeResult, configuration);
+                    jsonReportFilename = createJsonReport(htmlCompositeResult, tmpJsonReportFilename);
+                    htmlReportFilename = createHtmlReport(configuration, htmlCompositeResult, tmpHtmlReportFilename);
 
+                    if (htmlReportFilename != null) {
+                        LOG.info("A Flyway report has been generated here: " + htmlReportFilename);
+                    }
+                }
                 if (commandLineArguments.isCommunityFallback()) {
                     LOG.warn("A Flyway License was not provided; fell back to Community Edition. Please contact sales at sales@flywaydb.org for license information.");
                 }
 
+                if (aggregate != null) {
+                    throw aggregate;
+                }
                 if (commandLineArguments.shouldOutputJson()) {
                     printJson(commandLineArguments, result, jsonReportFilename, htmlReportFilename);
                 }
-                if(aggregate != null) {
-                    throw aggregate;
-                }
             } catch (DbMigrate.FlywayMigrateException e) {
                 MigrateErrorResult errorResult = ErrorOutput.fromMigrateException(e);
                 printError(commandLineArguments, e, errorResult);
@@ -229,7 +204,7 @@ public class Main {
                 flushLog(commandLineArguments);
             }
         } finally {
-            if(flywayTelemetryManager != null) {
+            if (flywayTelemetryManager != null) {
                 flywayTelemetryManager.close();
             }
         }
@@ -239,6 +214,27 @@ public class Main {
         }
     }
 
+    private static OperationResult executeFlyway(FlywayTelemetryManager flywayTelemetryManager, CommandLineArguments commandLineArguments, Configuration configuration) {
+        Flyway flyway = Flyway.configure(configuration.getClassLoader()).configuration(configuration).load();
+        OperationResult result;
+        if (commandLineArguments.getOperations().size() == 1) {
+            String operation = commandLineArguments.getOperations().get(0);
+            result = executeOperation(flyway, operation, commandLineArguments, flywayTelemetryManager, configuration);
+        } else {
+            CompositeResult<OperationResult> compositeResult = new CompositeResult<>();
+
+            for (String operation : commandLineArguments.getOperations()) {
+                OperationResult operationResult = executeOperation(flyway, operation, commandLineArguments, flywayTelemetryManager, configuration);
+                compositeResult.individualResults.add(operationResult);
+                if (operationResult instanceof HtmlResult && ((HtmlResult) operationResult).exceptionObject instanceof DbMigrate.FlywayMigrateException) {
+                    break;
+                }
+            }
+            result = compositeResult;
+        }
+        return result;
+    }
+
     private static boolean useModernConfig(CommandLineArguments commandLineArguments) {
         List<File> tomlFiles = new ArrayList<>();
         tomlFiles.addAll(ConfigUtils.getDefaultTomlConfigFileLocations(new File(ClassUtils.getInstallDir(Main.class))));
@@ -420,7 +416,7 @@ public class Main {
     }
 
     @SneakyThrows
-    private static OperationResult executeOperation(Flyway flyway, String operation, CommandLineArguments commandLineArguments, FlywayTelemetryManager telemetryManager) {
+    private static OperationResult executeOperation(Flyway flyway, String operation, CommandLineArguments commandLineArguments, FlywayTelemetryManager telemetryManager, Configuration configuration) {
         OperationResult result = null;
         flyway.setFlywayTelemetryManager(telemetryManager);
         if ("clean".equals(operation)) {
@@ -428,7 +424,13 @@ public class Main {
         } else if ("baseline".equals(operation)) {
             result = flyway.baseline();
         } else if ("migrate".equals(operation)) {
-            result = flyway.migrate();
+            try {
+                result = flyway.migrate();
+            } catch (DbMigrate.FlywayMigrateException e) {
+                result = ErrorOutput.fromMigrateException(e);
+                HtmlResult hr = (HtmlResult) result;
+                hr.setException(e);
+            }
         } else if ("validate".equals(operation)) {
             try (EventTelemetryModel telemetryModel = new EventTelemetryModel("validate", telemetryManager)) {
                 try {
@@ -443,7 +445,7 @@ public class Main {
                 }
             }
         } else if ("info".equals(operation)) {
-            try(InfoTelemetryModel infoTelemetryModel = new InfoTelemetryModel(telemetryManager)) {
+            try (InfoTelemetryModel infoTelemetryModel = new InfoTelemetryModel(telemetryManager)) {
                 try {
                     MigrationInfoService info = flyway.info();
                     MigrationInfo current = info.current();
@@ -480,7 +482,7 @@ public class Main {
         } else if ("repair".equals(operation)) {
             result = flyway.repair();
         } else {
-            result = CommandExtensionUtils.runCommandExtension(flyway.getConfiguration(), operation, commandLineArguments.getFlags(), telemetryManager);
+            result = CommandExtensionUtils.runCommandExtension(configuration, operation, commandLineArguments.getFlags(), telemetryManager);
         }
 
         return result;
@@ -522,7 +524,7 @@ public class Main {
                 .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                 .create();
         JsonElement jsonElements = gson.toJsonTree(object);
-        if(jsonReport != null) {
+        if (jsonReport != null) {
             jsonElements.getAsJsonObject().addProperty("jsonReport", jsonReport);
             jsonElements.getAsJsonObject().addProperty("htmlReport", htmlReport);
         }
@@ -633,8 +635,8 @@ public class Main {
         LOG.info(indent + "-q                Suppress all output, except for errors and warnings");
         LOG.info(indent + "-n                Suppress prompting for a user and password");
         LOG.info(indent + "--help, -h, -?    Print this usage info and exit");
-        LOG.info(indent + "-community        Run the Flyway Community Edition (default)");
-        LOG.info(indent + "-teams            Run the Flyway Teams Edition");
+        LOG.info(indent + "-community        [deprecated] Run the Flyway Community Edition (default)");
+        LOG.info(indent + "-teams            [deprecated] Run the Flyway Teams Edition");
         LOG.info("");
         LOG.info("Flyway Usage Example");
         LOG.info(indent + "flyway -user=myuser -password=s3cr3t -url=jdbc:h2:mem -placeholders.abc=def migrate");

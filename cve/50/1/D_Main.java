@@ -149,8 +149,8 @@ public class Main {
                 }
 
                 Configuration configuration = useModernConfig(commandLineArguments) ? getConfiguration(commandLineArguments) : getLegacyConfiguration(commandLineArguments);
-                
-                if(flywayTelemetryManager != null) {
+
+                if (flywayTelemetryManager != null) {
                     flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), configuration, isRedgateEmployee(pluginRegister, configuration)));
                 }
 

@@ -167,36 +167,37 @@ public class Main {
                 String htmlReportFilename = null;
 
                 OperationResult filteredResults = filterHtmlResults(result);
-                if (filteredResults != null && configuration.isReportEnabled()) {
+                if (filteredResults != null) {
                     aggregate = getAggregateExceptions(filteredResults);
-                    CompositeResult<HtmlResult> htmlCompositeResult = flattenHtmlResults(filteredResults);
-
-                    htmlCompositeResult.individualResults.forEach(r -> r.setTimestamp(executionTime));
-
-                    String reportFilename = configuration.getReportFilename();
-                    String baseReportFilename = getBaseFilename(reportFilename);
-
-                    String tmpJsonReportFilename = baseReportFilename + JSON_REPORT_EXTENSION;
-                    String tmpHtmlReportFilename = baseReportFilename + (reportFilename.endsWith(HTM_REPORT_EXTENSION) ? HTM_REPORT_EXTENSION : HTML_REPORT_EXTENSION);
-
-
-                    try {
-                        htmlCompositeResult = JsonUtils.appendIfExists(tmpJsonReportFilename, htmlCompositeResult, new CompositeResultDeserializer(configuration.getPluginRegister()));
-                        jsonReportFilename = createJsonReport(htmlCompositeResult, tmpJsonReportFilename);
-                        htmlReportFilename = createHtmlReport(configuration, htmlCompositeResult, tmpHtmlReportFilename);
-                    } catch (FlywayException e) {
-                        if(DEFAULT_REPORT_FILENAME.equals(configuration.getReportFilename())) {
-                            LOG.warn("Unable to create default report files.");
-                            if(LOG.isDebugEnabled()) {
-                                e.printStackTrace(System.out);
+                    if (configuration.isReportEnabled()) {
+                        CompositeResult<HtmlResult> htmlCompositeResult = flattenHtmlResults(filteredResults);
+
+                        htmlCompositeResult.individualResults.forEach(r -> r.setTimestamp(executionTime));
+
+                        String reportFilename = configuration.getReportFilename();
+                        String baseReportFilename = getBaseFilename(reportFilename);
+
+                        String tmpJsonReportFilename = baseReportFilename + JSON_REPORT_EXTENSION;
+                        String tmpHtmlReportFilename = baseReportFilename + (reportFilename.endsWith(HTM_REPORT_EXTENSION) ? HTM_REPORT_EXTENSION : HTML_REPORT_EXTENSION);
+
+                        try {
+                            htmlCompositeResult = JsonUtils.appendIfExists(tmpJsonReportFilename, htmlCompositeResult, new CompositeResultDeserializer(configuration.getPluginRegister()));
+                            jsonReportFilename = createJsonReport(htmlCompositeResult, tmpJsonReportFilename);
+                            htmlReportFilename = createHtmlReport(configuration, htmlCompositeResult, tmpHtmlReportFilename);
+                        } catch (FlywayException e) {
+                            if (DEFAULT_REPORT_FILENAME.equals(configuration.getReportFilename())) {
+                                LOG.warn("Unable to create default report files.");
+                                if (LOG.isDebugEnabled()) {
+                                    e.printStackTrace(System.out);
+                                }
+                            } else {
+                                LOG.error("Unable to create report files", e);
                             }
-                        } else {
-                            LOG.error("Unable to create report files", e);
                         }
-                    }
 
-                    if (htmlReportFilename != null) {
-                        LOG.info("A Flyway report has been generated here: " + htmlReportFilename);
+                        if (htmlReportFilename != null) {
+                            LOG.info("A Flyway report has been generated here: " + htmlReportFilename);
+                        }
                     }
                 }
                 if (commandLineArguments.isCommunityFallback()) {

@@ -82,7 +82,7 @@ public class Main {
         FlywayTelemetryManager flywayTelemetryManager = null;
         if (!StringUtils.hasText(System.getenv("REDGATE_DISABLE_TELEMETRY"))) {
             flywayTelemetryManager = new FlywayTelemetryManager(pluginRegister);
-            flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), null, false));
+            flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), null, null));
         }
 
         try {
@@ -102,7 +102,7 @@ public class Main {
                 Configuration configuration = new ConfigurationManagerImpl().getConfiguration(commandLineArguments);
 
                 if (flywayTelemetryManager != null) {
-                    flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), configuration, LicenseGuard.getPermit(configuration).isRedgateEmployee()));
+                    flywayTelemetryManager.setRootTelemetryModel(populateRootTelemetry(flywayTelemetryManager.getRootTelemetryModel(), configuration, LicenseGuard.getPermit(configuration)));
                 }
 
                 if (!commandLineArguments.skipCheckForUpdate()) {


 @ExtensionMethod(Tier.class)
 public class TelemetryUtils {
 
-    public static RootTelemetryModel populateRootTelemetry(RootTelemetryModel rootTelemetryModel, Configuration configuration, boolean isRedgateEmployee) {
-        rootTelemetryModel.setRedgateEmployee(isRedgateEmployee);
+    public static RootTelemetryModel populateRootTelemetry(RootTelemetryModel rootTelemetryModel, Configuration configuration, FlywayPermit flywayPermit) {
+
+        rootTelemetryModel.setApplicationVersion(VersionPrinter.getVersion());
+
+        boolean isRGDomainSet = System.getenv("RGDOMAIN") != null;
+
+        if (flywayPermit != null) {
+            rootTelemetryModel.setRedgateEmployee(flywayPermit.isRedgateEmployee() || isRGDomainSet);
+            rootTelemetryModel.setApplicationEdition(flywayPermit.getTier().asString());
+            rootTelemetryModel.setTrial(flywayPermit.isTrial());
+            rootTelemetryModel.setSignedIn(flywayPermit.isFromAuth());
+        } else {
+            rootTelemetryModel.setRedgateEmployee(isRGDomainSet);
+        }
 
         if (configuration != null) {
-            String currentTier = LicenseGuard.getTierAsString(configuration);
-            rootTelemetryModel.setApplicationEdition(currentTier);
-            rootTelemetryModel.setApplicationVersion(VersionPrinter.getVersion());
-            rootTelemetryModel.setTrial(LicenseGuard.getPermit(configuration).isTrial());
             ConfigurationModel modernConfig = configuration.getModernConfig();
             if (modernConfig != null && StringUtils.hasText(modernConfig.getId())) {
                 rootTelemetryModel.setProjectId(EncryptionUtils.hashString(modernConfig.getId(), "fur"));

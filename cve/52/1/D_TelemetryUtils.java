@@ -52,7 +52,7 @@ public class TelemetryUtils {
             rootTelemetryModel.setTrial(LicenseGuard.getPermit(configuration).isTrial());
             ConfigurationModel modernConfig = configuration.getModernConfig();
             if (modernConfig != null && StringUtils.hasText(modernConfig.getId())) {
-                rootTelemetryModel.setProjectId(EncryptionUtils.hashProjectId(modernConfig.getId(), "fur"));
+                rootTelemetryModel.setProjectId(EncryptionUtils.hashString(modernConfig.getId(), "fur"));
             }
         }
 

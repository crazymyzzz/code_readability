@@ -40,6 +40,12 @@ import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 
+
+
+
+
+
+
 @NoArgsConstructor(access = AccessLevel.PRIVATE)
 @ExtensionMethod(Tier.class)
 public class TelemetryUtils {
@@ -70,6 +76,8 @@ public class TelemetryUtils {
             if (modernConfig != null && StringUtils.hasText(modernConfig.getId())) {
                 rootTelemetryModel.setProjectId(EncryptionUtils.hashString(modernConfig.getId(), "fur"));
             }
+
+            rootTelemetryModel.setSecretsManagementType(getSecretsManagementType(configuration));
         }
         
         rootTelemetryModel.setContainerType(getContainerType(Paths::get));
@@ -145,4 +153,24 @@ public class TelemetryUtils {
             return "";
         }
     }
+
+    private static String getSecretsManagementType(Configuration configuration) {
+
+
+
+
+
+
+
+
+
+
+
+
+
+
+
+
+        return "None";
+    }
 }

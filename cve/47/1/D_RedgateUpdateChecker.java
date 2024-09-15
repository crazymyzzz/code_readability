@@ -55,7 +55,7 @@ public class RedgateUpdateChecker {
     private static final String CFU_ENDPOINT = "/flyway/cfu/api/v0/cfu";
 
     public static boolean isEnabled() {
-        return usageChecker("flyway-cfu", VersionPrinter.getVersion());
+        return !Boolean.parseBoolean(System.getenv("REDGATE_DISABLE_TELEMETRY")) && usageChecker("flyway-cfu", VersionPrinter.getVersion());
     }
 
     public static void checkForVersionUpdates(Context context) {

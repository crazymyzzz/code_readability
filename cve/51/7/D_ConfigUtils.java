@@ -131,7 +131,7 @@ public class ConfigUtils {
         return result;
     }
 
-    private static String convertKey(String key) {
+    public static String convertKey(String key) {
         if ("FLYWAY_BASELINE_DESCRIPTION".equals(key)) {
             return BASELINE_DESCRIPTION;
         }
@@ -559,11 +559,16 @@ public class ConfigUtils {
      * @throws FlywayException when the property value is not a valid boolean.
      */
     public static Boolean removeBoolean(Map<String, String> config, String key) {
+
+        if (config == null) {
+            return null;
+        }
+
         String value = config.remove(key);
         if (value == null) {
             return null;
         }
-        if (!"true".equals(value) && !"false".equals(value)) {
+        if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
             throw new FlywayException("Invalid value for " + key + " (should be either true or false): " + value,
                                       ErrorCode.CONFIGURATION);
         }

@@ -335,6 +335,10 @@ public class ConfigUtils {
             return ORACLE_WALLET_LOCATION;
         }
 
+        if ("FLYWAY_REPORT_FILENAME".endsWith(key)) {
+            return REPORT_FILENAME;
+        }
+
         // Command-line specific
         if ("FLYWAY_JAR_DIRS".equals(key)) {
             return JAR_DIRS;
@@ -345,6 +349,7 @@ public class ConfigUtils {
             return CONFIGURATIONS;
         }
 
+
         for (ConfigurationExtension configurationExtension : PLUGIN_REGISTER.getPlugins(ConfigurationExtension.class)) {
             String configurationParameter = configurationExtension.getConfigurationParameterFromEnvironmentVariable(key);
             if (configurationParameter != null) {

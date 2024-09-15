@@ -15,16 +15,19 @@
  */
 package org.flywaydb.core.internal.configuration;
 
+import java.util.stream.Collectors;
 import lombok.AccessLevel;
 import lombok.CustomLog;
 import lombok.NoArgsConstructor;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.Location;
 import org.flywaydb.core.api.configuration.Configuration;
+import org.flywaydb.core.api.configuration.FluentConfiguration;
 import org.flywaydb.core.extensibility.ConfigurationExtension;
 import org.flywaydb.core.internal.command.clean.CleanModel;
 import org.flywaydb.core.internal.configuration.models.ConfigurationModel;
+import org.flywaydb.core.internal.configuration.models.EnvironmentModel;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
 import org.flywaydb.core.internal.plugin.PluginRegister;
 import org.flywaydb.core.internal.util.ClassUtils;
@@ -56,6 +59,7 @@ import static org.flywaydb.core.internal.sqlscript.SqlScriptMetadata.isMultiline
 @NoArgsConstructor(access = AccessLevel.PRIVATE)
 public class ConfigUtils {
     public static final String DEFAULT_CLI_SQL_LOCATION = "sql";
+    public static final String DEFAULT_CLI_JARS_LOCATION = "jars";
     public static final String CONFIG_FILE_NAME = "flyway.conf";
     public static final String CONFIG_FILES = "flyway.configFiles";
     public static final String CONFIG_FILE_ENCODING = "flyway.configFileEncoding";
@@ -66,6 +70,7 @@ public class ConfigUtils {
     public static final String CALLBACKS = "flyway.callbacks";
     public static final String CLEAN_DISABLED = "flyway.cleanDisabled";
     public static final String CLEAN_ON_VALIDATION_ERROR = "flyway.cleanOnValidationError";
+    public static final String COMMUNITY_DB_SUPPORT_ENABLED = "flyway.communityDBSupportEnabled";
     public static final String CONNECT_RETRIES = "flyway.connectRetries";
     public static final String CONNECT_RETRIES_INTERVAL = "flyway.connectRetriesInterval";
     public static final String DEFAULT_SCHEMA = "flyway.defaultSchema";
@@ -175,6 +180,9 @@ public class ConfigUtils {
         if ("FLYWAY_CLEAN_ON_VALIDATION_ERROR".equals(key)) {
             return CLEAN_ON_VALIDATION_ERROR;
         }
+        if ("FLYWAY_COMMUNITY_DB_SUPPORT_DISABLED".equals(key)) {
+            return COMMUNITY_DB_SUPPORT_ENABLED;
+        }
         if ("FLYWAY_CONFIG_FILE_ENCODING".equals(key)) {
             return CONFIG_FILE_ENCODING;
         }
@@ -622,7 +630,7 @@ public class ConfigUtils {
         }
         if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
             throw new FlywayException("Invalid value for " + key + " (should be either true or false): " + value,
-                                      ErrorCode.CONFIGURATION);
+                                      CoreErrorCode.CONFIGURATION);
         }
         return Boolean.valueOf(value);
     }
@@ -644,7 +652,7 @@ public class ConfigUtils {
             return Integer.valueOf(value);
         } catch (NumberFormatException e) {
             throw new FlywayException("Invalid value for " + key + " (should be an integer): " + value,
-                                      ErrorCode.CONFIGURATION);
+                                      CoreErrorCode.CONFIGURATION);
         }
     }
 
@@ -715,7 +723,7 @@ public class ConfigUtils {
             String message = String.format("Unknown configuration %s: %s",
                                            property,
                                            StringUtils.arrayToCommaDelimitedString(unknownFlywayProperties.toArray()));
-            throw new FlywayException(message, ErrorCode.CONFIGURATION);
+            throw new FlywayException(message, CoreErrorCode.CONFIGURATION);
         }
     }
 
@@ -810,10 +818,38 @@ public class ConfigUtils {
     }
 
     public static String getFilenameWithWorkingDirectory(String filename, Configuration conf) {
-        if (conf.getWorkingDirectory() != null && !new File(filename).isAbsolute()) {
-            return new File(conf.getWorkingDirectory(), filename).getPath();
+        return getFilenameWithWorkingDirectory(filename, conf.getWorkingDirectory());
+    }
+
+    public static String getFilenameWithWorkingDirectory(String filename, String workingDirectory) {
+        if (workingDirectory != null && !new File(filename).isAbsolute()) {
+            return new File(workingDirectory, filename).getPath();
         } else {
             return filename;
         }
     }
+
+    public static void makeRelativeJarDirsBasedOnWorkingDirectory(String workingDirectory, Map<String, String> config) {
+        String jarDirsString = config.get(ConfigUtils.JAR_DIRS);
+        String[] jarDirs = new String[0];
+
+        if (StringUtils.hasText(jarDirsString)) {
+            jarDirs = jarDirsString.split(",");
+        }
+
+        jarDirs = Arrays.stream(jarDirs)
+            .map(dir -> getFilenameWithWorkingDirectory(dir, workingDirectory))
+            .toArray(String[]::new);
+
+        config.put(ConfigUtils.JAR_DIRS, StringUtils.arrayToCommaDelimitedString(jarDirs));
+    }
+
+    public static void makeRelativeJarDirsInEnvironmentsBasedOnWorkingDirectory(String workingDirectory, Map<String, EnvironmentModel> environments) {
+        environments.forEach((key, model) -> {
+            List<String> jarDirs = model.getJarDirs().stream()
+                .map(dir -> getFilenameWithWorkingDirectory(dir, workingDirectory))
+                .collect(Collectors.toList());
+            model.setJarDirs(jarDirs);
+        });
+    }
 }
\ No newline at end of file

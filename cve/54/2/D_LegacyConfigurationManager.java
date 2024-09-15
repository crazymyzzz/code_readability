@@ -29,16 +29,14 @@ import org.flywaydb.core.extensibility.Tier;
 
 import java.io.Console;
 import java.io.File;
-import java.nio.file.Paths;
 import java.util.ArrayList;
-import java.util.Arrays;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
-import java.util.stream.Collectors;
-import java.util.stream.Stream;
 
+import static org.flywaydb.core.internal.configuration.ConfigUtils.DEFAULT_CLI_JARS_LOCATION;
 import static org.flywaydb.core.internal.configuration.ConfigUtils.DEFAULT_CLI_SQL_LOCATION;
+import static org.flywaydb.core.internal.configuration.ConfigUtils.makeRelativeJarDirsBasedOnWorkingDirectory;
 import static org.flywaydb.core.internal.configuration.ConfigUtils.makeRelativeLocationsBasedOnWorkingDirectory;
 
 @CustomLog
@@ -50,7 +48,7 @@ public class LegacyConfigurationManager implements ConfigurationManager {
         String installDirectory = commandLineArguments.isWorkingDirectorySet() ? commandLineArguments.getWorkingDirectory() : ClassUtils.getInstallDir(Main.class);
         String workingDirectory = commandLineArguments.getWorkingDirectoryOrNull();
 
-        File jarDir = new File(installDirectory, "jars");
+        File jarDir = new File(installDirectory, DEFAULT_CLI_JARS_LOCATION);
         ConfigUtils.warnIfUsingDeprecatedMigrationsFolder(jarDir, ".jar");
         if (jarDir.exists()) {
             config.put(ConfigUtils.JAR_DIRS, jarDir.getAbsolutePath());
@@ -70,20 +68,10 @@ public class LegacyConfigurationManager implements ConfigurationManager {
 
         if (workingDirectory != null) {
             makeRelativeLocationsBasedOnWorkingDirectory(workingDirectory, config);
+            makeRelativeJarDirsBasedOnWorkingDirectory(workingDirectory, config);
         }
 
-        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
-
-        List<File> jarFiles = new ArrayList<>(CommandLineConfigurationUtils.getJdbcDriverJarFiles());
-
-        String jarDirs = config.get(ConfigUtils.JAR_DIRS);
-        if (StringUtils.hasText(jarDirs)) {
-            jarFiles.addAll(CommandLineConfigurationUtils.getJavaMigrationJarFiles(StringUtils.tokenizeToStringArray(jarDirs.replace(File.pathSeparator, ","), ",")));
-        }
-
-        if (!jarFiles.isEmpty()) {
-            classLoader = ClassUtils.addJarsOrDirectoriesToClasspath(classLoader, jarFiles);
-        }
+        ClassLoader classLoader = buildClassLoaderBasedOnJarDirs(Thread.currentThread().getContextClassLoader(), config);
 
         ConfigUtils.dumpConfigurationMap(config);
         filterProperties(config);
@@ -196,7 +184,7 @@ public class LegacyConfigurationManager implements ConfigurationManager {
      * Detect whether the JDBC URL specifies a known authentication mechanism that does not need a username.
      */
     boolean needsUser(String url, String password, Configuration configuration) {
-        DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForUrl(url);
+        DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForUrl(url, configuration);
         if (databaseType.detectUserRequiredByUrl(url)) {
 
 
@@ -218,7 +206,7 @@ public class LegacyConfigurationManager implements ConfigurationManager {
      * Detect whether the JDBC URL specifies a known authentication mechanism that does not need a password.
      */
     boolean needsPassword(String url, String username, Configuration configuration) {
-        DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForUrl(url);
+        DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForUrl(url, configuration);
         if (databaseType.detectPasswordRequiredByUrl(url)) {
 
 
@@ -239,8 +227,23 @@ public class LegacyConfigurationManager implements ConfigurationManager {
      * Filters the properties to remove the Flyway Commandline-specific ones.
      */
     private void filterProperties(Map<String, String> config) {
-        config.remove(ConfigUtils.JAR_DIRS);
         config.remove(ConfigUtils.CONFIG_FILES);
         config.remove(ConfigUtils.CONFIG_FILE_ENCODING);
     }
+
+    private ClassLoader buildClassLoaderBasedOnJarDirs(ClassLoader parentClassLoader, Map<String, String> config) {
+        List<File> jarFiles = new ArrayList<>(CommandLineConfigurationUtils.getJdbcDriverJarFiles());
+
+        String jarDirs = config.get(ConfigUtils.JAR_DIRS);
+
+        if (StringUtils.hasText(jarDirs)) {
+            jarFiles.addAll(CommandLineConfigurationUtils.getJavaMigrationJarFiles(StringUtils.tokenizeToStringArray(jarDirs.replace(File.pathSeparator, ","), ",")));
+        }
+
+        if (!jarFiles.isEmpty()) {
+            return ClassUtils.addJarsOrDirectoriesToClasspath(parentClassLoader, jarFiles);
+        }
+
+        return parentClassLoader;
+    }
 }
\ No newline at end of file

@@ -29,10 +29,6 @@ import org.flywaydb.core.internal.configuration.TomlUtils;
 import org.flywaydb.core.internal.configuration.models.ConfigurationModel;
 import org.flywaydb.core.internal.configuration.models.EnvironmentModel;
 import org.flywaydb.core.internal.configuration.models.ResolvedEnvironment;
-import org.flywaydb.core.internal.configuration.resolvers.EnvironmentProvisioner;
-import org.flywaydb.core.internal.configuration.resolvers.EnvironmentResolver;
-import org.flywaydb.core.internal.configuration.resolvers.PropertyResolver;
-import org.flywaydb.core.internal.plugin.PluginRegister;
 import org.flywaydb.core.internal.util.ClassUtils;
 import org.flywaydb.core.internal.util.MergeUtils;
 
@@ -46,7 +42,9 @@ import java.util.Map;
 import java.util.regex.Pattern;
 import java.util.stream.Collectors;
 
+import static org.flywaydb.core.internal.configuration.ConfigUtils.DEFAULT_CLI_JARS_LOCATION;
 import static org.flywaydb.core.internal.configuration.ConfigUtils.DEFAULT_CLI_SQL_LOCATION;
+import static org.flywaydb.core.internal.configuration.ConfigUtils.makeRelativeJarDirsInEnvironmentsBasedOnWorkingDirectory;
 import static org.flywaydb.core.internal.configuration.ConfigUtils.makeRelativeLocationsBasedOnWorkingDirectory;
 
 @CustomLog
@@ -140,8 +138,8 @@ public class ModernConfigurationManager implements ConfigurationManager {
         }
 
         if (workingDirectory != null) {
-            makeRelativeLocationsBasedOnWorkingDirectory(workingDirectory,
-                config.getFlyway().getLocations());
+            makeRelativeLocationsBasedOnWorkingDirectory(workingDirectory, config.getFlyway().getLocations());
+            makeRelativeJarDirsInEnvironmentsBasedOnWorkingDirectory(workingDirectory, config.getEnvironments());
         }
 
         ConfigUtils.dumpConfigurationModel(config);
@@ -198,7 +196,7 @@ public class ModernConfigurationManager implements ConfigurationManager {
     private static void loadJarDirsAndAddToClasspath(String workingDirectory, ClassicConfiguration cfg) {
         List<String> jarDirs = new ArrayList<>();
 
-        File jarDir = new File(workingDirectory, "jars");
+        File jarDir = new File(workingDirectory, DEFAULT_CLI_JARS_LOCATION);
         ConfigUtils.warnIfUsingDeprecatedMigrationsFolder(jarDir, ".jar");
         if (jarDir.exists()) {
             jarDirs.add(jarDir.getAbsolutePath());

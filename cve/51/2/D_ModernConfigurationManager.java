@@ -55,14 +55,26 @@ public class ModernConfigurationManager implements ConfigurationManager {
         ConfigurationModel config = TomlUtils.loadConfigurationFiles(existingFiles, workingDirectory);
 
         ConfigurationModel commandLineArgumentsModel = TomlUtils.loadConfigurationFromCommandlineArgs(commandLineArguments.getConfiguration());
-        config = config.merge(TomlUtils.loadConfigurationFromEnvironment())
+        ConfigurationModel environmentVariablesModel = TomlUtils.loadConfigurationFromEnvironment();
+        config = config.merge(environmentVariablesModel)
                        .merge(commandLineArgumentsModel);
 
-        if (commandLineArgumentsModel.getEnvironments().containsKey(ClassicConfiguration.TEMP_ENVIRONMENT_NAME)) {
+        if (commandLineArgumentsModel.getEnvironments().containsKey(ClassicConfiguration.TEMP_ENVIRONMENT_NAME) ||
+                environmentVariablesModel.getEnvironments().containsKey(ClassicConfiguration.TEMP_ENVIRONMENT_NAME)) {
             EnvironmentModel defaultEnv = config.getEnvironments().get(config.getFlyway().getEnvironment());
-            config.getEnvironments().put(config.getFlyway().getEnvironment(), defaultEnv.merge(commandLineArgumentsModel.getEnvironments().get(ClassicConfiguration.TEMP_ENVIRONMENT_NAME)));
+
+            if (environmentVariablesModel.getEnvironments().containsKey(ClassicConfiguration.TEMP_ENVIRONMENT_NAME)) {
+                config.getEnvironments().put(config.getFlyway().getEnvironment(), defaultEnv.merge(environmentVariablesModel.getEnvironments().get(ClassicConfiguration.TEMP_ENVIRONMENT_NAME)));
+            }
+
+            if (commandLineArgumentsModel.getEnvironments().containsKey(ClassicConfiguration.TEMP_ENVIRONMENT_NAME)) {
+                config.getEnvironments().put(config.getFlyway().getEnvironment(), defaultEnv.merge(commandLineArgumentsModel.getEnvironments().get(ClassicConfiguration.TEMP_ENVIRONMENT_NAME)));
+            }
+
             config.getEnvironments().remove(ClassicConfiguration.TEMP_ENVIRONMENT_NAME);
         }
+
+
         config.getFlyway().getLocations().add("filesystem:" + new File(workingDirectory, "sql").getAbsolutePath());
 
         List<String> jarDirs = new ArrayList<>();
@@ -103,7 +115,7 @@ public class ModernConfigurationManager implements ConfigurationManager {
                 }
                 try {
                     ConfigurationExtension newConfigurationExtension = objectMapper.convertValue(values, configurationExtension.getClass());
-                    MergeUtils.mergeModel(configurationExtension, newConfigurationExtension);
+                    MergeUtils.mergeModel(newConfigurationExtension, configurationExtension);
                 } catch (IllegalArgumentException e) {
                     Matcher matcher = ANY_WORD_BETWEEN_TWO_QUOTES_PATTERN.matcher(e.getMessage());
                     if (matcher.find()) {

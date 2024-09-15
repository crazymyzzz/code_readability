
@@ -35,7 +37,7 @@ class ParsedDockerComposeFile {
     private Map<String, Set<String>> serviceNameToImageNames = new HashMap<>();
 
     ParsedDockerComposeFile(File composeFile) {
-        Yaml yaml = new Yaml();
+        Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
         try (FileInputStream fileInputStream = FileUtils.openInputStream(composeFile)) {
             composeFileContent = yaml.load(fileInputStream);
         } catch (Exception e) {

@@ -45,7 +45,7 @@ public class SqlScriptMetadata {
     private String shouldExecuteExpression;
     private boolean shouldExecute;
 
-    private SqlScriptMetadata(Map<String, String> metadata, Configuration config) {
+    private SqlScriptMetadata(Map<String, String> metadata, Map<String, String> unmappedMetadata, Configuration config) {
         // Make copy to prevent removing elements from the original
         metadata = new HashMap<>(metadata);
 
@@ -100,13 +100,16 @@ public class SqlScriptMetadata {
     public static SqlScriptMetadata fromResource(LoadableResource resource, Parser parser, Configuration config) {
         if (resource != null) {
             LOG.debug("Found script configuration: " + resource.getFilename());
+            var unmappedMetadata = ConfigUtils.loadConfigurationFromReader(resource.read());
             if (parser == null) {
-                return new SqlScriptMetadata(ConfigUtils.loadConfigurationFromReader(resource.read()), config);
+                return new SqlScriptMetadata(unmappedMetadata, unmappedMetadata, config);
             }
-            return new SqlScriptMetadata(ConfigUtils.loadConfigurationFromReader(
-                    PlaceholderReplacingReader.create(parser.configuration, parser.parsingContext, resource.read())), parser.configuration);
+
+            var mappedMetadata = ConfigUtils.loadConfigurationFromReader(
+                PlaceholderReplacingReader.create(parser.configuration, parser.parsingContext, resource.read()));
+            return new SqlScriptMetadata(mappedMetadata, unmappedMetadata, parser.configuration);
         }
-        return new SqlScriptMetadata(new HashMap<>(), config);
+        return new SqlScriptMetadata(new HashMap<>(), new HashMap<>(), config);
     }
 
     public static LoadableResource getMetadataResource(ResourceProvider resourceProvider, LoadableResource resource) {

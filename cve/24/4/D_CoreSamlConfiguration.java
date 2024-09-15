@@ -49,12 +49,12 @@ public class CoreSamlConfiguration {
         val properties = new Properties();
         properties.put(RuntimeConstants.INPUT_ENCODING, StandardCharsets.UTF_8.name());
         properties.put(RuntimeConstants.ENCODING_DEFAULT, StandardCharsets.UTF_8.name());
-        properties.put(RuntimeConstants.RESOURCE_LOADER, "file, classpath, string");
+        properties.put(RuntimeConstants.RESOURCE_LOADERS, "file, classpath, string");
         properties.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, FileUtils.getTempDirectory().getAbsolutePath());
         properties.put(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, Boolean.FALSE);
-        properties.put("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
-        properties.put("string.resource.loader.class", StringResourceLoader.class.getName());
-        properties.put("file.resource.loader.class", FileResourceLoader.class.getName());
+        properties.put("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
+        properties.put("resource.loader.string.class", StringResourceLoader.class.getName());
+        properties.put("resource.loader.file.class", FileResourceLoader.class.getName());
 
         return new VelocityEngine(properties);
     }

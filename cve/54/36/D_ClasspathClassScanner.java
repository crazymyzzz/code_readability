@@ -35,7 +35,7 @@ public class ClasspathClassScanner {
 
     public List<String> scanForType(String location, Class<?> classType, boolean errorOnNotFound) {
         ClassPathScanner<?> s = new ClassPathScanner<>(classType, classLoader, Charset.defaultCharset(), new Location("classpath:" + location),
-                                                       resourceNameCache, locationScannerCache, errorOnNotFound);
+                                                       resourceNameCache, locationScannerCache, errorOnNotFound, false);
 
         List<String> discoveredTypes = new ArrayList<>();
         for (LoadableResource resource : s.scanForResources()) {

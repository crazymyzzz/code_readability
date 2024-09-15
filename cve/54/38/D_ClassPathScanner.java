@@ -60,10 +60,12 @@ public class ClassPathScanner<I> implements ResourceAndClassScanner<I> {
      */
     private final boolean throwOnMissingLocations;
 
+
     public ClassPathScanner(Class<I> implementedInterface, ClassLoader classLoader, Charset encoding, Location location,
                             ResourceNameCache resourceNameCache,
                             LocationScannerCache locationScannerCache,
-                            boolean throwOnMissingLocations) {
+                            boolean throwOnMissingLocations,
+                            boolean stream) {
         this.implementedInterface = implementedInterface;
         this.classLoader = classLoader;
         this.location = location;
@@ -75,7 +77,7 @@ public class ClassPathScanner<I> implements ResourceAndClassScanner<I> {
         for (Pair<String, String> resourceNameAndParentURL : findResourceNamesAndParentURLs()) {
             String resourceName = resourceNameAndParentURL.getLeft();
             String parentURL = resourceNameAndParentURL.getRight();
-            resources.add(new ClassPathResource(location, resourceName, classLoader, encoding, parentURL));
+            resources.add(new ClassPathResource(location, resourceName, classLoader, encoding, parentURL, stream));
             LOG.debug("Found resource: " + resourceNameAndParentURL.getLeft());
         }
     }

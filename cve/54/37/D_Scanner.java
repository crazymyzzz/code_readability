@@ -93,7 +93,7 @@ public class Scanner<I> implements ResourceProvider, ClassProvider<I> {
                     LOG.error("Can't read location " + location + "; AWS SDK not found");
                 }
             } else {
-                ResourceAndClassScanner<I> resourceAndClassScanner = new ClassPathScanner<>(implementedInterface, classLoader, encoding, location, resourceNameCache, locationScannerCache, throwOnMissingLocations);
+                ResourceAndClassScanner<I> resourceAndClassScanner = new ClassPathScanner<>(implementedInterface, classLoader, encoding, location, resourceNameCache, locationScannerCache, throwOnMissingLocations, stream);
                 resources.addAll(resourceAndClassScanner.scanForResources());
                 classes.addAll(resourceAndClassScanner.scanForClasses());
             }

@@ -70,7 +70,6 @@ public class Scanner<I> implements ResourceProvider, ClassProvider<I> {
         FileSystemScanner fileSystemScanner = new FileSystemScanner(stream, configuration);
 
         FeatureDetector detector = new FeatureDetector(classLoader);
-        long cloudMigrationCount = 0;
         for (Location location : configuration.getLocations()) {
             if (location.isFileSystem()) {
                 resources.addAll(fileSystemScanner.scanForResources(location));
@@ -87,12 +86,10 @@ public class Scanner<I> implements ResourceProvider, ClassProvider<I> {
 
 
 
-
             } else if (location.isAwsS3()) {
                 if (detector.isAwsAvailable()) {
                     Collection<LoadableResource> awsResources = new AwsS3Scanner(encoding, throwOnMissingLocations).scanForResources(location);
                     resources.addAll(awsResources);
-                    cloudMigrationCount += awsResources.stream().filter(r -> r.getFilename().endsWith(".sql")).count();
                 } else {
                     LOG.error("Can't read location " + location + "; AWS SDK not found");
                 }
@@ -103,12 +100,6 @@ public class Scanner<I> implements ResourceProvider, ClassProvider<I> {
             }
         }
 
-
-        if (cloudMigrationCount > 100L) {
-            throw new FlywayEditionUpgradeRequiredException(Tier.TEAMS, LicenseGuard.getTier(configuration), "Cloud locations with more than 100 migrations");
-        }
-
-
         for (LoadableResource resource : resources) {
             relativeResourceMap.put(resource.getRelativePath().toLowerCase(), resource);
         }

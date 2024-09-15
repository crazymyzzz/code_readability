@@ -85,7 +85,7 @@ public class MavenVersionChecker {
             MigrationVersion latest = MigrationVersion.fromVersion(metadata.getVersioning().getRelease());
 
             if (current.compareTo(latest) < 0) {
-                LOG.warn("This version of Flyway is out of date. Upgrade to Flyway " + latest + ": "
+                LOG.info("A more recent version of Flyway is available. Find out more about Flyway " + latest + " at "
                                  + FlywayDbWebsiteLinks.STAYING_UP_TO_DATE + "\n");
             }
         } catch (Exception e) {

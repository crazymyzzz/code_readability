@@ -73,7 +73,7 @@ public class ResourceNameValidator {
     private boolean isSpecialResourceFile(Configuration configuration, String filename) {
         try {
         DatabaseType databaseType = configuration.getDatabaseType();
-        return databaseType.getSpecialResourceFilenames(configuration).contains(filename);
+        return databaseType.getSpecialResourceFilenames(configuration).contains(filename.toLowerCase());
         } catch (Exception e) {
             return false;
         }

@@ -146,17 +146,4 @@ public class FileSystemScanner {
 
         return resourceNames;
     }
-
-    private enum DirectoryValidationResult {
-        NOT_FOUND,
-        NOT_READABLE,
-        NOT_A_DIRECTORY,
-        UNABLE_TO_ACCESS_FOLDER,
-        VALID;
-
-        @Override
-        public String toString() {
-            return name().toLowerCase().replace('_', ' ');
-        }
-    }
 }

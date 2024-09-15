@@ -35,7 +35,7 @@ public class FileSystemScanner {
     private final Charset defaultEncoding;
     private final boolean detectEncoding;
     private final boolean throwOnMissingLocations;
-    private boolean stream = false;
+    private final boolean stream;
     private Configuration config;
 
     public FileSystemScanner(boolean stream, Configuration config) {

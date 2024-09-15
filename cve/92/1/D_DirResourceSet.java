@@ -217,6 +217,12 @@ public class DirResourceSet extends AbstractFileResourceSet {
             return false;
         }
 
+        // write() is meant to create a file so ensure that the path doesn't
+        // end in '/'
+        if (path.endsWith("/")) {
+            return false;
+        }
+
         File dest = null;
         String webAppMount = getWebAppMount();
         if (path.startsWith(webAppMount)) {

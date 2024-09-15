@@ -57,6 +57,14 @@ public abstract class AbstractFileResourceSet extends AbstractResourceSet {
             name = "";
         }
         File file = new File(fileBase, name);
+
+        // If the requested names ends in '/', the Java File API will return a
+        // matching file if one exists. This isn't what we want as it is not
+        // consistent with the Servlet spec rules for request mapping.
+        if (file.isFile() && name.endsWith("/")) {
+            return null;
+        }
+
         if (!mustExist || file.canRead()) {
 
             if (getRoot().getAllowLinking()) {

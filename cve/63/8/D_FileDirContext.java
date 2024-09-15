
@@ -768,50 +769,10 @@ public class FileDirContext extends BaseDirContext {
      */
     protected String normalize(String path) {
 
-    String normalized = path;
-
-    // Normalize the slashes and add leading slash if necessary
-    if (File.separatorChar == '\\' && normalized.indexOf('\\') >= 0)
-        normalized = normalized.replace('\\', '/');
-    if (!normalized.startsWith("/"))
-        normalized = "/" + normalized;
-
-    // Resolve occurrences of "//" in the normalized path
-    while (true) {
-        int index = normalized.indexOf("//");
-        if (index < 0)
-        break;
-        normalized = normalized.substring(0, index) +
-        normalized.substring(index + 1);
-    }
-
-    // Resolve occurrences of "/./" in the normalized path
-    while (true) {
-        int index = normalized.indexOf("/./");
-        if (index < 0)
-        break;
-        normalized = normalized.substring(0, index) +
-        normalized.substring(index + 2);
-    }
+        return RequestUtil.normalize(path, File.separatorChar == '\\');
 
-    // Resolve occurrences of "/../" in the normalized path
-    while (true) {
-        int index = normalized.indexOf("/../");
-        if (index < 0)
-        break;
-        if (index == 0)
-        return (null);  // Trying to go outside our context
-        int index2 = normalized.lastIndexOf('/', index - 1);
-        normalized = normalized.substring(0, index2) +
-        normalized.substring(index + 3);
     }
 
-    // Return the normalized path that we have completed
-    return (normalized);
-
-    }
-
-
     /**
      * Return a File object representing the specified normalized
      * context-relative path if it exists and is readable.  Otherwise,

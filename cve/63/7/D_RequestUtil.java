@@ -93,6 +93,19 @@ public final class RequestUtil {
      * @param path Relative path to be normalized
      */
     public static String normalize(String path) {
+        return normalize(path, true);
+    }
+
+    /**
+     * Normalize a relative URI path that may have relative values ("/./",
+     * "/../", and so on ) it it.  <strong>WARNING</strong> - This method is
+     * useful only for normalizing application-generated paths.  It does not
+     * try to perform security checks for malicious input.
+     *
+     * @param path Relative path to be normalized
+     * @param replaceBackSlash Should '\\' be replaced with '/'
+     */
+    public static String normalize(String path, boolean replaceBackSlash) {
 
         if (path == null)
             return null;
@@ -100,6 +113,9 @@ public final class RequestUtil {
         // Create a place for the normalized path
         String normalized = path;
 
+        if (replaceBackSlash && normalized.indexOf('\\') >= 0)
+            normalized = normalized.replace('\\', '/');
+
         if (normalized.equals("/."))
             return "/";
 

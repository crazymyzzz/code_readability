@@ -47,7 +47,7 @@ public class SSIServletRequestUtil {
         if ((result == null) || (result.equals(""))) {
             result = "/";
         }
-        return normalize(result);
+        return RequestUtil.normalize(result);
     }
 
 
@@ -63,15 +63,9 @@ public class SSIServletRequestUtil {

     public static String normalize(String path) {
-        if (path == null) return null;
-        String normalized = path;
-        //Why doesn't RequestUtil do this??
-        // Normalize the slashes and add leading slash if necessary
-        if (normalized.indexOf('\\') >= 0)
-            normalized = normalized.replace('\\', '/');
-        normalized = RequestUtil.normalize(path);
-        return normalized;
+        return RequestUtil.normalize(path);
     }
 }
\ No newline at end of file

@@ -1297,10 +1297,9 @@ public class Request
         int pos = requestPath.lastIndexOf('/');
         String relative = null;
         if (pos >= 0) {
-            relative = RequestUtil.normalize
-                (requestPath.substring(0, pos + 1) + path);
+            relative = requestPath.substring(0, pos + 1) + path;
         } else {
-            relative = RequestUtil.normalize(requestPath + path);
+            relative = requestPath + path;
         }
 
         return (context.getServletContext().getRequestDispatcher(relative));

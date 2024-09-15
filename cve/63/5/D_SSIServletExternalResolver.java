
@@ -370,7 +371,7 @@ public class SSIServletExternalResolver implements SSIExternalResolver {
                     + pathWithoutContext);
         }
         String fullPath = prefix + path;
-        String retVal = SSIServletRequestUtil.normalize(fullPath);
+        String retVal = RequestUtil.normalize(fullPath);
         if (retVal == null) {
             throw new IOException("Normalization yielded null on path: "
                     + fullPath);
@@ -403,7 +404,7 @@ public class SSIServletExternalResolver implements SSIExternalResolver {
             return new ServletContextAndPath(context,
                     getAbsolutePath(virtualPath));
         } else {
-            String normalized = SSIServletRequestUtil.normalize(virtualPath);
+            String normalized = RequestUtil.normalize(virtualPath);
             if (isVirtualWebappRelative) {
                 return new ServletContextAndPath(context, normalized);
             } else {

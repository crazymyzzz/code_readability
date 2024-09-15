
@@ -410,7 +411,7 @@ public class ApplicationContext
             path = path.substring(0, pos);
         }
 
-        path = normalize(path);
+        path = RequestUtil.normalize(path);
         if (path == null)
             return (null);
 
@@ -495,7 +496,7 @@ public class ApplicationContext
             throw new MalformedURLException(sm.getString("applicationContext.requestDispatcher.iae", path));
 
         
-        path = normalize(path);
+        path = RequestUtil.normalize(path);
         if (path == null)
             return (null);
 
@@ -544,13 +545,16 @@ public class ApplicationContext
      */
     public InputStream getResourceAsStream(String path) {
 
-        path = normalize(path);
         if (path == null)
             return (null);
 
         if (!path.startsWith("/") && Globals.STRICT_SERVLET_COMPLIANCE)
             return null;
-        
+
+        path = RequestUtil.normalize(path);
+        if (path == null)
+            return (null);
+
         DirContext resources = context.getResources();
         if (resources != null) {
             try {
@@ -583,7 +587,7 @@ public class ApplicationContext
                 (sm.getString("applicationContext.resourcePaths.iae", path));
         }
 
-        path = normalize(path);
+        path = RequestUtil.normalize(path);
         if (path == null)
             return (null);
 


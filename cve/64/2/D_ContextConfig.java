@@ -733,35 +733,40 @@ public class ContextConfig
         file = new File(docBase);
         String origDocBase = docBase;
         
-        String contextPath = context.getPath();
-        if (contextPath.equals("")) {
-            contextPath = "ROOT";
+        String pathName = context.getPath();
+        if (pathName.equals("")) {
+            pathName = "ROOT";
         } else {
-            if (contextPath.lastIndexOf('/') > 0) {
-                contextPath = "/" + contextPath.substring(1).replace('/','#');
-            }
+            // Context path must start with '/'
+            pathName = pathName.substring(1).replace('/', '#');
         }
         if (docBase.toLowerCase().endsWith(".war") && !file.isDirectory() && unpackWARs) {
             URL war = new URL("jar:" + (new File(docBase)).toURI().toURL() + "!/");
-            docBase = ExpandWar.expand(host, war, contextPath);
+            docBase = ExpandWar.expand(host, war, pathName);
             file = new File(docBase);
             docBase = file.getCanonicalPath();
             if (context instanceof StandardContext) {
                 ((StandardContext) context).setOriginalDocBase(origDocBase);
             }
+        } else if (docBase.toLowerCase().endsWith(".war") &&
+                !file.isDirectory() && !unpackWARs) {
+            URL war =
+                new URL("jar:" + (new File (docBase)).toURI().toURL() + "!/");
+            ExpandWar.validate(host, war, pathName);
         } else {
             File docDir = new File(docBase);
             if (!docDir.exists()) {
                 File warFile = new File(docBase + ".war");
                 if (warFile.exists()) {
+                    URL war =
+                        new URL("jar:" + warFile.toURI().toURL() + "!/");
                     if (unpackWARs) {
-                        URL war =
-                            new URL("jar:" + warFile.toURI().toURL() + "!/");
-                        docBase = ExpandWar.expand(host, war, contextPath);
+                        docBase = ExpandWar.expand(host, war, pathName);
                         file = new File(docBase);
                         docBase = file.getCanonicalPath();
                     } else {
                         docBase = warFile.getCanonicalPath();
+                        ExpandWar.validate(host, war, pathName);
                     }
                 }
                 if (context instanceof StandardContext) {
@@ -1122,7 +1127,8 @@ public class ContextConfig
             if (!docBaseFile.isAbsolute()) {
                 docBaseFile = new File(appBase, docBase);
             }
-            ExpandWar.delete(docBaseFile);
+            // No need to log failure - it is expected in this case
+            ExpandWar.delete(docBaseFile, false);
         }
         
         ok = true;

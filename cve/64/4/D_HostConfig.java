
 
@@ -715,13 +722,22 @@ public class HostConfig
             if (files[i].equalsIgnoreCase("WEB-INF"))
                 continue;
             File dir = new File(appBase, files[i]);
-            if (files[i].toLowerCase().endsWith(".war") && dir.isFile()) {
+            if (files[i].toLowerCase().endsWith(".war") && dir.isFile()
+                    && !invalidWars.contains(files[i]) ) {
                 
                 // Calculate the context path and make sure it is unique
                 String contextPath = "/" + files[i].replace('#','/');
                 int period = contextPath.lastIndexOf(".");
-                if (period >= 0)
-                    contextPath = contextPath.substring(0, period);
+                contextPath = contextPath.substring(0, period);
+                
+                // Check for WARs with /../ /./ or similar sequences in the name
+                if (!validateContextPath(appBase, contextPath)) {
+                    log.error(sm.getString(
+                            "hostConfig.illegalWarName", files[i]));
+                    invalidWars.add(files[i]);
+                    continue;
+                }
+
                 if (contextPath.equals("/ROOT"))
                     contextPath = "";
                 

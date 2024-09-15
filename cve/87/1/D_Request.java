@@ -1525,6 +1525,26 @@ public class Request
             return;
         }
 
+        // Do the security check before any updates are made
+        if (Globals.IS_SECURITY_ENABLED &&
+                name.equals("org.apache.tomcat.sendfile.filename")) {
+            // Use the canonical file name to avoid any possible symlink and
+            // relative path issues
+            String canonicalPath;
+            try {
+                canonicalPath = new File(value.toString()).getCanonicalPath();
+            } catch (IOException e) {
+                throw new SecurityException(sm.getString(
+                        "coyoteRequest.sendfileNotCanonical", value), e);
+            }
+            // Sendfile is performed in Tomcat's security context so need to
+            // check if the web app is permitted to access the file while still
+            // in the web app's security context
+            System.getSecurityManager().checkRead(canonicalPath);
+            // Update the value so the canonical path is used
+            value = canonicalPath;
+        }
+
         oldValue = attributes.put(name, value);
         if (oldValue != null) {
             replaced = true;

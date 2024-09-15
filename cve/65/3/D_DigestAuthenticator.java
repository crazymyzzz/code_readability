@@ -408,8 +408,7 @@ public class DigestAuthenticator
         // Get the realm name
         String realmName = config.getRealmName();
         if (realmName == null)
-            realmName = request.getServerName() + ":"
-                + request.getServerPort();
+            realmName = REALM_NAME;
 
         byte[] buffer = null;
         synchronized (md5Helper) {

@@ -165,9 +165,7 @@ public class BasicAuthenticator
         StringBuilder value = new StringBuilder(16);
         value.append("Basic realm=\"");
         if (config.getRealmName() == null) {
-            value.append(request.getServerName());
-            value.append(':');
-            value.append(Integer.toString(request.getServerPort()));
+            value.append(REALM_NAME);
         } else {
             value.append(config.getRealmName());
         }

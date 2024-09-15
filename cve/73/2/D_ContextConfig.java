@@ -366,11 +366,16 @@ public class ContextConfig
      */
     protected synchronized void authenticatorConfig() {
 
-        // Always need an authenticator to support @ServletSecurity annotations
         LoginConfig loginConfig = context.getLoginConfig();
         if (loginConfig == null) {
-            loginConfig = DUMMY_LOGIN_CONFIG;
-            context.setLoginConfig(loginConfig);
+            if (context.getIgnoreAnnotations())  {
+                return;
+            } else {
+                // Not metadata-complete, need an authenticator to support
+                // @ServletSecurity annotations
+                loginConfig = DUMMY_LOGIN_CONFIG;
+                context.setLoginConfig(loginConfig);
+            }
         }
 
         // Has an authenticator been configured already?

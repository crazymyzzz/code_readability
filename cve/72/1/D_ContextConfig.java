@@ -366,10 +366,7 @@ public class ContextConfig
      */
     protected synchronized void authenticatorConfig() {
 
-        // Does this Context require an Authenticator?
-        SecurityConstraint constraints[] = context.findConstraints();
-        if ((constraints == null) || (constraints.length == 0))
-            return;
+        // Always need an authenticator to support @ServletSecurity annotations
         LoginConfig loginConfig = context.getLoginConfig();
         if (loginConfig == null) {
             loginConfig = DUMMY_LOGIN_CONFIG;

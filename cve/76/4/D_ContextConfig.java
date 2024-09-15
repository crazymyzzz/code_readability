@@ -367,12 +367,17 @@ public class ContextConfig
     protected synchronized void authenticatorConfig() {
 
         LoginConfig loginConfig = context.getLoginConfig();
-        if (loginConfig == null) {
-            if (context.getIgnoreAnnotations())  {
-                return;
-            } else {
-                // Not metadata-complete, need an authenticator to support
-                // @ServletSecurity annotations
+
+        SecurityConstraint constraints[] = context.findConstraints();
+        if (context.getIgnoreAnnotations() &&
+                (constraints == null || constraints.length ==0) &&
+                !context.getPreemptiveAuthentication())  {
+            return;
+        } else {
+            if (loginConfig == null) {
+                // Not metadata-complete or security constraints present, need
+                // an authenticator to support @ServletSecurity annotations
+                // and/or constraints
                 loginConfig = DUMMY_LOGIN_CONFIG;
                 context.setLoginConfig(loginConfig);
             }


@@ -454,8 +456,7 @@ public abstract class AuthenticatorBase extends ValveBase
         SecurityConstraint [] constraints
             = realm.findSecurityConstraints(request, this.context);
        
-        if ((constraints == null) /* &&
-            (!Constants.FORM_METHOD.equals(config.getAuthMethod())) */ ) {
+        if (constraints == null && !context.getPreemptiveAuthentication()) {
             if (log.isDebugEnabled())
                 log.debug(" Not subject to any constraint");
             getNext().invoke(request, response);
@@ -464,7 +465,7 @@ public abstract class AuthenticatorBase extends ValveBase
 
         // Make sure that constrained resources are not cached by web proxies
         // or browsers as caching can provide a security hole
-        if (disableProxyCaching && 
+        if (constraints != null && disableProxyCaching && 
             // FIXME: Disabled for Mozilla FORM support over SSL 
             // (improper caching issue)
             //!request.isSecure() &&
@@ -482,36 +483,55 @@ public abstract class AuthenticatorBase extends ValveBase
         }
 
         int i;
-        // Enforce any user data constraint for this security constraint
-        if (log.isDebugEnabled()) {
-            log.debug(" Calling hasUserDataPermission()");
-        }
-        if (!realm.hasUserDataPermission(request, response,
-                                         constraints)) {
+        if (constraints != null) {
+            // Enforce any user data constraint for this security constraint
             if (log.isDebugEnabled()) {
-                log.debug(" Failed hasUserDataPermission() test");
+                log.debug(" Calling hasUserDataPermission()");
+            }
+            if (!realm.hasUserDataPermission(request, response,
+                                             constraints)) {
+                if (log.isDebugEnabled()) {
+                    log.debug(" Failed hasUserDataPermission() test");
+                }
+                /*
+                 * ASSERT: Authenticator already set the appropriate
+                 * HTTP status code, so we do not have to do anything special
+                 */
+                return;
             }
-            /*
-             * ASSERT: Authenticator already set the appropriate
-             * HTTP status code, so we do not have to do anything special
-             */
-            return;
         }
 
         // Since authenticate modifies the response on failure,
         // we have to check for allow-from-all first.
-        boolean authRequired = true;
-        for(i=0; i < constraints.length && authRequired; i++) {
-            if(!constraints[i].getAuthConstraint()) {
-                authRequired = false;
-            } else if(!constraints[i].getAllRoles()) {
-                String [] roles = constraints[i].findAuthRoles();
-                if(roles == null || roles.length == 0) {
+        boolean authRequired;
+        if (constraints == null) {
+            authRequired = false;
+        } else {
+            authRequired = true;
+            for(i=0; i < constraints.length && authRequired; i++) {
+                if(!constraints[i].getAuthConstraint()) {
                     authRequired = false;
+                } else if(!constraints[i].getAllRoles()) {
+                    String [] roles = constraints[i].findAuthRoles();
+                    if(roles == null || roles.length == 0) {
+                        authRequired = false;
+                    }
                 }
             }
         }
-             
+
+        if (!authRequired) {
+            authRequired =
+                request.getCoyoteRequest().getMimeHeaders().getValue(
+                        "authorization") != null;
+        }
+
+        if (!authRequired) {
+            X509Certificate[] certs = (X509Certificate[]) request.getAttribute(
+                    Globals.CERTIFICATES_ATTR);
+            authRequired = certs != null && certs.length > 0;
+        }
+
         if(authRequired) {  
             if (log.isDebugEnabled()) {
                 log.debug(" Calling authenticate()");
@@ -530,21 +550,23 @@ public abstract class AuthenticatorBase extends ValveBase
             
         }
     
-        if (log.isDebugEnabled()) {
-            log.debug(" Calling accessControl()");
-        }
-        if (!realm.hasResourcePermission(request, response,
-                                         constraints,
-                                         this.context)) {
+        if (constraints != null) {
             if (log.isDebugEnabled()) {
-                log.debug(" Failed accessControl() test");
+                log.debug(" Calling accessControl()");
+            }
+            if (!realm.hasResourcePermission(request, response,
+                                             constraints,
+                                             this.context)) {
+                if (log.isDebugEnabled()) {
+                    log.debug(" Failed accessControl() test");
+                }
+                /*
+                 * ASSERT: AccessControl method has already set the
+                 * appropriate HTTP status code, so we do not have to do
+                 * anything special
+                 */
+                return;
             }
-            /*
-             * ASSERT: AccessControl method has already set the
-             * appropriate HTTP status code, so we do not have to do
-             * anything special
-             */
-            return;
         }
     
         // Any and all specified constraints have been satisfied

@@ -37,6 +37,7 @@ import org.apache.catalina.Manager;
 import org.apache.catalina.Realm;
 import org.apache.catalina.Session;
 import org.apache.catalina.Valve;
+import org.apache.catalina.Wrapper;
 import org.apache.catalina.connector.Request;
 import org.apache.catalina.connector.Response;
 import org.apache.catalina.deploy.LoginConfig;
@@ -478,6 +479,13 @@ public abstract class AuthenticatorBase extends ValveBase
             }
         }
 
+        // The Servlet may specify security constraints through annotations.
+        // Ensure that they have been processed before constraints are checked
+        Wrapper wrapper = (Wrapper) request.getMappingData().wrapper; 
+        if (wrapper.getServlet() != null) {
+            wrapper.load();
+        }
+
         Realm realm = this.context.getRealm();
         // Is this request URI subject to a security constraint?
         SecurityConstraint [] constraints

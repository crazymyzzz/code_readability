@@ -42,6 +42,7 @@ import org.apache.catalina.Realm;
 import org.apache.catalina.Server;
 import org.apache.catalina.Service;
 import org.apache.catalina.Wrapper;
+import org.apache.catalina.authenticator.NonLoginAuthenticator;
 import org.apache.catalina.connector.Connector;
 import org.apache.catalina.core.NamingContextListener;
 import org.apache.catalina.core.StandardContext;
@@ -50,6 +51,7 @@ import org.apache.catalina.core.StandardHost;
 import org.apache.catalina.core.StandardServer;
 import org.apache.catalina.core.StandardService;
 import org.apache.catalina.core.StandardWrapper;
+import org.apache.catalina.deploy.LoginConfig;
 import org.apache.catalina.realm.GenericPrincipal;
 import org.apache.catalina.realm.RealmBase;
 import org.apache.catalina.session.StandardManager;
@@ -698,6 +700,13 @@ public class Tomcat {
                 if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
                     context.setConfigured(true);
                 }
+                // LoginConfig is required to process @ServletSecurity
+                // annotations
+                if (context.getLoginConfig() == null) {
+                    context.setLoginConfig(
+                            new LoginConfig("NONE", null, null, null));
+                    context.getPipeline().addValve(new NonLoginAuthenticator());
+                }
             } catch (ClassCastException e) {
                 return;
             }

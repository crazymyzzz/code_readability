
@@ -1075,10 +1077,20 @@ public class StandardWrapper extends ContainerBase
                 }
             }
 
+            ServletSecurity secAnnotation =
+                servlet.getClass().getAnnotation(ServletSecurity.class);
+            Context ctxt = (Context) getParent();
+            if (secAnnotation != null) {
+                ctxt.addServletSecurity(
+                        new ApplicationServletRegistration(this, ctxt),
+                        new ServletSecurityElement(secAnnotation));
+            }
+            
+
             // Special handling for ContainerServlet instances
             if ((servlet instanceof ContainerServlet) &&
                   (isContainerProvidedServlet(servletClass) ||
-                    ((Context)getParent()).getPrivileged() )) {
+                    ctxt.getPrivileged() )) {
                 ((ContainerServlet) servlet).setWrapper(this);
             }
 

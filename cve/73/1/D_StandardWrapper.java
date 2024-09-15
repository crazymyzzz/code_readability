@@ -1145,9 +1145,14 @@ public class StandardWrapper extends ContainerBase
         // Calling this twice isn't harmful so no syncs
         servletSecurityAnnotationScanRequired = false;
 
+        Context ctxt = (Context) getParent();
+        
+        if (ctxt.getIgnoreAnnotations()) {
+            return;
+        }
+
         ServletSecurity secAnnotation =
             servlet.getClass().getAnnotation(ServletSecurity.class);
-        Context ctxt = (Context) getParent();
         if (secAnnotation != null) {
             ctxt.addServletSecurity(
                     new ApplicationServletRegistration(this, ctxt),

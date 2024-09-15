@@ -482,7 +482,7 @@ public abstract class AuthenticatorBase extends ValveBase
         // The Servlet may specify security constraints through annotations.
         // Ensure that they have been processed before constraints are checked
         Wrapper wrapper = (Wrapper) request.getMappingData().wrapper; 
-        if (wrapper.getServlet() != null) {
+        if (wrapper.getServlet() == null) {
             wrapper.load();
         }
 

@@ -482,8 +482,13 @@ public class CoyoteAdapter implements Adapter {
                 (connector.getURIEncoding());
         }
         
-        connector.getService().getContainer().logAccess(
-                request, response, time, true);
+        try {
+            connector.getService().getContainer().logAccess(
+                    request, response, time, true);
+        } catch (Throwable t) {
+            ExceptionUtils.handleThrowable(t);
+            log.warn(sm.getString("coyoteAdapter.accesslogFail"), t);
+        }
         
         if (create) {
             request.recycle();

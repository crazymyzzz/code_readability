@@ -126,12 +126,14 @@ public class AjpNioProcessor extends AbstractAjpProcessor<NioChannel> {
                     recycle(false);
                     continue;
                 } else if(type != Constants.JK_AJP13_FORWARD_REQUEST) {
-                    // Usually the servlet didn't read the previous request body
-                    if(log.isDebugEnabled()) {
-                        log.debug("Unexpected message: "+type);
+                    // Unexpected packet type. Unread body packets should have
+                    // been swallowed in finish().
+                    if (log.isDebugEnabled()) {
+                        log.debug("Unexpected message: " + type);
                     }
+                    error = true;
                     recycle(true);
-                    continue;
+                    break;
                 }
                 request.setStartTime(System.currentTimeMillis());
             } catch (IOException e) {

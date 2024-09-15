@@ -140,11 +140,13 @@ public class AjpAprProcessor extends AbstractAjpProcessor<Long> {
                     }
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
-                    continue;
+                    error = true;
+                    break;
                 }
 
                 keptAlive = true;

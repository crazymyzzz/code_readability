@@ -1460,7 +1460,9 @@ public class AprEndpoint extends AbstractEndpoint {
                                                data.pos, data.end - data.pos, 0);
                     if (nw < 0) {
                         if (!(-nw == Status.EAGAIN)) {
-                            destroySocket(data.socket);
+                            Pool.destroy(data.fdpool);
+                            // No need to close socket, this will be done by
+                            // calling code since data.socket == 0
                             data.socket = 0;
                             return false;
                         } else {

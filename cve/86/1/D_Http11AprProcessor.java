@@ -305,7 +305,18 @@ public class Http11AprProcessor extends AbstractHttp11Processor<Long> {
                 sendfileData.socket = socketRef;
                 sendfileData.keepAlive = keepAlive;
                 if (!((AprEndpoint)endpoint).getSendfile().add(sendfileData)) {
-                    openSocket = true;
+                    if (sendfileData.socket == 0) {
+                        // Didn't send all the data but the socket is no longer
+                        // set. Something went wrong. Close the connection.
+                        // Too late to set status code.
+                        if (log.isDebugEnabled()) {
+                            log.debug(sm.getString(
+                                    "http11processor.sendfile.error"));
+                        }
+                        error = true;
+                    } else {
+                        openSocket = true;
+                    }
                     break;
                 }
             }

@@ -1271,6 +1271,7 @@ public class NioEndpoint extends AbstractEndpoint {
                             log.debug("Send file connection is being closed");
                         }
                         cancelledKey(sk,SocketStatus.STOP,false);
+                        return false;
                     }
                 } else if ( attachment.interestOps() == 0 && reg ) {
                     if (log.isDebugEnabled()) {

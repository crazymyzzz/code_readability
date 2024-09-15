@@ -197,6 +197,7 @@ public class AjpAprProtocol extends AbstractAjpProtocol {
                     connections.put(socket, processor);
                     socket.setAsync(true);
                 } else {
+                    processor.recycle();
                     recycledProcessors.offer(processor);
                 }
                 return state;
@@ -220,6 +221,7 @@ public class AjpAprProtocol extends AbstractAjpProtocol {
                 // less-than-verbose logs.
                 log.error(sm.getString("ajpprotocol.proto.error"), e);
             }
+            processor.recycle();
             recycledProcessors.offer(processor);
             return SocketState.CLOSED;
         }
@@ -251,6 +253,7 @@ public class AjpAprProtocol extends AbstractAjpProtocol {
                     }
                     if (state != SocketState.LONG && state != SocketState.ASYNC_END) {
                         connections.remove(socket);
+                        processor.recycle();
                         recycledProcessors.offer(processor);
                         if (state == SocketState.OPEN) {
                             ((AprEndpoint)proto.endpoint).getPoller().add(socket.getSocket().longValue());

@@ -296,6 +296,7 @@ public class Http11AprProtocol extends AbstractHttp11Protocol {
                         if (state != SocketState.LONG) {
                             connections.remove(socket.getSocket());
                             socket.setAsync(false);
+                            processor.recycle();
                             recycledProcessors.offer(processor);
                             if (state == SocketState.OPEN) {
                                 ((AprEndpoint)proto.endpoint).getPoller().add(socket.getSocket().longValue());
@@ -337,6 +338,7 @@ public class Http11AprProtocol extends AbstractHttp11Protocol {
                                 socket.getSocket().longValue());
                     }
                 } else {
+                    processor.recycle();
                     recycledProcessors.offer(processor);
                 }
                 return state;
@@ -361,6 +363,7 @@ public class Http11AprProtocol extends AbstractHttp11Protocol {
                 Http11AprProtocol.log.error(
                         sm.getString("http11protocol.proto.error"), e);
             }
+            processor.recycle();
             recycledProcessors.offer(processor);
             return SocketState.CLOSED;
         }
@@ -391,6 +394,7 @@ public class Http11AprProtocol extends AbstractHttp11Protocol {
                     if (state != SocketState.LONG && state != SocketState.ASYNC_END) {
                         connections.remove(socket.getSocket());
                         socket.setAsync(false);
+                        processor.recycle();
                         recycledProcessors.offer(processor);
                         if (state == SocketState.OPEN) {
                             ((AprEndpoint)proto.endpoint).getPoller().add(socket.getSocket().longValue());

@@ -194,6 +194,7 @@ public class Http11Protocol extends AbstractHttp11JsseProtocol {
                     return processor.asyncPostProcess();
                 } else {
                     socket.setAsync(false);
+                    processor.recycle();
                     recycledProcessors.offer(processor);
                 }
                 return state;
@@ -216,6 +217,7 @@ public class Http11Protocol extends AbstractHttp11JsseProtocol {
                 // less-than-verbose logs.
                 log.error(sm.getString("http11protocol.proto.error"), e);
             }
+            processor.recycle();
             recycledProcessors.offer(processor);
             return SocketState.CLOSED;
         }

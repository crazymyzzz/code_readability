@@ -189,6 +189,7 @@ public class AjpProtocol extends AbstractAjpProtocol {
                     return processor.asyncPostProcess();
                 } else {
                     socket.setAsync(false);
+                    processor.recycle();
                     recycledProcessors.offer(processor);
                 }
                 return state;
@@ -211,6 +212,7 @@ public class AjpProtocol extends AbstractAjpProtocol {
                 // less-than-verbose logs.
                 log.error(sm.getString("ajpprotocol.proto.error"), e);
             }
+            processor.recycle();
             recycledProcessors.offer(processor);
             return SocketState.CLOSED;
         }

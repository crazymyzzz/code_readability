@@ -356,12 +356,10 @@ public class Http11Processor extends AbstractHttp11Processor {
         rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
 
         if (error) {
-            recycle();
             return SocketState.CLOSED;
         } else if (isAsync()) {
             return SocketState.LONG;
         } else {
-            recycle();
             if (!keepAlive) {
                 return SocketState.CLOSED;
             } else {

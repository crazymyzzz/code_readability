@@ -187,12 +187,10 @@ public class Http11AprProcessor extends AbstractHttp11Processor {
         if (error) {
             inputBuffer.nextRequest();
             outputBuffer.nextRequest();
-            recycle();
             return SocketState.CLOSED;
         } else if (!comet) {
             inputBuffer.nextRequest();
             outputBuffer.nextRequest();
-            recycle();
             return SocketState.OPEN;
         } else {
             return SocketState.LONG;
@@ -367,12 +365,10 @@ public class Http11AprProcessor extends AbstractHttp11Processor {
         rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
 
         if (error || endpoint.isPaused()) {
-            recycle();
             return SocketState.CLOSED;
         } else if (comet  || isAsync()) {
             return SocketState.LONG;
         } else {
-            recycle();
             return (openSocket) ? SocketState.OPEN : SocketState.CLOSED;
         }
         
@@ -406,12 +402,10 @@ public class Http11AprProcessor extends AbstractHttp11Processor {
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

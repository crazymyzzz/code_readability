@@ -206,10 +206,8 @@ public class Http11NioProcessor extends AbstractHttp11Processor {
         rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
 
         if (error) {
-            recycle();
             return SocketState.CLOSED;
         } else if (!comet) {
-            recycle();
             return (keepAlive)?SocketState.OPEN:SocketState.CLOSED;
         } else {
             return SocketState.LONG;
@@ -267,10 +265,8 @@ public class Http11NioProcessor extends AbstractHttp11Processor {
         rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
 
         if (error) {
-            recycle();
             return SocketState.CLOSED;
         } else if (!comet && !isAsync()) {
-            recycle();
             return (keepAlive)?SocketState.OPEN:SocketState.CLOSED;
         } else {
             return SocketState.LONG;
@@ -305,7 +301,7 @@ public class Http11NioProcessor extends AbstractHttp11Processor {
 
         boolean keptAlive = false;
         boolean openSocket = false;
-        boolean recycle = true;
+        boolean readComplete = true;
         final KeyAttachment ka = (KeyAttachment)socket.getAttachment(false);
         
         while (!error && keepAlive && !comet && !isAsync() && !endpoint.isPaused()) {
@@ -328,7 +324,7 @@ public class Http11NioProcessor extends AbstractHttp11Processor {
                     } else {
                         // Started to read request line. Need to keep processor
                         // associated with socket
-                        recycle = false;
+                        readComplete = false;
                     }
                     if (endpoint.isPaused()) {
                         // 503 - Service unavailable
@@ -345,7 +341,7 @@ public class Http11NioProcessor extends AbstractHttp11Processor {
                         //we've read part of the request, don't recycle it
                         //instead associate it with the socket
                         openSocket = true;
-                        recycle = false;
+                        readComplete = false;
                         break;
                     }
                     request.setStartTime(System.currentTimeMillis());
@@ -471,16 +467,11 @@ public class Http11NioProcessor extends AbstractHttp11Processor {
 
         rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
         if (error || endpoint.isPaused()) {
-            recycle();
             return SocketState.CLOSED;
         } else if (comet || isAsync()) {
             return SocketState.LONG;
         } else {
-            if (recycle) {
-                recycle();
-            }
-            //return (openSocket) ? (SocketState.OPEN) : SocketState.CLOSED;
-            return (openSocket) ? (recycle?SocketState.OPEN:SocketState.LONG) : SocketState.CLOSED;
+            return (openSocket) ? (readComplete?SocketState.OPEN:SocketState.LONG) : SocketState.CLOSED;
         }
 
     }

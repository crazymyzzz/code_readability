@@ -316,7 +316,6 @@ public class AjpAprProcessor extends AbstractAjpProcessor {
 
             rp.setStage(org.apache.coyote.Constants.STAGE_KEEPALIVE);
             recycle();
-
         }
 
         // Add the socket to the poller
@@ -329,12 +328,10 @@ public class AjpAprProcessor extends AbstractAjpProcessor {
         rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
         
         if (error || endpoint.isPaused()) {
-            recycle();
             return SocketState.CLOSED;
         } else if (isAsync()) {
             return SocketState.LONG;
         } else {
-            recycle();
             return SocketState.OPEN;
         }
     }
@@ -369,14 +366,12 @@ public class AjpAprProcessor extends AbstractAjpProcessor {
         if (isAsync()) {
             if (error) {
                 request.updateCounters();
-                recycle();
                 return SocketState.CLOSED;
             } else {
                 return SocketState.LONG;
             }
         } else {
             request.updateCounters();
-            recycle();
             if (error) {
                 return SocketState.CLOSED;
             } else {

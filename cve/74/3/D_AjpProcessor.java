@@ -334,7 +334,6 @@ public class AjpProcessor extends AbstractAjpProcessor {
 
             rp.setStage(org.apache.coyote.Constants.STAGE_KEEPALIVE);
             recycle();
-
         }
         
         rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
@@ -342,7 +341,6 @@ public class AjpProcessor extends AbstractAjpProcessor {
         if (isAsync() && !error && !endpoint.isPaused()) {
             return SocketState.LONG;
         } else {
-            recycle();
             input = null;
             output = null;
             return SocketState.CLOSED;
@@ -373,7 +371,6 @@ public class AjpProcessor extends AbstractAjpProcessor {
             if (error) {
                 response.setStatus(500);
                 request.updateCounters();
-                recycle();
                 input = null;
                 output = null;
                 return SocketState.CLOSED;
@@ -385,7 +382,6 @@ public class AjpProcessor extends AbstractAjpProcessor {
                 response.setStatus(500);
             }
             request.updateCounters();
-            recycle();
             input = null;
             output = null;
             return SocketState.CLOSED;

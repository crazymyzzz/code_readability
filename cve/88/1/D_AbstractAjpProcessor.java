@@ -985,6 +985,11 @@ public abstract class AbstractAjpProcessor<S> extends AbstractProcessor<S> {
 
         finished = true;
 
+        // Swallow the unread body packet if present
+        if (first && request.getContentLengthLong() > 0) {
+            receive();
+        }
+
         // Add the end message
         if (error) {
             output(endAndCloseMessageArray, 0, endAndCloseMessageArray.length);

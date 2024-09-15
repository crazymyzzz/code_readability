@@ -102,10 +102,12 @@ public class BufferedInputFilter implements InputFilter {
     }
 
     public void recycle() {
-        if (buffered.getBuffer().length > 65536) {
-            buffered = null;
-        } else {
-            buffered.recycle();
+        if (buffered != null) {
+            if (buffered.getBuffer().length > 65536) {
+                buffered = null;
+            } else {
+                buffered.recycle();
+            }
         }
         tempRead.recycle();
         hasRead = false;

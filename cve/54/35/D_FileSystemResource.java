@@ -92,11 +92,7 @@ public class FileSystemResource extends LoadableResource {
 
     @Override
     public boolean shouldStream() {
-        //noinspection RedundantIfStatement
-        if (stream && file.length() > STREAM_MINIMUM_THRESHOLD) {
-            return true;
-        }
-        return false;
+        return stream && file.length() > STREAM_MINIMUM_THRESHOLD;
     }
 
     /**

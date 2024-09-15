
@@ -290,9 +289,14 @@ public class MultipartStream {
 
         // We prepend CR/LF to the boundary to chop trailing CR/LF from
         // body-data tokens.
-        this.boundary = new byte[boundary.length + BOUNDARY_PREFIX.length];
         this.boundaryLength = boundary.length + BOUNDARY_PREFIX.length;
+        if (bufSize < this.boundaryLength + 1) {
+            throw new IllegalArgumentException(
+                    "The buffer size specified for the MultipartStream is too small");
+        }
+        this.boundary = new byte[this.boundaryLength];
         this.keepRegion = this.boundary.length;
+
         System.arraycopy(BOUNDARY_PREFIX, 0, this.boundary, 0,
                 BOUNDARY_PREFIX.length);
         System.arraycopy(boundary, 0, this.boundary, BOUNDARY_PREFIX.length,


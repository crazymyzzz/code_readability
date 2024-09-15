@@ -39,14 +39,13 @@ public class GzipCompressionCodec extends AbstractCompressionCodec implements Co
         }
     };
 
-    @Override
-    public String getAlgorithmName() {
-        return GZIP;
+    public GzipCompressionCodec() {
+        super(GZIP);
     }
 
     @Override
-    protected byte[] doCompress(byte[] payload) throws IOException {
-        return writeAndClose(payload, WRAPPER);
+    protected byte[] doCompress(byte[] content) throws IOException {
+        return writeAndClose(content, WRAPPER);
     }
 
     @Override

@@ -42,14 +42,13 @@ public class DeflateCompressionCodec extends AbstractCompressionCodec {
         }
     };
 
-    @Override
-    public String getAlgorithmName() {
-        return DEFLATE;
+    public DeflateCompressionCodec() {
+        super(DEFLATE);
     }
 
     @Override
-    protected byte[] doCompress(byte[] payload) throws IOException {
-        return writeAndClose(payload, WRAPPER);
+    protected byte[] doCompress(byte[] content) throws IOException {
+        return writeAndClose(content, WRAPPER);
     }
 
     @Override

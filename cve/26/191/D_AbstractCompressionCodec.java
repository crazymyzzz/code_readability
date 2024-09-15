@@ -19,6 +19,7 @@ import io.jsonwebtoken.CompressionCodec;
 import io.jsonwebtoken.CompressionException;
 import io.jsonwebtoken.lang.Assert;
 import io.jsonwebtoken.lang.Objects;
+import io.jsonwebtoken.lang.Strings;
 
 import java.io.ByteArrayOutputStream;
 import java.io.IOException;
@@ -32,6 +33,22 @@ import java.io.OutputStream;
  */
 public abstract class AbstractCompressionCodec implements CompressionCodec {
 
+    private final String id;
+
+    protected AbstractCompressionCodec(String id) {
+        this.id = Assert.hasText(Strings.clean(id), "id argument cannot be null or empty.");
+    }
+
+    @Override
+    public String getId() {
+        return this.id;
+    }
+
+    @Override
+    public String getAlgorithmName() {
+        return getId();
+    }
+
     //package-protected for a point release.  This can be made protected on a minor release (0.11.0, 0.12.0, 1.0, etc).
     //TODO: make protected on a minor release
     interface StreamWrapper {
@@ -58,11 +75,11 @@ public abstract class AbstractCompressionCodec implements CompressionCodec {
 
     //package-protected for a point release.  This can be made protected on a minor release (0.11.0, 0.12.0, 1.0, etc).
     //TODO: make protected on a minor release
-    byte[] writeAndClose(byte[] payload, StreamWrapper wrapper) throws IOException {
+    byte[] writeAndClose(byte[] content, StreamWrapper wrapper) throws IOException {
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
         OutputStream compressionStream = wrapper.wrap(outputStream);
         try {
-            compressionStream.write(payload);
+            compressionStream.write(content);
             compressionStream.flush();
         } finally {
             Objects.nullSafeClose(compressionStream);
@@ -71,29 +88,29 @@ public abstract class AbstractCompressionCodec implements CompressionCodec {
     }
 
     /**
-     * Implement this method to do the actual work of compressing the payload
+     * Implement this method to do the actual work of compressing the content
      *
-     * @param payload the bytes to compress
+     * @param content the bytes to compress
      * @return the compressed bytes
      * @throws IOException if the compression causes an IOException
      */
-    protected abstract byte[] doCompress(byte[] payload) throws IOException;
+    protected abstract byte[] doCompress(byte[] content) throws IOException;
 
     /**
-     * Asserts that payload is not null and calls {@link #doCompress(byte[]) doCompress}
+     * Asserts that content is not null and calls {@link #doCompress(byte[]) doCompress}
      *
-     * @param payload bytes to compress
+     * @param content bytes to compress
      * @return compressed bytes
      * @throws CompressionException if {@link #doCompress(byte[]) doCompress} throws an IOException
      */
     @Override
-    public final byte[] compress(byte[] payload) {
-        Assert.notNull(payload, "payload cannot be null.");
+    public final byte[] compress(byte[] content) {
+        Assert.notNull(content, "content cannot be null.");
 
         try {
-            return doCompress(payload);
+            return doCompress(content);
         } catch (IOException e) {
-            throw new CompressionException("Unable to compress payload.", e);
+            throw new CompressionException("Unable to compress content.", e);
         }
     }
 

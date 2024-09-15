@@ -18,38 +18,47 @@ package io.jsonwebtoken;
 /**
  * Compresses and decompresses byte arrays according to a compression algorithm.
  *
+ * <p><b>&quot;zip&quot; identifier</b></p>
+ *
+ * <p>{@code CompressionCodec} extends {@code Identifiable}; the value returned from
+ * {@link Identifiable#getId() getId()} will be used as the JWT
+ * <a href="https://tools.ietf.org/html/rfc7516#section-4.1.3"><code>zip</code></a> header value.</p>
+ *
  * @see CompressionCodecs#DEFLATE
  * @see CompressionCodecs#GZIP
  * @since 0.6.0
  */
-public interface CompressionCodec {
+public interface CompressionCodec extends Identifiable {
 
     /**
-     * The compression algorithm name to use as the JWT's {@code zip} header value.
+     * The algorithm name to use as the JWT
+     * <a href="https://tools.ietf.org/html/rfc7516#section-4.1.3"><code>zip</code></a> header value.
      *
-     * @return the compression algorithm name to use as the JWT's {@code zip} header value.
+     * @return the algorithm name to use as the JWT
+     * <a href="https://tools.ietf.org/html/rfc7516#section-4.1.3"><code>zip</code></a> header value.
+     * @deprecated since JJWT_RELEASE_VERSION in favor of {@link Identifiable#getId()} to ensure congruence with
+     * all other identifiable algorithms.
      */
+    @SuppressWarnings("DeprecatedIsStillUsed")
+    @Deprecated
     String getAlgorithmName();
 
     /**
-     * Compresses the specified byte array according to the compression {@link #getAlgorithmName() algorithm}.
+     * Compresses the specified byte array, returning the compressed byte array result.
      *
-     * @param payload bytes to compress
+     * @param content bytes to compress
      * @return compressed bytes
-     * @throws CompressionException if the specified byte array cannot be compressed according to the compression
-     *                              {@link #getAlgorithmName() algorithm}.
+     * @throws CompressionException if the specified byte array cannot be compressed.
      */
-    byte[] compress(byte[] payload) throws CompressionException;
+    byte[] compress(byte[] content) throws CompressionException;
 
     /**
-     * Decompresses the specified compressed byte array according to the compression
-     * {@link #getAlgorithmName() algorithm}.  The specified byte array must already be in compressed form
-     * according to the {@link #getAlgorithmName() algorithm}.
+     * Decompresses the specified compressed byte array, returning the decompressed byte array result.  The
+     * specified byte array must already be in compressed form.
      *
      * @param compressed compressed bytes
      * @return decompressed bytes
-     * @throws CompressionException if the specified byte array cannot be decompressed according to the compression
-     *                              {@link #getAlgorithmName() algorithm}.
+     * @throws CompressionException if the specified byte array cannot be decompressed.
      */
     byte[] decompress(byte[] compressed) throws CompressionException;
 }
\ No newline at end of file

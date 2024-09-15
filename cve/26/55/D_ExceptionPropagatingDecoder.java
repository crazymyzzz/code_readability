@@ -18,17 +18,33 @@ package io.jsonwebtoken.io;
 import io.jsonwebtoken.lang.Assert;
 
 /**
+ * Decoder that ensures any exceptions thrown that are <em>not</em> {@link DecodingException}s are wrapped
+ * and re-thrown as a {@code DecodingException}.
+ *
  * @since 0.10.0
  */
 class ExceptionPropagatingDecoder<T, R> implements Decoder<T, R> {
 
     private final Decoder<T, R> decoder;
 
+    /**
+     * Creates a new instance, wrapping the specified {@code decoder} to invoke during {@link #decode(Object)}.
+     *
+     * @param decoder the decoder to wrap and call during {@link #decode(Object)}
+     */
     ExceptionPropagatingDecoder(Decoder<T, R> decoder) {
         Assert.notNull(decoder, "Decoder cannot be null.");
         this.decoder = decoder;
     }
 
+    /**
+     * Decode the specified encoded data, delegating to the wrapped Decoder, wrapping any
+     * non-{@link DecodingException} as a {@code DecodingException}.
+     *
+     * @param t the encoded data
+     * @return the decoded data
+     * @throws DecodingException if there is an unexpected problem during decoding.
+     */
     @Override
     public R decode(T t) throws DecodingException {
         Assert.notNull(t, "Decode argument cannot be null.");

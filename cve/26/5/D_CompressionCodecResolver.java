@@ -30,7 +30,11 @@ package io.jsonwebtoken;
  * {@link io.jsonwebtoken.JwtParser#setCompressionCodecResolver(CompressionCodecResolver) parsing} JWTs.</p>
  *
  * @since 0.6.0
+ * @deprecated in favor of {@link Locator}
+ * @see JwtParserBuilder#setCompressionCodecLocator(Locator)
  */
+@SuppressWarnings("DeprecatedIsStillUsed")
+@Deprecated
 public interface CompressionCodecResolver {
 
     /**
@@ -41,6 +45,6 @@ public interface CompressionCodecResolver {
      * @return CompressionCodec matching the {@code zip} header, or null if there is no {@code zip} header.
      * @throws CompressionException if a {@code zip} header value is found and not supported.
      */
-    CompressionCodec resolveCompressionCodec(Header header) throws CompressionException;
+    CompressionCodec resolveCompressionCodec(Header<?> header) throws CompressionException;
 
 }

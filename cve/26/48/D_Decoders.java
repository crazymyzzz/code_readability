@@ -16,11 +16,24 @@
 package io.jsonwebtoken.io;
 
 /**
+ * Constant definitions for various decoding algorithms.
+ *
+ * @see #BASE64
+ * @see #BASE64URL
  * @since 0.10.0
  */
 public final class Decoders {
 
+    /**
+     * Very fast <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-4">Base64</a> decoder guaranteed to
+     * work in all &gt;= Java 7 JDK and Android environments.
+     */
     public static final Decoder<String, byte[]> BASE64 = new ExceptionPropagatingDecoder<>(new Base64Decoder());
+
+    /**
+     * Very fast <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-5">Base64Url</a> decoder guaranteed to
+     * work in all &gt;= Java 7 JDK and Android environments.
+     */
     public static final Decoder<String, byte[]> BASE64URL = new ExceptionPropagatingDecoder<>(new Base64UrlDecoder());
 
     private Decoders() { //prevent instantiation

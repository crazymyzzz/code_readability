@@ -16,11 +16,24 @@
 package io.jsonwebtoken.io;
 
 /**
+ * Constant definitions for various encoding algorithms.
+ *
+ * @see #BASE64
+ * @see #BASE64URL
  * @since 0.10.0
  */
 public final class Encoders {
 
+    /**
+     * Very fast <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-4">Base64</a> encoder guaranteed to
+     * work in all &gt;= Java 7 JDK and Android environments.
+     */
     public static final Encoder<byte[], String> BASE64 = new ExceptionPropagatingEncoder<>(new Base64Encoder());
+
+    /**
+     * Very fast <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-5">Base64Url</a> encoder guaranteed to
+     * work in all &gt;= Java 7 JDK and Android environments.
+     */
     public static final Encoder<byte[], String> BASE64URL = new ExceptionPropagatingEncoder<>(new Base64UrlEncoder());
 
     private Encoders() { //prevent instantiation

@@ -18,6 +18,9 @@ package io.jsonwebtoken.io;
 import io.jsonwebtoken.lang.Assert;
 
 /**
+ * Very fast <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-4">Base64</a> decoder guaranteed to
+ * work in all &gt;= Java 7 JDK and Android environments.
+ *
  * @since 0.10.0
  */
 class Base64Decoder extends Base64Support implements Decoder<String, byte[]> {

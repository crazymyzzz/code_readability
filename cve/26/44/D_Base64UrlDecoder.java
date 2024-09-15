@@ -16,6 +16,9 @@
 package io.jsonwebtoken.io;
 
 /**
+ * Very fast <a href="https://datatracker.ietf.org/doc/html/rfc4648#section-5">Base64Url</a> decoder guaranteed to
+ * work in all &gt;= Java 7 JDK and Android environments.
+ *
  * @since 0.10.0
  */
 class Base64UrlDecoder extends Base64Decoder {

@@ -19,10 +19,14 @@ package io.jsonwebtoken;
  * An expanded (not compact/serialized) Signed JSON Web Token.
  *
  * @param <B> the type of the JWS body contents, either a String or a {@link Claims} instance.
- *
  * @since 0.1
  */
-public interface Jws<B> extends Jwt<JwsHeader,B> {
+public interface Jws<B> extends Jwt<JwsHeader, B> {
 
-    String getSignature();
+    /**
+     * Returns the verified JWS signature as a Base64Url string.
+     *
+     * @return the verified JWS signature as a Base64Url string.
+     */
+    String getSignature(); //TODO for 1.0: return a byte[]
 }

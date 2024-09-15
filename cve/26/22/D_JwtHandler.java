@@ -17,7 +17,7 @@ package io.jsonwebtoken;
 
 /**
  * A JwtHandler is invoked by a {@link io.jsonwebtoken.JwtParser JwtParser} after parsing a JWT to indicate the exact
- * type of JWT or JWS parsed.
+ * type of JWT, JWS or JWE parsed.
  *
  * @param <T> the type of object to return to the parser caller after handling the parsed JWT.
  * @since 0.2
@@ -26,37 +26,42 @@ public interface JwtHandler<T> {
 
     /**
      * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
-     * a plaintext JWT.  A plaintext JWT has a String (non-JSON) body payload and it is not cryptographically signed.
+     * an Unprotected content JWT.  An Unprotected content JWT has a byte array payload that is not
+     * cryptographically signed or encrypted.  If the JWT creator set the (optional)
+     * {@link Header#getContentType() contentType} header value, the application may inspect that value to determine
+     * how to convert the byte array to the final content type as desired.
      *
-     * @param jwt the parsed plaintext JWT
+     * @param jwt the parsed Unprotected content JWT
      * @return any object to be used after inspecting the JWT, or {@code null} if no return value is necessary.
      */
-    T onPlaintextJwt(Jwt<Header, String> jwt);
+    T onContentJwt(Jwt<UnprotectedHeader, byte[]> jwt);
 
     /**
      * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
-     * a Claims JWT.  A Claims JWT has a {@link Claims} body and it is not cryptographically signed.
+     * a Claims JWT.  A Claims JWT has a {@link Claims} payload that is not cryptographically signed or encrypted.
      *
      * @param jwt the parsed claims JWT
      * @return any object to be used after inspecting the JWT, or {@code null} if no return value is necessary.
      */
-    T onClaimsJwt(Jwt<Header, Claims> jwt);
+    T onClaimsJwt(Jwt<UnprotectedHeader, Claims> jwt);
 
     /**
      * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
-     * a plaintext JWS.  A plaintext JWS is a JWT with a String (non-JSON) body (payload) that has been
-     * cryptographically signed.
+     * a content JWS.  A content JWS is a JWT with a byte array payload that has been cryptographically signed.
+     * If the JWT creator set the (optional) {@link Header#getContentType() contentType} header value, the
+     * application may inspect that value to determine how to convert the byte array to the final content type
+     * as desired.
      *
      * <p>This method will only be invoked if the cryptographic signature can be successfully verified.</p>
      *
-     * @param jws the parsed plaintext JWS
+     * @param jws the parsed content JWS
      * @return any object to be used after inspecting the JWS, or {@code null} if no return value is necessary.
      */
-    T onPlaintextJws(Jws<String> jws);
+    T onContentJws(Jws<byte[]> jws);
 
     /**
      * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
-     * a valid Claims JWS.  A Claims JWS is a JWT with a {@link Claims} body that has been cryptographically signed.
+     * a valid Claims JWS.  A Claims JWS is a JWT with a {@link Claims} payload that has been cryptographically signed.
      *
      * <p>This method will only be invoked if the cryptographic signature can be successfully verified.</p>
      *
@@ -65,4 +70,30 @@ public interface JwtHandler<T> {
      */
     T onClaimsJws(Jws<Claims> jws);
 
+    /**
+     * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
+     * a content JWE.  A content JWE is a JWE with a byte array payload that has been encrypted. If the JWT creator set
+     * the (optional) {@link Header#getContentType() contentType} header value, the application may inspect that
+     * value to determine how to convert the byte array to the final content type as desired.
+     *
+     * <p>This method will only be invoked if the content JWE can be successfully decrypted.</p>
+     *
+     * @param jwe the parsed content jwe
+     * @return any object to be used after inspecting the JWE, or {@code null} if no return value is necessary.
+     * @since JJWT_RELEASE_VERSION
+     */
+    T onContentJwe(Jwe<byte[]> jwe);
+
+    /**
+     * This method is invoked when a {@link io.jsonwebtoken.JwtParser JwtParser} determines that the parsed JWT is
+     * a valid Claims JWE.  A Claims JWE is a JWT with a {@link Claims} payload that has been encrypted.
+     *
+     * <p>This method will only be invoked if the Claims JWE can be successfully decrypted.</p>
+     *
+     * @param jwe the parsed claims jwe
+     * @return any object to be used after inspecting the JWE, or {@code null} if no return value is necessary.
+     * @since JJWT_RELEASE_VERSION
+     */
+    T onClaimsJwe(Jwe<Claims> jwe);
+
 }

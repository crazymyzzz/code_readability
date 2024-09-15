@@ -21,32 +21,42 @@ package io.jsonwebtoken;
  * known/expected for a particular use case.
  *
  * <p>All of the methods in this implementation throw exceptions: overridden methods represent
- * scenarios expected by calling code in known situations.  It would be unexpected to receive a JWS or JWT that did
+ * scenarios expected by calling code in known situations.  It would be unexpected to receive a JWT that did
  * not match parsing expectations, so all non-overridden methods throw exceptions to indicate that the JWT
  * input was unexpected.</p>
  *
  * @param <T> the type of object to return to the parser caller after handling the parsed JWT.
  * @since 0.2
  */
-public class JwtHandlerAdapter<T> implements JwtHandler<T> {
+public abstract class JwtHandlerAdapter<T> implements JwtHandler<T> {
 
     @Override
-    public T onPlaintextJwt(Jwt<Header, String> jwt) {
-        throw new UnsupportedJwtException("Unsigned plaintext JWTs are not supported.");
+    public T onContentJwt(Jwt<UnprotectedHeader, byte[]> jwt) {
+        throw new UnsupportedJwtException("Unprotected content JWTs are not supported.");
     }
 
     @Override
-    public T onClaimsJwt(Jwt<Header, Claims> jwt) {
-        throw new UnsupportedJwtException("Unsigned Claims JWTs are not supported.");
+    public T onClaimsJwt(Jwt<UnprotectedHeader, Claims> jwt) {
+        throw new UnsupportedJwtException("Unprotected Claims JWTs are not supported.");
     }
 
     @Override
-    public T onPlaintextJws(Jws<String> jws) {
-        throw new UnsupportedJwtException("Signed plaintext JWSs are not supported.");
+    public T onContentJws(Jws<byte[]> jws) {
+        throw new UnsupportedJwtException("Signed content JWTs are not supported.");
     }
 
     @Override
     public T onClaimsJws(Jws<Claims> jws) {
-        throw new UnsupportedJwtException("Signed Claims JWSs are not supported.");
+        throw new UnsupportedJwtException("Signed Claims JWTs are not supported.");
+    }
+
+    @Override
+    public T onContentJwe(Jwe<byte[]> jwe) {
+        throw new UnsupportedJwtException("Encrypted content JWTs are not supported.");
+    }
+
+    @Override
+    public T onClaimsJwe(Jwe<Claims> jwe) {
+        throw new UnsupportedJwtException("Encrypted Claims JWTs are not supported.");
     }
 }

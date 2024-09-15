@@ -22,7 +22,7 @@ import java.security.Key;
  * should be used to verify a JWS signature.
  *
  * <p>A {@code SigningKeyResolver} is necessary when the signing key is not already known before parsing the JWT and the
- * JWT header or payload (plaintext body or Claims) must be inspected first to determine how to look up the signing key.
+ * JWT header or payload (byte array or Claims) must be inspected first to determine how to look up the signing key.
  * Once returned by the resolver, the JwtParser will then verify the JWS signature with the returned key.  For
  * example:</p>
  *
@@ -40,13 +40,15 @@ import java.security.Key;
  *
  * <h2>Using an Adapter</h2>
  *
- * <p>If you only need to resolve a signing key for a particular JWS (either a plaintext or Claims JWS), consider using
+ * <p>If you only need to resolve a signing key for a particular JWS (either a content or Claims JWS), consider using
  * the {@link io.jsonwebtoken.SigningKeyResolverAdapter} and overriding only the method you need to support instead of
  * implementing this interface directly.</p>
  *
- * @see io.jsonwebtoken.SigningKeyResolverAdapter
+ * @see io.jsonwebtoken.JwtParserBuilder#setKeyLocator(Locator)
  * @since 0.4
+ * @deprecated since JJWT_RELEASE_VERSION. Implement {@link Locator} instead.
  */
+@Deprecated
 public interface SigningKeyResolver {
 
     /**
@@ -54,20 +56,20 @@ public interface SigningKeyResolver {
      * header and claims.
      *
      * @param header the header of the JWS to validate
-     * @param claims the claims (body) of the JWS to validate
+     * @param claims the Claims payload of the JWS to validate
      * @return the signing key that should be used to validate a digital signature for the Claims JWS with the specified
      * header and claims.
      */
     Key resolveSigningKey(JwsHeader header, Claims claims);
 
     /**
-     * Returns the signing key that should be used to validate a digital signature for the Plaintext JWS with the
-     * specified header and plaintext payload.
+     * Returns the signing key that should be used to validate a digital signature for the content JWS with the
+     * specified header and byte array payload.
      *
-     * @param header    the header of the JWS to validate
-     * @param plaintext the plaintext body of the JWS to validate
-     * @return the signing key that should be used to validate a digital signature for the Plaintext JWS with the
-     * specified header and plaintext payload.
+     * @param header  the header of the JWS to validate
+     * @param content the byte array payload of the JWS to validate
+     * @return the signing key that should be used to validate a digital signature for the content JWS with the
+     * specified header and byte array payload.
      */
-    Key resolveSigningKey(JwsHeader header, String plaintext);
+    Key resolveSigningKey(JwsHeader header, byte[] content);
 }

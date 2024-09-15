@@ -21,44 +21,67 @@ import javax.crypto.spec.SecretKeySpec;
 import java.security.Key;
 
 /**
- * An <a href="http://en.wikipedia.org/wiki/Adapter_pattern">Adapter</a> implementation of the
+ * <h2>Deprecation Notice</h2>
+ *
+ * <p>As of JJWT JJWT_RELEASE_VERSION, various Resolver concepts (including the {@code SigningKeyResolver}) have been
+ * unified into a single {@link Locator} interface.  For key location, (for both signing and encryption keys),
+ * use the {@link JwtParserBuilder#setKeyLocator(Locator)} to configure a parser with your desired Key locator instead
+ * of using a {@code SigningKeyResolver}. Also see {@link LocatorAdapter} for the Adapter pattern parallel of this
+ * class. <b>This {@code SigningKeyResolverAdapter} class will be removed before the 1.0 release.</b></p>
+ *
+ * <p><b>Previous Documentation</b></p>
+ *
+ * <p>An <a href="http://en.wikipedia.org/wiki/Adapter_pattern">Adapter</a> implementation of the
  * {@link SigningKeyResolver} interface that allows subclasses to process only the type of JWS body that
- * is known/expected for a particular case.
+ * is known/expected for a particular case.</p>
  *
- * <p>The {@link #resolveSigningKey(JwsHeader, Claims)} and {@link #resolveSigningKey(JwsHeader, String)} method
+ * <p>The {@link #resolveSigningKey(JwsHeader, Claims)} and {@link #resolveSigningKey(JwsHeader, byte[])} method
  * implementations delegate to the
- * {@link #resolveSigningKeyBytes(JwsHeader, Claims)} and {@link #resolveSigningKeyBytes(JwsHeader, String)} methods
+ * {@link #resolveSigningKeyBytes(JwsHeader, Claims)} and {@link #resolveSigningKeyBytes(JwsHeader, byte[])} methods
  * respectively.  The latter two methods simply throw exceptions:  they represent scenarios expected by
  * calling code in known situations, and it is expected that you override the implementation in those known situations;
  * non-overridden *KeyBytes methods indicates that the JWS input was unexpected.</p>
  *
- * <p>If either {@link #resolveSigningKey(JwsHeader, String)} or {@link #resolveSigningKey(JwsHeader, Claims)}
+ * <p>If either {@link #resolveSigningKey(JwsHeader, byte[])} or {@link #resolveSigningKey(JwsHeader, Claims)}
  * are not overridden, one (or both) of the *KeyBytes variants must be overridden depending on your expected
  * use case.  You do not have to override any method that does not represent an expected condition.</p>
  *
+ * @see io.jsonwebtoken.JwtParserBuilder#setKeyLocator(Locator)
+ * @see LocatorAdapter
  * @since 0.4
+ * @deprecated since JJWT_RELEASE_VERSION. Use {@link LocatorAdapter LocatorAdapter} with
+ * {@link JwtParserBuilder#setKeyLocator(Locator)}
  */
+@SuppressWarnings("DeprecatedIsStillUsed")
+@Deprecated
 public class SigningKeyResolverAdapter implements SigningKeyResolver {
 
+    /**
+     * Default constructor.
+     */
+    public SigningKeyResolverAdapter() {
+
+    }
+
     @Override
     public Key resolveSigningKey(JwsHeader header, Claims claims) {
         SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
-        Assert.isTrue(alg.isHmac(), "The default resolveSigningKey(JwsHeader, Claims) implementation cannot be " +
-                                    "used for asymmetric key algorithms (RSA, Elliptic Curve).  " +
-                                    "Override the resolveSigningKey(JwsHeader, Claims) method instead and return a " +
-                                    "Key instance appropriate for the " + alg.name() + " algorithm.");
+        Assert.isTrue(alg.isHmac(), "The default resolveSigningKey(JwsHeader, Claims) implementation cannot " +
+                "be used for asymmetric key algorithms (RSA, Elliptic Curve).  " +
+                "Override the resolveSigningKey(JwsHeader, Claims) method instead and return a " +
+                "Key instance appropriate for the " + alg.name() + " algorithm.");
         byte[] keyBytes = resolveSigningKeyBytes(header, claims);
         return new SecretKeySpec(keyBytes, alg.getJcaName());
     }
 
     @Override
-    public Key resolveSigningKey(JwsHeader header, String plaintext) {
+    public Key resolveSigningKey(JwsHeader header, byte[] content) {
         SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
-        Assert.isTrue(alg.isHmac(), "The default resolveSigningKey(JwsHeader, String) implementation cannot be " +
-                                    "used for asymmetric key algorithms (RSA, Elliptic Curve).  " +
-                                    "Override the resolveSigningKey(JwsHeader, String) method instead and return a " +
-                                    "Key instance appropriate for the " + alg.name() + " algorithm.");
-        byte[] keyBytes = resolveSigningKeyBytes(header, plaintext);
+        Assert.isTrue(alg.isHmac(), "The default resolveSigningKey(JwsHeader, byte[]) implementation cannot " +
+                "be used for asymmetric key algorithms (RSA, Elliptic Curve).  " +
+                "Override the resolveSigningKey(JwsHeader, byte[]) method instead and return a " +
+                "Key instance appropriate for the " + alg.name() + " algorithm.");
+        byte[] keyBytes = resolveSigningKeyBytes(header, content);
         return new SecretKeySpec(keyBytes, alg.getJcaName());
     }
 
@@ -76,24 +99,25 @@ public class SigningKeyResolverAdapter implements SigningKeyResolver {
      */
     public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
         throw new UnsupportedJwtException("The specified SigningKeyResolver implementation does not support " +
-                                          "Claims JWS signing key resolution.  Consider overriding either the " +
-                                          "resolveSigningKey(JwsHeader, Claims) method or, for HMAC algorithms, the " +
-                                          "resolveSigningKeyBytes(JwsHeader, Claims) method.");
+                "Claims JWS signing key resolution.  Consider overriding either the " +
+                "resolveSigningKey(JwsHeader, Claims) method or, for HMAC algorithms, the " +
+                "resolveSigningKeyBytes(JwsHeader, Claims) method.");
     }
 
     /**
-     * Convenience method invoked by {@link #resolveSigningKey(JwsHeader, String)} that obtains the necessary signing
-     * key bytes.  This implementation simply throws an exception: if the JWS parsed is a plaintext JWS, you must
-     * override this method or the {@link #resolveSigningKey(JwsHeader, String)} method instead.
+     * Convenience method invoked by {@link #resolveSigningKey(JwsHeader, byte[])} that obtains the necessary signing
+     * key bytes.  This implementation simply throws an exception: if the JWS parsed is a content JWS, you must
+     * override this method or the {@link #resolveSigningKey(JwsHeader, byte[])} method instead.
      *
-     * @param header the parsed {@link JwsHeader}
-     * @param payload the parsed String plaintext payload
+     * @param header  the parsed {@link JwsHeader}
+     * @param content the byte array payload
      * @return the signing key bytes to use to verify the JWS signature.
      */
-    public byte[] resolveSigningKeyBytes(JwsHeader header, String payload) {
+    @SuppressWarnings("unused")
+    public byte[] resolveSigningKeyBytes(JwsHeader header, byte[] content) {
         throw new UnsupportedJwtException("The specified SigningKeyResolver implementation does not support " +
-                                          "plaintext JWS signing key resolution.  Consider overriding either the " +
-                                          "resolveSigningKey(JwsHeader, String) method or, for HMAC algorithms, the " +
-                                          "resolveSigningKeyBytes(JwsHeader, String) method.");
+                "content JWS signing key resolution.  Consider overriding either the " +
+                "resolveSigningKey(JwsHeader, byte[]) method or, for HMAC algorithms, the " +
+                "resolveSigningKeyBytes(JwsHeader, byte[]) method.");
     }
 }

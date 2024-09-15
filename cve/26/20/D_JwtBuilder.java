@@ -19,21 +19,55 @@ import io.jsonwebtoken.io.Decoder;
 import io.jsonwebtoken.io.Decoders;
 import io.jsonwebtoken.io.Encoder;
 import io.jsonwebtoken.io.Serializer;
+import io.jsonwebtoken.lang.Builder;
+import io.jsonwebtoken.security.AeadAlgorithm;
 import io.jsonwebtoken.security.InvalidKeyException;
+import io.jsonwebtoken.security.KeyAlgorithm;
 import io.jsonwebtoken.security.Keys;
+import io.jsonwebtoken.security.Password;
+import io.jsonwebtoken.security.SecureDigestAlgorithm;
+import io.jsonwebtoken.security.StandardKeyAlgorithms;
+import io.jsonwebtoken.security.StandardSecureDigestAlgorithms;
+import io.jsonwebtoken.security.WeakKeyException;
 
+import javax.crypto.SecretKey;
 import java.security.Key;
+import java.security.PrivateKey;
+import java.security.Provider;
+import java.security.SecureRandom;
+import java.security.interfaces.ECKey;
+import java.security.interfaces.RSAKey;
 import java.util.Date;
 import java.util.Map;
 
 /**
- * A builder for constructing JWTs.
+ * A builder for constructing Unprotected JWTs, Signed JWTs (aka 'JWS's) and Encrypted JWTs (aka 'JWE's).
  *
  * @since 0.1
  */
 public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
 
-    //replaces any existing header with the specified header.
+    /**
+     * Sets the JCA Provider to use during cryptographic signing or encryption operations, or {@code null} if the
+     * JCA subsystem preferred provider should be used.
+     *
+     * @param provider the JCA Provider to use during cryptographic signing or encryption operations, or {@code null} if the
+     *                 JCA subsystem preferred provider should be used.
+     * @return the builder for method chaining.
+     * @since JJWT_RELEASE_VERSION
+     */
+    JwtBuilder setProvider(Provider provider);
+
+    /**
+     * Sets the {@link SecureRandom} to use during cryptographic signing or encryption operations, or {@code null} if
+     * a default {@link SecureRandom} should be used.
+     *
+     * @param secureRandom the {@link SecureRandom} to use during cryptographic signing or encryption operations, or
+     *                     {@code null} if a default {@link SecureRandom} should be used.
+     * @return the builder for method chaining.
+     * @since JJWT_RELEASE_VERSION
+     */
+    JwtBuilder setSecureRandom(SecureRandom secureRandom);
 
     /**
      * Sets (and replaces) any existing header with the specified header.  If you do not want to replace the existing
@@ -42,7 +76,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * @param header the header to set (and potentially replace any existing header).
      * @return the builder for method chaining.
      */
-    JwtBuilder setHeader(Header header);
+    JwtBuilder setHeader(Header<?> header); //replaces any existing header with the specified header.
 
     /**
      * Sets (and replaces) any existing header with the specified header.  If you do not want to replace the existing
@@ -51,7 +85,17 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * @param header the header to set (and potentially replace any existing header).
      * @return the builder for method chaining.
      */
-    JwtBuilder setHeader(Map<String, Object> header);
+    JwtBuilder setHeader(Map<String, ?> header);
+
+    /**
+     * Sets (and replaces) any existing header with the header resulting from the specified builder's
+     * {@link Builder#build()} result.
+     *
+     * @param builder the builder to use to obtain the header
+     * @return the JwtBuilder for method chaining.
+     * @since JJWT_RELEASE_VERSION
+     */
+    JwtBuilder setHeader(Builder<? extends Header<?>> builder);
 
     /**
      * Applies the specified name/value pairs to the header.  If a header does not yet exist at the time this method
@@ -60,9 +104,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * @param params the header name/value pairs to append to the header.
      * @return the builder for method chaining.
      */
-    JwtBuilder setHeaderParams(Map<String, Object> params);
-
-    //sets the specified header parameter, overwriting any previous value under the same name.
+    JwtBuilder setHeaderParams(Map<String, ?> params);
 
     /**
      * Applies the specified name/value pair to the header.  If a header does not yet exist at the time this method
@@ -75,35 +117,97 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
     JwtBuilder setHeaderParam(String name, Object value);
 
     /**
-     * Sets the JWT's payload to be a plaintext (non-JSON) string.  If you want the JWT body to be JSON, use the
-     * {@link #setClaims(Claims)} or {@link #setClaims(java.util.Map)} methods instead.
+     * Sets the JWT payload to the string's UTF-8-encoded bytes.  It is strongly recommended to also set the
+     * {@link Header#getContentType() contentType} header value so the JWT recipient may inspect that value to
+     * determine how to convert the byte array to the final data type as desired. In this case, consider using
+     * {@link #setContent(byte[], String)} instead.
+     *
+     * <p>This is a convenience method that is effectively the same as:</p>
+     * <blockquote><pre>
+     * {@link #setContent(byte[]) setPayload}(payload.getBytes(StandardCharsets.UTF_8));</pre></blockquote>
+     *
+     * <p>If you want the JWT payload to be JSON, use the
+     * {@link #setClaims(Claims)} or {@link #setClaims(java.util.Map)} methods instead.</p>
      *
      * <p>The payload and claims properties are mutually exclusive - only one of the two may be used.</p>
      *
-     * @param payload the plaintext (non-JSON) string that will be the body of the JWT.
+     * @param payload the string used to set UTF-8-encoded bytes as the JWT payload.
      * @return the builder for method chaining.
+     * @see #setContent(byte[])
+     * @see #setContent(byte[], String)
+     * @deprecated since JJWT_RELEASE VERSION in favor of {@link #setContent(byte[])} or {@link #setContent(byte[], String)}
+     * because both Claims and Content are technically 'payloads', so this method name is misleading.  This method will
+     * be removed before the 1.0 release.
      */
+    @Deprecated
     JwtBuilder setPayload(String payload);
 
     /**
-     * Sets the JWT payload to be a JSON Claims instance.  If you do not want the JWT body to be JSON and instead want
-     * it to be a plaintext string, use the {@link #setPayload(String)} method instead.
+     * Sets the JWT payload to be the specified content byte array.
+     *
+     * <p><b>Content Type Recommendation</b></p>
+     *
+     * <p>Unless you are confident that the JWT recipient will <em>always</em> know how to use
+     * the given byte array without additional metadata, it is strongly recommended to use the
+     * {@link #setContent(byte[], String)} method instead of this one.  That method ensures that a JWT recipient
+     * can inspect the {@code cty} header to know how to handle the byte array without ambiguity.</p>
+     *
+     * <p>Note that the content and claims properties are mutually exclusive - only one of the two may be used.</p>
+     *
+     * @param content the content byte array to use as the JWT payload
+     * @return the builder for method chaining.
+     * @since JJWT_RELEASE_VERSION
+     */
+    JwtBuilder setContent(byte[] content);
+
+    /**
+     * Convenience method that sets the JWT payload to be the specified content byte array and also sets the
+     * {@link Header#setContentType(String) contentType} header value to a compact {@code cty} media type
+     * identifier to indicate the data format of the byte array. The JWT recipient can inspect the
+     * {@code cty} value to determine how to convert the byte array to the final content type as desired.
+     *
+     * <p><b>Compact Media Type Identifier</b></p>
+     *
+     * <p>As a convenience, this method will automatically trim any <code><b>application/</b></code> prefix from the
+     * {@code cty} string if possible according to the
+     * <a href="https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.10">JWT specification recommendations</a>.</p>
+     *
+     * <p>If for some reason you do not wish to adhere to the JWT specification recommendation, do not call this
+     * method - instead call {@link #setContent(byte[])} and {@link Header#setContentType(String)} independently.</p>
+     *
+     * <p>If you want the JWT payload to be JSON claims, use the {@link #setClaims(Claims)} or
+     * {@link #setClaims(java.util.Map)} methods instead.</p>
+     *
+     * <p>Note that the content and claims properties are mutually exclusive - only one of the two may be used.</p>
+     *
+     * @param content the content byte array that will be the JWT payload.  Cannot be null or empty.
+     * @param cty     the content type (media type) identifier attributed to the byte array. Cannot be null or empty.
+     * @return the builder for method chaining.
+     * @throws IllegalArgumentException if either {@code payload} or {@code cty} are null or empty.
+     * @since JJWT_RELEASE_VERSION
+     */
+    JwtBuilder setContent(byte[] content, String cty) throws IllegalArgumentException;
+
+    /**
+     * Sets the JWT payload to be a JSON Claims instance.  If you do not want the JWT payload to be JSON claims and
+     * instead want it to be a byte array representing any type of content, use the {@link #setContent(byte[])}
+     * method instead.
      *
      * <p>The payload and claims properties are mutually exclusive - only one of the two may be used.</p>
      *
-     * @param claims the JWT claims to be set as the JWT body.
+     * @param claims the JWT claims to be set as the JWT payload.
      * @return the builder for method chaining.
      */
     JwtBuilder setClaims(Claims claims);
 
     /**
      * Sets the JWT payload to be a JSON Claims instance populated by the specified name/value pairs.  If you do not
-     * want the JWT body to be JSON and instead want it to be a plaintext string, use the {@link #setPayload(String)}
-     * method instead.
+     * want the JWT payload to be JSON claims and instead want it to be a byte array for any content, use the
+     * {@link #setContent(byte[])} or {@link #setContent(byte[], String)} methods instead.
      *
-     * <p>The payload* and claims* properties are mutually exclusive - only one of the two may be used.</p>
+     * <p>The payload and claims properties are mutually exclusive - only one of the two may be used.</p>
      *
-     * @param claims the JWT claims to be set as the JWT body.
+     * @param claims the JWT Claims to be set as the JWT payload.
      * @return the builder for method chaining.
      */
     JwtBuilder setClaims(Map<String, ?> claims);
@@ -114,17 +218,17 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      *
      * <p>The payload and claims properties are mutually exclusive - only one of the two may be used.</p>
      *
-     * @param claims the JWT claims to be added to the JWT body.
+     * @param claims the JWT Claims to be added to the JWT payload.
      * @return the builder for method chaining.
      * @since 0.8
      */
-    JwtBuilder addClaims(Map<String, Object> claims);
+    JwtBuilder addClaims(Map<String, ?> claims);
 
     /**
      * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.1">
      * <code>iss</code></a> (issuer) value.  A {@code null} value will remove the property from the Claims.
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set
      * the Claims {@link Claims#setIssuer(String) issuer} field with the specified value.  This allows you to write
      * code like this:</p>
      *
@@ -151,7 +255,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.2">
      * <code>sub</code></a> (subject) value.  A {@code null} value will remove the property from the Claims.
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set
      * the Claims {@link Claims#setSubject(String) subject} field with the specified value.  This allows you to write
      * code like this:</p>
      *
@@ -178,7 +282,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * Sets the JWT Claims <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.3">
      * <code>aud</code></a> (audience) value.  A {@code null} value will remove the property from the Claims.
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set
      * the Claims {@link Claims#setAudience(String) audience} field with the specified value.  This allows you to write
      * code like this:</p>
      *
@@ -207,7 +311,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      *
      * <p>A JWT obtained after this timestamp should not be used.</p>
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set
      * the Claims {@link Claims#setExpiration(java.util.Date) expiration} field with the specified value.  This allows
      * you to write code like this:</p>
      *
@@ -236,7 +340,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      *
      * <p>A JWT obtained before this timestamp should not be used.</p>
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set
      * the Claims {@link Claims#setNotBefore(java.util.Date) notBefore} field with the specified value.  This allows
      * you to write code like this:</p>
      *
@@ -265,7 +369,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      *
      * <p>The value is the timestamp when the JWT was created.</p>
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set
      * the Claims {@link Claims#setIssuedAt(java.util.Date) issuedAt} field with the specified value.  This allows
      * you to write code like this:</p>
      *
@@ -296,7 +400,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * manner that ensures that there is a negligible probability that the same value will be accidentally
      * assigned to a different data object.  The ID can be used to prevent the JWT from being replayed.</p>
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set
      * the Claims {@link Claims#setId(String) id} field with the specified value.  This allows
      * you to write code like this:</p>
      *
@@ -322,7 +426,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
     /**
      * Sets a custom JWT Claims parameter value.  A {@code null} value will remove the property from the Claims.
      *
-     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT body and then set the
+     * <p>This is a convenience method.  It will first ensure a Claims instance exists as the JWT payload and then set the
      * named property on the Claims instance using the Claims {@link Claims#put(Object, Object) put} method. This allows
      * you to write code like this:</p>
      *
@@ -345,20 +449,144 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
     JwtBuilder claim(String name, Object value);
 
     /**
-     * Signs the constructed JWT with the specified key using the key's
-     * {@link SignatureAlgorithm#forSigningKey(Key) recommended signature algorithm}, producing a JWS. If the
-     * recommended signature algorithm isn't sufficient for your needs, consider using
-     * {@link #signWith(Key, SignatureAlgorithm)} instead.
+     * Signs the constructed JWT with the specified key using the key's <em>recommended signature algorithm</em>
+     * as defined below, producing a JWS.  If the recommended signature algorithm isn't sufficient for your needs,
+     * consider using {@link #signWith(Key, SecureDigestAlgorithm)} instead.
      *
      * <p>If you are looking to invoke this method with a byte array that you are confident may be used for HMAC-SHA
      * algorithms, consider using {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(bytes)} to
      * convert the byte array into a valid {@code Key}.</p>
      *
+     * <p><b><a id="recsigalg">Recommended Signature Algorithm</a></b></p>
+     *
+     * <p>The recommended signature algorithm used with a given key is chosen based on the following:</p>
+     * <table>
+     * <caption>Key Recommended Signature Algorithm</caption>
+     * <thead>
+     * <tr>
+     * <th>If the Key is a:</th>
+     * <th>And:</th>
+     * <th>With a key size of:</th>
+     * <th>The SignatureAlgorithm used will be:</th>
+     * </tr>
+     * </thead>
+     * <tbody>
+     * <tr>
+     * <td>{@link SecretKey}</td>
+     * <td><code>{@link Key#getAlgorithm() getAlgorithm()}.equals("HmacSHA256")</code><sup>1</sup></td>
+     * <td>256 &lt;= size &lt;= 383 <sup>2</sup></td>
+     * <td>{@link StandardSecureDigestAlgorithms#HS256 HS256}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link SecretKey}</td>
+     * <td><code>{@link Key#getAlgorithm() getAlgorithm()}.equals("HmacSHA384")</code><sup>1</sup></td>
+     * <td>384 &lt;= size &lt;= 511</td>
+     * <td>{@link StandardSecureDigestAlgorithms#HS384 HS384}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link SecretKey}</td>
+     * <td><code>{@link Key#getAlgorithm() getAlgorithm()}.equals("HmacSHA512")</code><sup>1</sup></td>
+     * <td>512 &lt;= size</td>
+     * <td>{@link StandardSecureDigestAlgorithms#HS512 HS512}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link ECKey}</td>
+     * <td><code>instanceof {@link PrivateKey}</code></td>
+     * <td>256 &lt;= size &lt;= 383 <sup>3</sup></td>
+     * <td>{@link StandardSecureDigestAlgorithms#ES256 ES256}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link ECKey}</td>
+     * <td><code>instanceof {@link PrivateKey}</code></td>
+     * <td>384 &lt;= size &lt;= 520 <sup>4</sup></td>
+     * <td>{@link StandardSecureDigestAlgorithms#ES384 ES384}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link ECKey}</td>
+     * <td><code>instanceof {@link PrivateKey}</code></td>
+     * <td><b>521</b> &lt;= size <sup>4</sup></td>
+     * <td>{@link StandardSecureDigestAlgorithms#ES512 ES512}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link RSAKey}</td>
+     * <td><code>instanceof {@link PrivateKey}</code></td>
+     * <td>2048 &lt;= size &lt;= 3071 <sup>5,6</sup></td>
+     * <td>{@link StandardSecureDigestAlgorithms#RS256 RS256}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link RSAKey}</td>
+     * <td><code>instanceof {@link PrivateKey}</code></td>
+     * <td>3072 &lt;= size &lt;= 4095 <sup>6</sup></td>
+     * <td>{@link StandardSecureDigestAlgorithms#RS384 RS384}</td>
+     * </tr>
+     * <tr>
+     * <td>{@link RSAKey}</td>
+     * <td><code>instanceof {@link PrivateKey}</code></td>
+     * <td>4096 &lt;= size <sup>5</sup></td>
+     * <td>{@link StandardSecureDigestAlgorithms#RS512 RS512}</td>
+     * </tr>
+     * <tr>
+     *     <td><a href="https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/security/interfaces/EdECKey.html">EdECKey</a><sup>7</sup></td>
+     *     <td><code>instanceof {@link PrivateKey}</code></td>
+     *     <td>256</td>
+     *     <td>{@link StandardSecureDigestAlgorithms#Ed25519 Ed25519}</td>
+     * </tr>
+     * <tr>
+     *     <td><a href="https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/security/interfaces/EdECKey.html">EdECKey</a><sup>7</sup></td>
+     *     <td><code>instanceof {@link PrivateKey}</code></td>
+     *     <td>456</td>
+     *     <td>{@link StandardSecureDigestAlgorithms#Ed448 Ed448}</td>
+     * </tr>
+     * </tbody>
+     * </table>
+     * <p>Notes:</p>
+     * <ol>
+     * <li>{@code SecretKey} instances must have an {@link Key#getAlgorithm() algorithm} name equal
+     * to {@code HmacSHA256}, {@code HmacSHA384} or {@code HmacSHA512}.  If not, the key bytes might not be
+     * suitable for HMAC signatures will be rejected with a {@link InvalidKeyException}. </li>
+     * <li>The JWT <a href="https://tools.ietf.org/html/rfc7518#section-3.2">JWA Specification (RFC 7518,
+     * Section 3.2)</a> mandates that HMAC-SHA-* signing keys <em>MUST</em> be 256 bits or greater.
+     * {@code SecretKey}s with key lengths less than 256 bits will be rejected with an
+     * {@link WeakKeyException}.</li>
+     * <li>The JWT <a href="https://tools.ietf.org/html/rfc7518#section-3.4">JWA Specification (RFC 7518,
+     * Section 3.4)</a> mandates that ECDSA signing key lengths <em>MUST</em> be 256 bits or greater.
+     * {@code ECKey}s with key lengths less than 256 bits will be rejected with a
+     * {@link WeakKeyException}.</li>
+     * <li>The ECDSA {@code P-521} curve does indeed use keys of <b>521</b> bits, not 512 as might be expected.  ECDSA
+     * keys of 384 &lt; size &lt;= 520 are suitable for ES384, while ES512 requires keys &gt;= 521 bits.  The '512' part of the
+     * ES512 name reflects the usage of the SHA-512 algorithm, not the ECDSA key length.  ES512 with ECDSA keys less
+     * than 521 bits will be rejected with a {@link WeakKeyException}.</li>
+     * <li>The JWT <a href="https://tools.ietf.org/html/rfc7518#section-3.3">JWA Specification (RFC 7518,
+     * Section 3.3)</a> mandates that RSA signing key lengths <em>MUST</em> be 2048 bits or greater.
+     * {@code RSAKey}s with key lengths less than 2048 bits will be rejected with a
+     * {@link WeakKeyException}.</li>
+     * <li>Technically any RSA key of length &gt;= 2048 bits may be used with the
+     * {@link StandardSecureDigestAlgorithms#RS256 RS256}, {@link StandardSecureDigestAlgorithms#RS384 RS384}, and
+     * {@link StandardSecureDigestAlgorithms#RS512 RS512} algorithms, so we assume an RSA signature algorithm based on the key
+     * length to parallel similar decisions in the JWT specification for HMAC and ECDSA signature algorithms.
+     * This is not required - just a convenience.</li>
+     * <li><a href="https://docs.oracle.com/en/java/javase/15/docs/api/java.base/java/security/interfaces/EdECKey.html">EdECKey</a>s
+     * require JDK &gt;= 15 or BouncyCastle in the runtime classpath.</li>
+     * </ol>
+     *
+     * <p>This implementation does not use the {@link StandardSecureDigestAlgorithms#PS256 PS256},
+     * {@link StandardSecureDigestAlgorithms#PS384 PS384}, or {@link StandardSecureDigestAlgorithms#PS512 PS512} RSA variants for any
+     * specified {@link RSAKey} because the the {@link StandardSecureDigestAlgorithms#RS256 RS256},
+     * {@link StandardSecureDigestAlgorithms#RS384 RS384}, and {@link StandardSecureDigestAlgorithms#RS512 RS512} algorithms are
+     * available in the JDK by default while the {@code PS}* variants require either JDK 11 or an additional JCA
+     * Provider (like BouncyCastle).  If you wish to use a {@code PS}* variant with your key, use the
+     * {@link #signWith(Key, SecureDigestAlgorithm)} method instead.</p>
+     *
+     * <p>Finally, this method will throw an {@link InvalidKeyException} for any key that does not match the
+     * heuristics and requirements documented above, since that inevitably means the Key is either insufficient,
+     * unsupported, or explicitly disallowed by the JWT specification.</p>
+     *
      * @param key the key to use for signing
      * @return the builder instance for method chaining.
-     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification as
-     *                             described by {@link SignatureAlgorithm#forSigningKey(Key)}.
-     * @see #signWith(Key, SignatureAlgorithm)
+     * @throws InvalidKeyException if the Key is insufficient, unsupported, or explicitly disallowed by the JWT
+     *                             specification as described above in <em>recommended signature algorithms</em>.
+     * @see Jwts#SIG
+     * @see #signWith(Key, SecureDigestAlgorithm)
      * @since 0.10.0
      */
     JwtBuilder signWith(Key key) throws InvalidKeyException;
@@ -369,17 +597,19 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * <p><b>Deprecation Notice: Deprecated as of 0.10.0</b></p>
      *
      * <p>Use {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(bytes)} to
-     * obtain the {@code Key} and then invoke {@link #signWith(Key)} or {@link #signWith(Key, SignatureAlgorithm)}.</p>
+     * obtain the {@code Key} and then invoke {@link #signWith(Key)} or
+     * {@link #signWith(Key, SecureDigestAlgorithm)}.</p>
      *
      * <p>This method will be removed in the 1.0 release.</p>
      *
      * @param alg       the JWS algorithm to use to digitally sign the JWT, thereby producing a JWS.
      * @param secretKey the algorithm-specific signing key to use to digitally sign the JWT.
      * @return the builder for method chaining.
-     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification as
-     *                             described by {@link SignatureAlgorithm#forSigningKey(Key)}.
+     * @throws InvalidKeyException if the Key is insufficient for the specified algorithm or explicitly disallowed by
+     *                             the JWT specification.
      * @deprecated as of 0.10.0: use {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(bytes)} to
-     * obtain the {@code Key} and then invoke {@link #signWith(Key)} or {@link #signWith(Key, SignatureAlgorithm)}.
+     * obtain the {@code Key} and then invoke {@link #signWith(Key)} or
+     * {@link #signWith(Key, SecureDigestAlgorithm)}.
      * This method will be removed in the 1.0 release.
      */
     @Deprecated
@@ -403,7 +633,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * <p>{@code String base64EncodedSecretKey = base64Encode(secretKeyBytes);}</p>
      *
      * <p>However, a non-trivial number of JJWT users were confused by the method signature and attempted to
-     * use raw password strings as the key argument - for example {@code signWith(HS256, myPassword)} - which is
+     * use raw password strings as the key argument - for example {@code with(HS256, myPassword)} - which is
      * almost always incorrect for cryptographic hashes and can produce erroneous or insecure results.</p>
      *
      * <p>See this
@@ -415,7 +645,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * <pre><code>
      * byte[] keyBytes = {@link Decoders Decoders}.{@link Decoders#BASE64 BASE64}.{@link Decoder#decode(Object) decode(base64EncodedSecretKey)};
      * Key key = {@link Keys Keys}.{@link Keys#hmacShaKeyFor(byte[]) hmacShaKeyFor(keyBytes)};
-     * jwtBuilder.signWith(key); //or {@link #signWith(Key, SignatureAlgorithm)}
+     * jwtBuilder.with(key); //or {@link #signWith(Key, SignatureAlgorithm)}
      * </code></pre>
      *
      * <p>This method will be removed in the 1.0 release.</p>
@@ -445,14 +675,21 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification for
      *                             the specified algorithm.
      * @see #signWith(Key)
-     * @deprecated since 0.10.0: use {@link #signWith(Key, SignatureAlgorithm)} instead.  This method will be removed
-     * in the 1.0 release.
+     * @deprecated since 0.10.0. Use {@link #signWith(Key, SecureDigestAlgorithm)} instead.
+     * This method will be removed before the 1.0 release.
      */
     @Deprecated
     JwtBuilder signWith(SignatureAlgorithm alg, Key key) throws InvalidKeyException;
 
     /**
-     * Signs the constructed JWT with the specified key using the specified algorithm, producing a JWS.
+     * <p><b>Deprecation Notice</b></p>
+     *
+     * <p><b>This has been deprecated since JJWT_RELEASE_VERSION.  Use
+     * {@link #signWith(Key, SecureDigestAlgorithm)} instead</b>.  Standard JWA algorithms
+     * are represented as instances of this new interface in the {@link Jwts#SIG}
+     * algorithm registry.</p>
+     *
+     * <p>Signs the constructed JWT with the specified key using the specified algorithm, producing a JWS.</p>
      *
      * <p>It is typically recommended to call the {@link #signWith(Key)} instead for simplicity.
      * However, this method can be useful if the recommended algorithm heuristics do not meet your needs or if
@@ -465,11 +702,97 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      *                             the specified algorithm.
      * @see #signWith(Key)
      * @since 0.10.0
+     * @deprecated since JJWT_RELEASE_VERSION to use the more flexible {@link #signWith(Key, SecureDigestAlgorithm)}.
      */
+    @Deprecated
     JwtBuilder signWith(Key key, SignatureAlgorithm alg) throws InvalidKeyException;
 
     /**
-     * Compresses the JWT body using the specified {@link CompressionCodec}.
+     * Signs the constructed JWT with the specified key using the specified algorithm, producing a JWS.
+     *
+     * <p>The {@link Jwts#SIG} registry makes available all standard signature
+     * algorithms defined in the JWA specification.</p>
+     *
+     * <p>It is typically recommended to call the {@link #signWith(Key)} instead for simplicity.
+     * However, this method can be useful if the recommended algorithm heuristics do not meet your needs or if
+     * you want explicit control over the signature algorithm used with the specified key.</p>
+     *
+     * @param key the signing key to use to digitally sign the JWT.
+     * @param <K> The type of key accepted by the {@code SignatureAlgorithm}.
+     * @param alg the JWS algorithm to use with the key to digitally sign the JWT, thereby producing a JWS.
+     * @return the builder for method chaining.
+     * @throws InvalidKeyException if the Key is insufficient or explicitly disallowed by the JWT specification for
+     *                             the specified algorithm.
+     * @see #signWith(Key)
+     * @see Jwts#SIG
+     * @since JJWT_RELEASE_VERSION
+     */
+    <K extends Key> JwtBuilder signWith(K key, io.jsonwebtoken.security.SecureDigestAlgorithm<? super K, ?> alg) throws InvalidKeyException;
+
+    /**
+     * Encrypts the constructed JWT with the specified symmetric {@code key} using the provided {@code enc}ryption
+     * algorithm, producing a JWE.  Because it is a symmetric key, the JWE recipient
+     * must also have access to the same key to decrypt.
+     *
+     * <p>This method is a convenience method that delegates to
+     * {@link #encryptWith(Key, KeyAlgorithm, AeadAlgorithm) encryptWith(Key, KeyAlgorithm, AeadAlgorithm)}
+     * based on the {@code key} argument:</p>
+     * <ul>
+     *     <li>If the provided {@code key} is a {@link Password Password} instance,
+     *     the {@code KeyAlgorithm} used will be one of the three JWA-standard password-based key algorithms
+     *      ({@link StandardKeyAlgorithms#PBES2_HS256_A128KW PBES2_HS256_A128KW},
+     *      {@link StandardKeyAlgorithms#PBES2_HS384_A192KW PBES2_HS384_A192KW}, or
+     *      {@link StandardKeyAlgorithms#PBES2_HS512_A256KW PBES2_HS512_A256KW}) as determined by the {@code enc} algorithm's
+     *      {@link AeadAlgorithm#getKeyBitLength() key length} requirement.</li>
+     *     <li>If the {@code key} is otherwise a standard {@code SecretKey}, the {@code KeyAlgorithm} will be
+     *     {@link StandardKeyAlgorithms#DIRECT}, indicating that {@code key} should be used directly with the
+     *     {@code enc} algorithm.  In this case, the {@code key} argument <em>MUST</em> be of sufficient strength to
+     *     use with the specified {@code enc} algorithm, otherwise an exception will be thrown during encryption. If
+     *     desired, secure-random keys suitable for an {@link AeadAlgorithm} may be generated using the algorithm's
+     *     {@link AeadAlgorithm#keyBuilder() keyBuilder}.</li>
+     * </ul>
+     *
+     * @param key the symmetric encryption key to use with the {@code enc} algorithm.
+     * @param enc the {@link AeadAlgorithm} algorithm used to encrypt the JWE, usually one of the JWA-standard
+     *            algorithms accessible via {@link Jwts#ENC}.
+     * @return the JWE builder for method chaining.
+     * @see Jwts#ENC
+     */
+    JwtBuilder encryptWith(SecretKey key, AeadAlgorithm enc);
+
+    /**
+     * Encrypts the constructed JWT using the specified {@code enc} algorithm with the symmetric key produced by the
+     * {@code keyAlg} when invoked with the given {@code key}, producing a JWE.
+     *
+     * <p>This behavior can be illustrated by the following pseudocode, a rough example of what happens during
+     * {@link #compact() compact}ion:</p>
+     * <blockquote><pre>
+     *     SecretKey encryptionKey = keyAlg.getEncryptionKey(key);           // (1)
+     *     byte[] jweCiphertext = enc.encrypt(payloadBytes, encryptionKey);  // (2)</pre></blockquote>
+     * <ol>
+     *     <li>The {@code keyAlg} argument is first invoked with the provided {@code key} argument, resulting in a
+     *         {@link SecretKey}.</li>
+     *     <li>This {@code SecretKey} result is used to call the provided {@code enc} encryption algorithm argument,
+     *         resulting in the final JWE ciphertext.</li>
+     * </ol>
+     *
+     * <p>Most application developers will reference one of the JWA
+     * {@link Jwts#KEY standard key algorithms} and {@link Jwts#ENC standard encryption algorithms}
+     * when invoking this method, but custom implementations are also supported.</p>
+     *
+     * @param <K>    the type of key that must be used with the specified {@code keyAlg} instance.
+     * @param key    the key used to invoke the provided {@code keyAlg} instance.
+     * @param keyAlg the key management algorithm that will produce the symmetric {@code SecretKey} to use with the
+     *               {@code enc} algorithm
+     * @param enc    the {@link AeadAlgorithm} algorithm used to encrypt the JWE
+     * @return the JWE builder for method chaining.
+     * @see Jwts#ENC
+     * @see Jwts#KEY
+     */
+    <K extends Key> JwtBuilder encryptWith(K key, KeyAlgorithm<? super K, ?> keyAlg, AeadAlgorithm enc);
+
+    /**
+     * Compresses the JWT payload using the specified {@link CompressionCodec}.
      *
      * <p>If your compact JWTs are large, and you want to reduce their total size during network transmission, this
      * can be useful.  For example, when embedding JWTs  in URLs, some browsers may not support URLs longer than a
@@ -477,7 +800,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
      *
      * <p><b>Compatibility Warning</b></p>
      *
-     * <p>The JWT family of specifications defines compression only for JWE (Json Web Encryption)
+     * <p>The JWT family of specifications defines compression only for JWE (JSON Web Encryption)
      * tokens.  Even so, JJWT will also support compression for JWS tokens as well if you choose to use it.
      * However, be aware that <b>if you use compression when creating a JWS token, other libraries may not be able to
      * parse that JWS token</b>.  When using compression for JWS tokens, be sure that all parties accessing the
@@ -507,7 +830,7 @@ public interface JwtBuilder extends ClaimsMutator<JwtBuilder> {
 
     /**
      * Performs object-to-JSON serialization with the specified Serializer.  This is used by the builder to convert
-     * JWT/JWS/JWT headers and claims Maps to JSON strings as required by the JWT specification.
+     * JWT/JWS/JWE headers and claims Maps to JSON strings as required by the JWT specification.
      *
      * <p>If this method is not called, JJWT will use whatever serializer it can find at runtime, checking for the
      * presence of well-known implementations such Jackson, Gson, and org.json.  If one of these is not found

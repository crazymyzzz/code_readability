@@ -17,6 +17,7 @@ package io.jsonwebtoken;
 
 import io.jsonwebtoken.io.Decoder;
 import io.jsonwebtoken.io.Deserializer;
+import io.jsonwebtoken.security.SecurityException;
 import io.jsonwebtoken.security.SignatureException;
 
 import java.security.Key;
@@ -28,9 +29,17 @@ import java.util.Map;
  *
  * @since 0.1
  */
+@SuppressWarnings("DeprecatedIsStillUsed")
 public interface JwtParser {
 
-    public static final char SEPARATOR_CHAR = '.';
+    /**
+     * Deprecated - this was an implementation detail accidentally added to the public interface. This
+     * will be removed in a future release.
+     *
+     * @deprecated since JJWT_RELEASE_VERSION, to be removed in a future relase.
+     */
+    @Deprecated
+    char SEPARATOR_CHAR = '.';
 
     /**
      * Ensures that the specified {@code jti} exists in the parsed JWT.  If missing or if the parsed
@@ -213,7 +222,7 @@ public interface JwtParser {
      * @param key the algorithm-specific signature verification key used to validate any discovered JWS digital
      *            signature.
      * @return the parser for method chaining.
-     * @deprecated see {@link JwtParserBuilder#setSigningKey(byte[])}.
+     * @deprecated in favor of {@link JwtParserBuilder#verifyWith(Key)}.
      * To construct a JwtParser use the corresponding builder via {@link Jwts#parserBuilder()}. This will construct an
      * immutable JwtParser.
      * <p><b>NOTE: this method will be removed before version 1.0</b>
@@ -259,7 +268,7 @@ public interface JwtParser {
      * @param base64EncodedSecretKey the BASE64-encoded algorithm-specific signature verification key to use to validate
      *                               any discovered JWS digital signature.
      * @return the parser for method chaining.
-     * @deprecated see {@link JwtParserBuilder#setSigningKey(String)}.
+     * @deprecated in favor of {@link JwtParserBuilder#verifyWith(Key)}.
      * To construct a JwtParser use the corresponding builder via {@link Jwts#parserBuilder()}. This will construct an
      * immutable JwtParser.
      * <p><b>NOTE: this method will be removed before version 1.0</b>
@@ -279,7 +288,7 @@ public interface JwtParser {
      * @param key the algorithm-specific signature verification key to use to validate any discovered JWS digital
      *            signature.
      * @return the parser for method chaining.
-     * @deprecated see {@link JwtParserBuilder#setSigningKey(Key)}.
+     * @deprecated in favor of {@link JwtParserBuilder#verifyWith(Key)}.
      * To construct a JwtParser use the corresponding builder via {@link Jwts#parserBuilder()}. This will construct an
      * immutable JwtParser.
      * <p><b>NOTE: this method will be removed before version 1.0</b>
@@ -292,7 +301,7 @@ public interface JwtParser {
      * a JWS's signature.  If the parsed String is not a JWS (no signature), this resolver is not used.
      *
      * <p>Specifying a {@code SigningKeyResolver} is necessary when the signing key is not already known before parsing
-     * the JWT and the JWT header or payload (plaintext body or Claims) must be inspected first to determine how to
+     * the JWT and the JWT header or payload (content byte array or Claims) must be inspected first to determine how to
      * look up the signing key.  Once returned by the resolver, the JwtParser will then verify the JWS signature with the
      * returned key.  For example:</p>
      *
@@ -314,22 +323,24 @@ public interface JwtParser {
      * @param signingKeyResolver the signing key resolver used to retrieve the signing key.
      * @return the parser for method chaining.
      * @since 0.4
-     * @deprecated see {@link JwtParserBuilder#setSigningKeyResolver(SigningKeyResolver)}.
+     * @deprecated in favor of {@link JwtParserBuilder#setKeyLocator(Locator)}.
      * To construct a JwtParser use the corresponding builder via {@link Jwts#parserBuilder()}. This will construct an
      * immutable JwtParser.
      * <p><b>NOTE: this method will be removed before version 1.0</b>
      */
+    @SuppressWarnings("DeprecatedIsStillUsed")
     @Deprecated
+    // TODO: remove for 1.0
     JwtParser setSigningKeyResolver(SigningKeyResolver signingKeyResolver);
 
     /**
      * Sets the {@link CompressionCodecResolver} used to acquire the {@link CompressionCodec} that should be used to
-     * decompress the JWT body. If the parsed JWT is not compressed, this resolver is not used.
+     * decompress the JWT payload. If the parsed JWT is not compressed, this resolver is not used.
      *
-     * <p><b>NOTE:</b> Compression is not defined by the JWT Specification, and it is not expected that other libraries
-     * (including JJWT versions &lt; 0.6.0) are able to consume a compressed JWT body correctly.  This method is only
-     * useful if the compact JWT was compressed with JJWT &gt;= 0.6.0 or another library that you know implements
-     * the same behavior.</p>
+     * <p><b>NOTE:</b> Compression is not defined by the JWS Specification - only the JWE Specification - and it is
+     * not expected that other libraries (including JJWT versions &lt; 0.6.0) are able to consume a compressed JWS
+     * payload correctly.  This method is only useful if the compact JWT was compressed with JJWT &gt;= 0.6.0 or another
+     * library that you know implements the same behavior.</p>
      *
      * <p><b>Default Support</b></p>
      *
@@ -341,10 +352,10 @@ public interface JwtParser {
      * your own {@link CompressionCodecResolver} and specify that via this method and also when
      * {@link io.jsonwebtoken.JwtBuilder#compressWith(CompressionCodec) building} JWTs.</p>
      *
-     * @param compressionCodecResolver the compression codec resolver used to decompress the JWT body.
+     * @param compressionCodecResolver the compression codec resolver used to decompress the JWT payload.
      * @return the parser for method chaining.
      * @since 0.6.0
-     * @deprecated see {@link JwtParserBuilder#setCompressionCodecResolver(CompressionCodecResolver)}.
+     * @deprecated in favor of {@link JwtParserBuilder#setCompressionCodecLocator(Locator)}.
      * To construct a JwtParser use the corresponding builder via {@link Jwts#parserBuilder()}. This will construct an
      * immutable JwtParser.
      * <p><b>NOTE: this method will be removed before version 1.0</b>
@@ -397,42 +408,45 @@ public interface JwtParser {
      * <p>Note that if you are reasonably sure that the token is signed, it is more efficient to attempt to
      * parse the token (and catching exceptions if necessary) instead of calling this method first before parsing.</p>
      *
-     * @param jwt the compact serialized JWT to check
+     * @param compact the compact serialized JWT to check
      * @return {@code true} if the specified JWT compact string represents a signed JWT (aka a 'JWS'), {@code false}
      * otherwise.
      */
-    boolean isSigned(String jwt);
+    boolean isSigned(String compact);
 
     /**
      * Parses the specified compact serialized JWT string based on the builder's current configuration state and
-     * returns the resulting JWT or JWS instance.
+     * returns the resulting JWT, JWS, or JWE instance.
      *
-     * <p>This method returns a JWT or JWS based on the parsed string.  Because it may be cumbersome to determine if it
-     * is a JWT or JWS, or if the body/payload is a Claims or String with {@code instanceof} checks, the
-     * {@link #parse(String, JwtHandler) parse(String,JwtHandler)} method allows for a type-safe callback approach that
-     * may help reduce code or instanceof checks.</p>
+     * <p>This method returns a JWT, JWS, or JWE based on the parsed string.  Because it may be cumbersome to
+     * determine if it is a JWT, JWS or JWE, or if the payload is a Claims or byte array with {@code instanceof} checks,
+     * the {@link #parse(String, JwtHandler) parse(String,JwtHandler)} method allows for a type-safe callback approach
+     * that may help reduce code or instanceof checks.</p>
      *
      * @param jwt the compact serialized JWT to parse
      * @return the specified compact serialized JWT string based on the builder's current configuration state.
      * @throws MalformedJwtException    if the specified JWT was incorrectly constructed (and therefore invalid).
-     *                                  Invalid
-     *                                  JWTs should not be trusted and should be discarded.
+     *                                  Invalid JWTs should not be trusted and should be discarded.
      * @throws SignatureException       if a JWS signature was discovered, but could not be verified.  JWTs that fail
      *                                  signature validation should not be trusted and should be discarded.
+     * @throws SecurityException        if the specified JWT string is a JWE and decryption fails
      * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
      *                                  before the time this method is invoked.
      * @throws IllegalArgumentException if the specified string is {@code null} or empty or only whitespace.
      * @see #parse(String, JwtHandler)
-     * @see #parsePlaintextJwt(String)
+     * @see #parseContentJwt(String)
      * @see #parseClaimsJwt(String)
-     * @see #parsePlaintextJws(String)
+     * @see #parseContentJws(String)
      * @see #parseClaimsJws(String)
+     * @see #parseContentJwe(String)
+     * @see #parseClaimsJwe(String)
      */
-    Jwt parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
+    Jwt<?, ?> parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException,
+            SecurityException, IllegalArgumentException;
 
     /**
      * Parses the specified compact serialized JWT string based on the builder's current configuration state and
-     * invokes the specified {@code handler} with the resulting JWT or JWS instance.
+     * invokes the specified {@code handler} with the resulting JWT, JWS, or JWE instance.
      *
      * <p>If you are confident of the format of the JWT before parsing, you can create an anonymous subclass using the
      * {@link io.jsonwebtoken.JwtHandlerAdapter JwtHandlerAdapter} and override only the methods you know are relevant
@@ -453,145 +467,220 @@ public interface JwtParser {
      * following convenience methods instead of this one:</p>
      *
      * <ul>
-     * <li>{@link #parsePlaintextJwt(String)}</li>
+     * <li>{@link #parseContentJwt(String)}</li>
      * <li>{@link #parseClaimsJwt(String)}</li>
-     * <li>{@link #parsePlaintextJws(String)}</li>
+     * <li>{@link #parseContentJws(String)}</li>
      * <li>{@link #parseClaimsJws(String)}</li>
+     * <li>{@link #parseContentJwe(String)}</li>
+     * <li>{@link #parseClaimsJwe(String)}</li>
      * </ul>
      *
-     * @param jwt the compact serialized JWT to parse
+     * @param jwt     the compact serialized JWT to parse
      * @param handler the handler to invoke when encountering a specific type of JWT
-     * @param <T> the type of object returned from the {@code handler}
+     * @param <T>     the type of object returned from the {@code handler}
      * @return the result returned by the {@code JwtHandler}
      * @throws MalformedJwtException    if the specified JWT was incorrectly constructed (and therefore invalid).
      *                                  Invalid JWTs should not be trusted and should be discarded.
      * @throws SignatureException       if a JWS signature was discovered, but could not be verified.  JWTs that fail
      *                                  signature validation should not be trusted and should be discarded.
+     * @throws SecurityException        if the specified JWT string is a JWE and decryption fails
      * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
      *                                  before the time this method is invoked.
      * @throws IllegalArgumentException if the specified string is {@code null} or empty or only whitespace, or if the
      *                                  {@code handler} is {@code null}.
-     * @see #parsePlaintextJwt(String)
+     * @see #parseContentJwt(String)
      * @see #parseClaimsJwt(String)
-     * @see #parsePlaintextJws(String)
+     * @see #parseContentJws(String)
      * @see #parseClaimsJws(String)
+     * @see #parseContentJwe(String)
+     * @see #parseClaimsJwe(String)
      * @see #parse(String)
      * @since 0.2
      */
-    <T> T parse(String jwt, JwtHandler<T> handler)
-            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
+    <T> T parse(String jwt, JwtHandler<T> handler) throws ExpiredJwtException, UnsupportedJwtException,
+            MalformedJwtException, SignatureException, SecurityException, IllegalArgumentException;
 
     /**
      * Parses the specified compact serialized JWT string based on the builder's current configuration state and
-     * returns the resulting unsigned plaintext JWT instance.
+     * returns the resulting unprotected content JWT instance. If the JWT creator set the (optional)
+     * {@link Header#getContentType() contentType} header value, the application may inspect that value to determine
+     * how to convert the byte array to the final content type as desired.
      *
      * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects an
-     * unsigned plaintext JWT. An unsigned plaintext JWT has a String (non-JSON) body payload and it is not
-     * cryptographically signed.</p>
+     * unprotected content JWT. An unprotected content JWT has a byte array payload and it is not
+     * cryptographically signed or encrypted. If the JWT creator set the (optional)
+     * {@link Header#getContentType() contentType} header value, the application may inspect that value to determine
+     * how to convert the byte array to the final content type as desired.</p>
      *
-     * <p><b>If the compact string presented does not reflect an unsigned plaintext JWT with non-JSON string body,
+     * <p><b>If the compact string presented does not reflect an unprotected content JWT with byte array payload,
      * an {@link UnsupportedJwtException} will be thrown.</b></p>
      *
-     * @param plaintextJwt a compact serialized unsigned plaintext JWT string.
+     * @param jwt a compact serialized unprotected content JWT string.
      * @return the {@link Jwt Jwt} instance that reflects the specified compact JWT string.
-     * @throws UnsupportedJwtException  if the {@code plaintextJwt} argument does not represent an unsigned plaintext
-     *                                  JWT
-     * @throws MalformedJwtException    if the {@code plaintextJwt} string is not a valid JWT
-     * @throws SignatureException       if the {@code plaintextJwt} string is actually a JWS and signature validation
-     *                                  fails
-     * @throws IllegalArgumentException if the {@code plaintextJwt} string is {@code null} or empty or only whitespace
+     * @throws UnsupportedJwtException  if the {@code jwt} argument does not represent an unprotected content JWT
+     * @throws MalformedJwtException    if the {@code jwt} string is not a valid JWT
+     * @throws SignatureException       if the {@code jwt} string is actually a JWS and signature validation fails
+     * @throws SecurityException        if the {@code jwt} string is actually a JWE and decryption fails
+     * @throws IllegalArgumentException if the {@code jwt} string is {@code null} or empty or only whitespace
      * @see #parseClaimsJwt(String)
-     * @see #parsePlaintextJws(String)
+     * @see #parseContentJws(String)
      * @see #parseClaimsJws(String)
      * @see #parse(String, JwtHandler)
      * @see #parse(String)
      * @since 0.2
      */
-    Jwt<Header, String> parsePlaintextJwt(String plaintextJwt)
-            throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
+    Jwt<UnprotectedHeader, byte[]> parseContentJwt(String jwt) throws UnsupportedJwtException, MalformedJwtException,
+            SignatureException, SecurityException, IllegalArgumentException;
 
     /**
      * Parses the specified compact serialized JWT string based on the builder's current configuration state and
-     * returns the resulting unsigned plaintext JWT instance.
+     * returns the resulting unprotected Claims JWT instance.
      *
      * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects an
-     * unsigned Claims JWT. An unsigned Claims JWT has a {@link Claims} body and it is not cryptographically
-     * signed.</p>
+     * unprotected Claims JWT. An unprotected Claims JWT has a {@link Claims} payload and it is not cryptographically
+     * signed or encrypted.</p>
      *
-     * <p><b>If the compact string presented does not reflect an unsigned Claims JWT, an
+     * <p><b>If the compact string presented does not reflect an unprotected Claims JWT, an
      * {@link UnsupportedJwtException} will be thrown.</b></p>
      *
-     * @param claimsJwt a compact serialized unsigned Claims JWT string.
+     * @param jwt a compact serialized unprotected Claims JWT string.
      * @return the {@link Jwt Jwt} instance that reflects the specified compact JWT string.
-     * @throws UnsupportedJwtException  if the {@code claimsJwt} argument does not represent an unsigned Claims JWT
-     * @throws MalformedJwtException    if the {@code claimsJwt} string is not a valid JWT
-     * @throws SignatureException       if the {@code claimsJwt} string is actually a JWS and signature validation
-     *                                  fails
+     * @throws UnsupportedJwtException  if the {@code jwt} argument does not represent an unprotected Claims JWT
+     * @throws MalformedJwtException    if the {@code jwt} string is not a valid JWT
+     * @throws SignatureException       if the {@code jwt} string is actually a JWS and signature validation fails
+     * @throws SecurityException        if the {@code jwt} string is actually a JWE and decryption fails
      * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
      *                                  before the time this method is invoked.
-     * @throws IllegalArgumentException if the {@code claimsJwt} string is {@code null} or empty or only whitespace
-     * @see #parsePlaintextJwt(String)
-     * @see #parsePlaintextJws(String)
+     * @throws IllegalArgumentException if the {@code jwt} string is {@code null} or empty or only whitespace
+     * @see #parseContentJwt(String)
+     * @see #parseContentJws(String)
      * @see #parseClaimsJws(String)
      * @see #parse(String, JwtHandler)
      * @see #parse(String)
      * @since 0.2
      */
-    Jwt<Header, Claims> parseClaimsJwt(String claimsJwt)
-            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
+    Jwt<UnprotectedHeader, Claims> parseClaimsJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException,
+            MalformedJwtException, SignatureException, SecurityException, IllegalArgumentException;
 
     /**
      * Parses the specified compact serialized JWS string based on the builder's current configuration state and
-     * returns the resulting plaintext JWS instance.
+     * returns the resulting content JWS instance. If the JWT creator set the (optional)
+     * {@link Header#getContentType() contentType} header value, the application may inspect that value to determine
+     * how to convert the byte array to the final content type as desired.
      *
      * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects a
-     * plaintext JWS. A plaintext JWS is a JWT with a String (non-JSON) body (payload) that has been
-     * cryptographically signed.</p>
+     * content JWS. A content JWS is a JWT with a byte array payload that has been cryptographically signed.</p>
      *
-     * <p><b>If the compact string presented does not reflect a plaintext JWS, an {@link UnsupportedJwtException}
+     * <p><b>If the compact string presented does not reflect a content JWS, an {@link UnsupportedJwtException}
      * will be thrown.</b></p>
      *
-     * @param plaintextJws a compact serialized JWS string.
+     * @param jws a compact serialized JWS string.
      * @return the {@link Jws Jws} instance that reflects the specified compact JWS string.
-     * @throws UnsupportedJwtException  if the {@code plaintextJws} argument does not represent an plaintext JWS
-     * @throws MalformedJwtException    if the {@code plaintextJws} string is not a valid JWS
-     * @throws SignatureException       if the {@code plaintextJws} JWS signature validation fails
-     * @throws IllegalArgumentException if the {@code plaintextJws} string is {@code null} or empty or only whitespace
-     * @see #parsePlaintextJwt(String)
+     * @throws UnsupportedJwtException  if the {@code jws} argument does not represent a content JWS
+     * @throws MalformedJwtException    if the {@code jws} string is not a valid JWS
+     * @throws SignatureException       if the {@code jws} JWS signature validation fails
+     * @throws SecurityException        if the {@code jws} string is actually a JWE and decryption fails
+     * @throws IllegalArgumentException if the {@code jws} string is {@code null} or empty or only whitespace
+     * @see #parseContentJwt(String)
+     * @see #parseContentJwe(String)
      * @see #parseClaimsJwt(String)
      * @see #parseClaimsJws(String)
+     * @see #parseClaimsJwe(String)
      * @see #parse(String, JwtHandler)
      * @see #parse(String)
      * @since 0.2
      */
-    Jws<String> parsePlaintextJws(String plaintextJws)
-            throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
+    Jws<byte[]> parseContentJws(String jws) throws UnsupportedJwtException, MalformedJwtException, SignatureException,
+            SecurityException, IllegalArgumentException;
 
     /**
      * Parses the specified compact serialized JWS string based on the builder's current configuration state and
      * returns the resulting Claims JWS instance.
      *
      * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects a
-     * Claims JWS. A Claims JWS is a JWT with a {@link Claims} body that has been cryptographically signed.</p>
+     * Claims JWS. A Claims JWS is a JWT with a {@link Claims} payload that has been cryptographically signed.</p>
      *
      * <p><b>If the compact string presented does not reflect a Claims JWS, an {@link UnsupportedJwtException} will be
      * thrown.</b></p>
      *
-     * @param claimsJws a compact serialized Claims JWS string.
+     * @param jws a compact serialized Claims JWS string.
      * @return the {@link Jws Jws} instance that reflects the specified compact Claims JWS string.
      * @throws UnsupportedJwtException  if the {@code claimsJws} argument does not represent an Claims JWS
      * @throws MalformedJwtException    if the {@code claimsJws} string is not a valid JWS
      * @throws SignatureException       if the {@code claimsJws} JWS signature validation fails
+     * @throws SecurityException        if the {@code jws} string is actually a JWE and decryption fails
      * @throws ExpiredJwtException      if the specified JWT is a Claims JWT and the Claims has an expiration time
      *                                  before the time this method is invoked.
      * @throws IllegalArgumentException if the {@code claimsJws} string is {@code null} or empty or only whitespace
-     * @see #parsePlaintextJwt(String)
+     * @see #parseContentJwt(String)
+     * @see #parseContentJws(String)
+     * @see #parseContentJwe(String)
      * @see #parseClaimsJwt(String)
-     * @see #parsePlaintextJws(String)
+     * @see #parseClaimsJwe(String)
      * @see #parse(String, JwtHandler)
      * @see #parse(String)
      * @since 0.2
      */
-    Jws<Claims> parseClaimsJws(String claimsJws)
-            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
+    Jws<Claims> parseClaimsJws(String jws) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
+            SignatureException, SecurityException, IllegalArgumentException;
+
+    /**
+     * Parses the specified compact serialized JWE string based on the builder's current configuration state and
+     * returns the resulting content JWE instance. If the JWT creator set the (optional)
+     * {@link Header#getContentType() contentType} header value, the application may inspect that value to determine
+     * how to convert the byte array to the final content type as desired.
+     *
+     * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects a
+     * content JWE. A content JWE is a JWT with a byte array payload that has been encrypted.</p>
+     *
+     * <p><b>If the compact string presented does not reflect a content JWE, an {@link UnsupportedJwtException}
+     * will be thrown.</b></p>
+     *
+     * @param jwe a compact serialized JWE string.
+     * @return the {@link Jwe Jwe} instance that reflects the specified compact JWE string.
+     * @throws UnsupportedJwtException  if the {@code jwe} argument does not represent a content JWE
+     * @throws MalformedJwtException    if the {@code jwe} string is not a valid JWE
+     * @throws SecurityException        if the {@code jwe} JWE decryption fails
+     * @throws IllegalArgumentException if the {@code jwe} string is {@code null} or empty or only whitespace
+     * @see #parseContentJwt(String)
+     * @see #parseContentJws(String)
+     * @see #parseClaimsJwt(String)
+     * @see #parseClaimsJws(String)
+     * @see #parseClaimsJwe(String)
+     * @see #parse(String, JwtHandler)
+     * @see #parse(String)
+     * @since JJWT_RELEASE_VERSION
+     */
+    Jwe<byte[]> parseContentJwe(String jwe) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
+            SecurityException, IllegalArgumentException;
+
+    /**
+     * Parses the specified compact serialized JWE string based on the builder's current configuration state and
+     * returns the resulting Claims JWE instance.
+     *
+     * <p>This is a convenience method that is usable if you are confident that the compact string argument reflects a
+     * Claims JWE. A Claims JWE is a JWT with a {@link Claims} payload that has been encrypted.</p>
+     *
+     * <p><b>If the compact string presented does not reflect a Claims JWE, an {@link UnsupportedJwtException} will be
+     * thrown.</b></p>
+     *
+     * @param jwe a compact serialized Claims JWE string.
+     * @return the {@link Jwe Jwe} instance that reflects the specified compact Claims JWE string.
+     * @throws UnsupportedJwtException  if the {@code claimsJwe} argument does not represent a Claims JWE
+     * @throws MalformedJwtException    if the {@code claimsJwe} string is not a valid JWE
+     * @throws SignatureException       if the {@code claimsJwe} JWE decryption fails
+     * @throws ExpiredJwtException      if the specified JWT is a Claims JWE and the Claims has an expiration time
+     *                                  before the time this method is invoked.
+     * @throws IllegalArgumentException if the {@code claimsJwe} string is {@code null} or empty or only whitespace
+     * @see #parseContentJwt(String)
+     * @see #parseContentJws(String)
+     * @see #parseContentJwe(String)
+     * @see #parseClaimsJwt(String)
+     * @see #parseClaimsJws(String)
+     * @see #parse(String, JwtHandler)
+     * @see #parse(String)
+     * @since JJWT_RELEASE_VERSION
+     */
+    Jwe<Claims> parseClaimsJwe(String jwe) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
+            SecurityException, IllegalArgumentException;
 }

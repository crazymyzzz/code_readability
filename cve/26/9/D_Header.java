@@ -32,103 +32,122 @@ import java.util.Map;
  *
  * <h2>Creation</h2>
  *
- * <p>It is easiest to create a {@code Header} instance by calling one of the
- * {@link Jwts#header() JWTs.header()} factory methods.</p>
+ * <p>It is easiest to create a {@code Header} instance by using {@link Jwts#header()}.</p>
  *
  * @since 0.1
  */
-public interface Header<T extends Header<T>> extends Map<String,Object> {
+public interface Header<T extends Header<T>> extends Map<String, Object>, HeaderMutator<T> {
 
-    /** JWT {@code Type} (typ) value: <code>"JWT"</code> */
-    public static final String JWT_TYPE = "JWT";
+    /**
+     * JWT {@code Type} (typ) value: <code>"JWT"</code>
+     *
+     * @deprecated since JJWT_RELEASE_VERSION - this constant is never used within the JJWT codebase.
+     */
+    @Deprecated
+    String JWT_TYPE = "JWT";
 
-    /** JWT {@code Type} header parameter name: <code>"typ"</code> */
-    public static final String TYPE = "typ";
+    /**
+     * JWT {@code Type} header parameter name: <code>"typ"</code>
+     */
+    String TYPE = "typ";
 
-    /** JWT {@code Content Type} header parameter name: <code>"cty"</code> */
-    public static final String CONTENT_TYPE = "cty";
+    /**
+     * JWT {@code Content Type} header parameter name: <code>"cty"</code>
+     */
+    String CONTENT_TYPE = "cty";
 
-    /** JWT {@code Compression Algorithm} header parameter name: <code>"zip"</code> */
-    public static final String COMPRESSION_ALGORITHM = "zip";
+    /**
+     * JWT {@code Algorithm} header parameter name: <code>"alg"</code>.
+     *
+     * @see <a href="https://tools.ietf.org/html/rfc7515#section-4.1.1">JWS Algorithm Header</a>
+     * @see <a href="https://tools.ietf.org/html/rfc7516#section-4.1.1">JWE Algorithm Header</a>
+     */
+    String ALGORITHM = "alg";
 
-    /** JJWT legacy/deprecated compression algorithm header parameter name: <code>"calg"</code>
-     * @deprecated use {@link #COMPRESSION_ALGORITHM} instead. */
-    @Deprecated
-    public static final String DEPRECATED_COMPRESSION_ALGORITHM = "calg";
+    /**
+     * JWT {@code Compression Algorithm} header parameter name: <code>"zip"</code>
+     */
+    String COMPRESSION_ALGORITHM = "zip";
 
     /**
-     * Returns the <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-5.1">
-     * <code>typ</code></a> (type) header value or {@code null} if not present.
+     * JJWT legacy/deprecated compression algorithm header parameter name: <code>"calg"</code>
      *
-     * @return the {@code typ} header value or {@code null} if not present.
+     * @deprecated use {@link #COMPRESSION_ALGORITHM} instead.
      */
-    String getType();
+    @SuppressWarnings("DeprecatedIsStillUsed")
+    @Deprecated
+    String DEPRECATED_COMPRESSION_ALGORITHM = "calg";
 
     /**
-     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-5.1">
-     * <code>typ</code></a> (Type) header value.  A {@code null} value will remove the property from the JSON map.
+     * Returns the <a href="https://www.rfc-editor.org/rfc/rfc7519.html#section-5.1">
+     * <code>typ</code> (Type)</a> header value or {@code null} if not present.
      *
-     * @param typ the JWT JOSE {@code typ} header value or {@code null} to remove the property from the JSON map.
-     * @return the {@code Header} instance for method chaining.
+     * @return the {@code typ} header value or {@code null} if not present.
      */
-    T setType(String typ);
+    String getType();
 
     /**
-     * Returns the <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-5.2">
-     * <code>cty</code></a> (Content Type) header value or {@code null} if not present.
+     * Returns the <a href="https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.10">
+     * <code>cty</code> (Content Type)</a> header value or {@code null} if not present.
      *
-     * <p>In the normal case where nested signing or encryption operations are not employed (i.e. a compact
-     * serialization JWT), the use of this header parameter is NOT RECOMMENDED.  In the case that nested
-     * signing or encryption is employed, this Header Parameter MUST be present; in this case, the value MUST be
-     * {@code JWT}, to indicate that a Nested JWT is carried in this JWT.  While media type names are not
-     * case-sensitive, it is RECOMMENDED that {@code JWT} always be spelled using uppercase characters for
-     * compatibility with legacy implementations.  See
-     * <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#appendix-A.2">JWT Appendix A.2</a> for
-     * an example of a Nested JWT.</p>
+     * <p>The <code>cty</code> (Content Type) Header Parameter is used by applications to declare the
+     * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">IANA MediaType</a> of the content
+     * (the payload).  This is intended for use by the application when more than
+     * one kind of object could be present in the Payload; the application can use this value to disambiguate among
+     * the different kinds of objects that might be present.  It will typically not be used by applications when
+     * the kind of object is already known.  This parameter is ignored by JWT implementations (like JJWT); any
+     * processing of this parameter is performed by the JWS application.  Use of this Header Parameter is OPTIONAL.</p>
+     *
+     * <p>To keep messages compact in common situations, it is RECOMMENDED that producers omit an
+     * <b><code>application/</code></b> prefix of a media type value in a {@code cty} Header Parameter when
+     * no other '<b>/</b>' appears in the media type value.  A recipient using the media type value <em>MUST</em>
+     * treat it as if <b><code>application/</code></b> were prepended to any {@code cty} value not containing a
+     * '<b>/</b>'. For instance, a {@code cty} value of <b><code>example</code></b> <em>SHOULD</em> be used to
+     * represent the <b><code>application/example</code></b> media type, whereas the media type
+     * <b><code>application/example;part=&quot;1/2&quot;</code></b> cannot be shortened to
+     * <b><code>example;part=&quot;1/2&quot;</code></b>.</p>
      *
      * @return the {@code typ} header parameter value or {@code null} if not present.
      */
     String getContentType();
 
     /**
-     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-5.2">
-     * <code>cty</code></a> (Content Type) header parameter value.  A {@code null} value will remove the property from
-     * the JSON map.
+     * Returns the JWT {@code alg} (Algorithm) header value or {@code null} if not present.
      *
-     * <p>In the normal case where nested signing or encryption operations are not employed (i.e. a compact
-     * serialization JWT), the use of this header parameter is NOT RECOMMENDED.  In the case that nested
-     * signing or encryption is employed, this Header Parameter MUST be present; in this case, the value MUST be
-     * {@code JWT}, to indicate that a Nested JWT is carried in this JWT.  While media type names are not
-     * case-sensitive, it is RECOMMENDED that {@code JWT} always be spelled using uppercase characters for
-     * compatibility with legacy implementations.  See
-     * <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#appendix-A.2">JWT Appendix A.2</a> for
-     * an example of a Nested JWT.</p>
+     * <ul>
+     *     <li>If the JWT is a Signed JWT (a JWS), the <a href="https://tools.ietf.org/html/rfc7515#section-4.1.1">
+     *      <code>alg</code></a> (Algorithm) header parameter identifies the cryptographic algorithm used to secure the
+     *      JWS.  Consider using {@link Jwts#SIG}.{@link io.jsonwebtoken.lang.Registry#find(Object) find(id)}
+     *      to convert this string value to a type-safe {@code SecureDigestAlgorithm} instance.</li>
+     *      <li>If the JWT is an Encrypted JWT (a JWE), the
+     * <a href="https://tools.ietf.org/html/rfc7516#section-4.1.1"><code>alg</code></a> (Algorithm) header parameter
+     * identifies the cryptographic key management algorithm used to encrypt or determine the value of the Content
+     * Encryption Key (CEK).  The encrypted content is not usable if the <code>alg</code> value does not represent a
+     * supported algorithm, or if the recipient does not have a key that can be used with that algorithm.  Consider
+     * using {@link Jwts#KEY}.{@link io.jsonwebtoken.lang.Registry#find(Object) find(id)} to convert this string value
+     * to a type-safe {@link io.jsonwebtoken.security.KeyAlgorithm KeyAlgorithm} instance.</li>
+     * </ul>
      *
-     * @param cty the JWT JOSE {@code cty} header value or {@code null} to remove the property from the JSON map.
-     * @return the {@code Header} instance for method chaining.
+     * @return the {@code alg} header value or {@code null} if not present.  This will always be
+     * {@code non-null} on validly constructed JWT instances, but could be {@code null} during construction.
+     * @since JJWT_RELEASE_VERSION
      */
-    T setContentType(String cty);
+    String getAlgorithm();
 
     /**
-     * Returns the JWT <code>zip</code> (Compression Algorithm) header value or {@code null} if not present.
+     * Returns the JWT  <a href="https://tools.ietf.org/html/rfc7516#section-4.1.3"><code>zip</code></a>
+     * (Compression Algorithm) header parameter value or {@code null} if not present.
      *
-     * @return the {@code zip} header parameter value or {@code null} if not present.
-     * @since 0.6.0
-     */
-    String getCompressionAlgorithm();
-
-    /**
-     * Sets the JWT <code>zip</code> (Compression Algorithm) header parameter value. A {@code null} value will remove
-     * the property from the JSON map.
+     * <p><b>Compatibility Note</b></p>
      *
-     * <p>The compression algorithm is NOT part of the <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25">JWT specification</a>
-     * and must be used carefully since, is not expected that other libraries (including previous versions of this one)
-     * be able to deserialize a compressed JTW body correctly. </p>
+     * <p>While the JWT family of specifications only defines the <code>zip</code> header in the JWE
+     * (JSON Web Encryption) specification, JJWT will also support compression for JWS as well if you choose to use it.
+     * However, be aware that <b>if you use compression when creating a JWS token, other libraries may not be able to
+     * parse the JWS</b>. However, compression when creating JWE tokens should be universally accepted for any library
+     * that supports JWE.</p>
      *
-     * @param zip the JWT compression algorithm {@code zip} value or {@code null} to remove the property from the JSON map.
-     * @return the {@code Header} instance for method chaining.
+     * @return the {@code zip} header parameter value or {@code null} if not present.
      * @since 0.6.0
      */
-    T setCompressionAlgorithm(String zip);
-
+    String getCompressionAlgorithm();
 }

@@ -16,92 +16,54 @@
 package io.jsonwebtoken;
 
 /**
- * A <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-31">JWS</a> header.
+ * A <a href="https://tools.ietf.org/html/rfc7515">JWS</a> header.
  *
- * @param <T> header type
  * @since 0.1
  */
-public interface JwsHeader<T extends JwsHeader<T>> extends Header<T> {
+public interface JwsHeader extends ProtectedHeader<JwsHeader> {
 
-    /** JWS {@code Algorithm} header parameter name: <code>"alg"</code> */
-    public static final String ALGORITHM = "alg";
-
-    /** JWS {@code JWT Set URL} header parameter name: <code>"jku"</code> */
-    public static final String JWK_SET_URL = "jku";
-
-    /** JWS {@code JSON Web Key} header parameter name: <code>"jwk"</code> */
-    public static final String JSON_WEB_KEY = "jwk";
-
-    /** JWS {@code Key ID} header parameter name: <code>"kid"</code> */
-    public static final String KEY_ID = "kid";
-
-    /** JWS {@code X.509 URL} header parameter name: <code>"x5u"</code> */
-    public static final String X509_URL = "x5u";
+    /**
+     * JWS <a href="https://tools.ietf.org/html/rfc7515#section-4.1.1">Algorithm Header</a> name: the string literal <b><code>alg</code></b>
+     */
+    String ALGORITHM = "alg";
 
-    /** JWS {@code X.509 Certificate Chain} header parameter name: <code>"x5c"</code> */
-    public static final String X509_CERT_CHAIN = "x5c";
+    /**
+     * JWS <a href="https://tools.ietf.org/html/rfc7515#section-4.1.2">JWK Set URL Header</a> name: the string literal <b><code>jku</code></b>
+     */
+    String JWK_SET_URL = "jku";
 
-    /** JWS {@code X.509 Certificate SHA-1 Thumbprint} header parameter name: <code>"x5t"</code> */
-    public static final String X509_CERT_SHA1_THUMBPRINT = "x5t";
+    /**
+     * JWS <a href="https://tools.ietf.org/html/rfc7515#section-4.1.3">JSON Web Key Header</a> name: the string literal <b><code>jwk</code></b>
+     */
+    String JSON_WEB_KEY = "jwk";
 
-    /** JWS {@code X.509 Certificate SHA-256 Thumbprint} header parameter name: <code>"x5t#S256"</code> */
-    public static final String X509_CERT_SHA256_THUMBPRINT = "x5t#S256";
+    /**
+     * JWS <a href="https://tools.ietf.org/html/rfc7516#section-4.1.4">Key ID Header</a> name: the string literal <b><code>kid</code></b>
+     */
+    String KEY_ID = "kid";
 
-    /** JWS {@code Critical} header parameter name: <code>"crit"</code> */
-    public static final String CRITICAL = "crit";
+    /**
+     * JWS <a href="https://tools.ietf.org/html/rfc7516#section-4.1.5">X.509 URL Header</a> name: the string literal <b><code>x5u</code></b>
+     */
+    String X509_URL = "x5u";
 
     /**
-     * Returns the JWS <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-31#section-4.1.1">
-     * <code>alg</code></a> (algorithm) header value or {@code null} if not present.
-     *
-     * <p>The algorithm header parameter identifies the cryptographic algorithm used to secure the JWS.  Consider
-     * using {@link io.jsonwebtoken.SignatureAlgorithm#forName(String) SignatureAlgorithm.forName} to convert this
-     * string value to a type-safe enum instance.</p>
-     *
-     * @return the JWS {@code alg} header value or {@code null} if not present.  This will always be
-     * {@code non-null} on validly constructed JWS instances, but could be {@code null} during construction.
+     * JWS <a href="https://tools.ietf.org/html/rfc7516#section-4.1.6">X.509 Certificate Chain Header</a> name: the string literal <b><code>x5c</code></b>
      */
-    String getAlgorithm();
+    String X509_CERT_CHAIN = "x5c";
 
     /**
-     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-31#section-4.1.1">
-     * <code>alg</code></a> (Algorithm) header value.  A {@code null} value will remove the property from the JSON map.
-     *
-     * <p>The algorithm header parameter identifies the cryptographic algorithm used to secure the JWS.  Consider
-     * using a type-safe {@link io.jsonwebtoken.SignatureAlgorithm SignatureAlgorithm} instance and using its
-     * {@link io.jsonwebtoken.SignatureAlgorithm#getValue() value} as the argument to this method.</p>
-     *
-     * @param alg the JWS {@code alg} header value or {@code null} to remove the property from the JSON map.
-     * @return the {@code Header} instance for method chaining.
+     * JWS <a href="https://tools.ietf.org/html/rfc7516#section-4.1.7">X.509 Certificate SHA-1 Thumbprint Header</a> name: the string literal <b><code>x5t</code></b>
      */
-    T setAlgorithm(String alg);
+    String X509_CERT_SHA1_THUMBPRINT = "x5t";
 
     /**
-     * Returns the JWS <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-31#section-4.1.4">
-     * <code>kid</code></a> (Key ID) header value or {@code null} if not present.
-     *
-     * <p>The keyId header parameter is a hint indicating which key was used to secure the JWS.  This parameter allows
-     * originators to explicitly signal a change of key to recipients.  The structure of the keyId value is
-     * unspecified.</p>
-     *
-     * <p>When used with a JWK, the keyId value is used to match a JWK {@code keyId} parameter value.</p>
-     *
-     * @return the JWS {@code kid} header value or {@code null} if not present.
+     * JWS <a href="https://tools.ietf.org/html/rfc7516#section-4.1.8">X.509 Certificate SHA-256 Thumbprint Header</a> name: the string literal <b><code>x5t#S256</code></b>
      */
-    String getKeyId();
+    String X509_CERT_SHA256_THUMBPRINT = "x5t#S256";
 
     /**
-     * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-31#section-4.1.4">
-     * <code>kid</code></a> (Key ID) header value.  A {@code null} value will remove the property from the JSON map.
-     *
-     * <p>The keyId header parameter is a hint indicating which key was used to secure the JWS.  This parameter allows
-     * originators to explicitly signal a change of key to recipients.  The structure of the keyId value is
-     * unspecified.</p>
-     *
-     * <p>When used with a JWK, the keyId value is used to match a JWK {@code keyId} parameter value.</p>
-     *
-     * @param kid the JWS {@code kid} header value or {@code null} to remove the property from the JSON map.
-     * @return the {@code Header} instance for method chaining.
+     * JWS <a href="https://tools.ietf.org/html/rfc7516#section-4.1.11">Critical Header</a> name: the string literal <b><code>crit</code></b>
      */
-    T setKeyId(String kid);
+    String CRITICAL = "crit";
 }

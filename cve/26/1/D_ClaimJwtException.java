@@ -16,36 +16,83 @@
 package io.jsonwebtoken;
 
 /**
- * ClaimJwtException is a subclass of the {@link JwtException} that is thrown after a validation of an JTW claim failed.
+ * ClaimJwtException is a subclass of the {@link JwtException} that is thrown after a validation of an JWT claim failed.
  *
  * @since 0.5
  */
 public abstract class ClaimJwtException extends JwtException {
 
+    /**
+     * Deprecated as this is an implementation detail accidentally exposed in the JJWT 0.5 public API. It is no
+     * longer referenced anywhere in JJWT's implementation and will be removed in a future release.
+     *
+     * @deprecated will be removed in a future release.
+     */
+    @Deprecated
     public static final String INCORRECT_EXPECTED_CLAIM_MESSAGE_TEMPLATE = "Expected %s claim to be: %s, but was: %s.";
+
+    /**
+     * Deprecated as this is an implementation detail accidentally exposed in the JJWT 0.5 public API. It is no
+     * longer referenced anywhere in JJWT's implementation and will be removed in a future release.
+     *
+     * @deprecated will be removed in a future release.
+     */
+    @Deprecated
     public static final String MISSING_EXPECTED_CLAIM_MESSAGE_TEMPLATE = "Expected %s claim to be: %s, but was not present in the JWT claims.";
 
-    private final Header header;
+    /**
+     * The header associated with the Claims that failed validation.
+     */
+    private final Header<?> header;
 
+    /**
+     * The Claims that failed validation.
+     */
     private final Claims claims;
 
-    protected ClaimJwtException(Header header, Claims claims, String message) {
+    /**
+     * Creates a new instance with the specified header, claims and exception message.
+     *
+     * @param header  the header inspected
+     * @param claims  the claims obtained
+     * @param message the exception message
+     */
+    protected ClaimJwtException(Header<?> header, Claims claims, String message) {
         super(message);
         this.header = header;
         this.claims = claims;
     }
 
-    protected ClaimJwtException(Header header, Claims claims, String message, Throwable cause) {
+    /**
+     * Creates a new instance with the specified header, claims and exception message as a result of encountering
+     * the specified {@code cause}.
+     *
+     * @param header  the header inspected
+     * @param claims  the claims obtained
+     * @param message the exception message
+     * @param cause   the exception that caused this ClaimJwtException to be thrown.
+     */
+    protected ClaimJwtException(Header<?> header, Claims claims, String message, Throwable cause) {
         super(message, cause);
         this.header = header;
         this.claims = claims;
     }
 
+    /**
+     * Returns the {@link Claims} that failed validation.
+     *
+     * @return the {@link Claims} that failed validation.
+     */
     public Claims getClaims() {
         return claims;
     }
 
-    public Header getHeader() {
+    /**
+     * Returns the header associated with the {@link #getClaims() claims} that failed validation.
+     *
+     * @return the header associated with the {@link #getClaims() claims} that failed validation.
+     */
+    public Header<?> getHeader() {
         return header;
     }
 }

@@ -21,35 +21,66 @@ package io.jsonwebtoken;
  *
  * @see IncorrectClaimException
  * @see MissingClaimException
- *
  * @since 0.6
  */
 public class InvalidClaimException extends ClaimJwtException {
 
-    private String claimName;
-    private Object claimValue;
+    /**
+     * The name of the invalid claim.
+     */
+    private final String claimName;
+
+    /**
+     * The claim value that could not be validated.
+     */
+    private final Object claimValue;
 
-    protected InvalidClaimException(Header header, Claims claims, String message) {
+    /**
+     * Creates a new instance with the specified header, claims and explanation message.
+     *
+     * @param header     the header inspected
+     * @param claims     the claims obtained
+     * @param claimName  the name of the claim that could not be validated
+     * @param claimValue the value of the claim that could not be validated
+     * @param message    the exception message
+     */
+    protected InvalidClaimException(Header<?> header, Claims claims, String claimName, Object claimValue, String message) {
         super(header, claims, message);
+        this.claimName = claimName;
+        this.claimValue = claimValue;
     }
 
-    protected InvalidClaimException(Header header, Claims claims, String message, Throwable cause) {
+    /**
+     * Creates a new instance with the specified header, claims, explanation message and underlying cause.
+     *
+     * @param header     the header inspected
+     * @param claims     the claims obtained
+     * @param claimName  the name of the claim that could not be validated
+     * @param claimValue the value of the claim that could not be validated
+     * @param message    the exception message
+     * @param cause      the underlying cause that resulted in this exception being thrown
+     */
+    protected InvalidClaimException(Header<?> header, Claims claims, String claimName, Object claimValue, String message, Throwable cause) {
         super(header, claims, message, cause);
+        this.claimName = claimName;
+        this.claimValue = claimValue;
     }
 
+    /**
+     * Returns the name of the invalid claim.
+     *
+     * @return the name of the invalid claim.
+     */
     public String getClaimName() {
         return claimName;
     }
 
-    public void setClaimName(String claimName) {
-        this.claimName = claimName;
-    }
-
+    /**
+     * Returns the claim value that could not be validated.
+     *
+     * @return the claim value that could not be validated.
+     */
     public Object getClaimValue() {
         return claimValue;
     }
-
-    public void setClaimValue(Object claimValue) {
-        this.claimValue = claimValue;
-    }
 }

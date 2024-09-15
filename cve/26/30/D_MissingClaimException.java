@@ -22,11 +22,32 @@ package io.jsonwebtoken;
  * @since 0.6
  */
 public class MissingClaimException extends InvalidClaimException {
-    public MissingClaimException(Header header, Claims claims, String message) {
-        super(header, claims, message);
+
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param header     the header associated with the claims that did not contain the required claim
+     * @param claims     the claims that did not contain the required claim
+     * @param claimName  the name of the claim that could not be validated
+     * @param claimValue the value of the claim that could not be validated
+     * @param message    the message explaining why the exception is thrown.
+     */
+    public MissingClaimException(Header<?> header, Claims claims, String claimName, Object claimValue, String message) {
+        super(header, claims, claimName, claimValue, message);
     }
 
-    public MissingClaimException(Header header, Claims claims, String message, Throwable cause) {
-        super(header, claims, message, cause);
+
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param header     the header associated with the claims that did not contain the required claim
+     * @param claims     the claims that did not contain the required claim
+     * @param claimName  the name of the claim that could not be validated
+     * @param claimValue the value of the claim that could not be validated
+     * @param message    the message explaining why the exception is thrown.
+     * @param cause      the underlying cause that resulted in this exception being thrown.
+     */
+    public MissingClaimException(Header<?> header, Claims claims, String claimName, Object claimValue, String message, Throwable cause) {
+        super(header, claims, claimName, claimValue, message, cause);
     }
 }

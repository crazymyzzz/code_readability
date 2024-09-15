@@ -23,11 +23,30 @@ package io.jsonwebtoken;
  */
 public class IncorrectClaimException extends InvalidClaimException {
 
-    public IncorrectClaimException(Header header, Claims claims, String message) {
-        super(header, claims, message);
+    /**
+     * Creates a new instance with the specified header, claims and explanation message.
+     *
+     * @param header     the header inspected
+     * @param claims     the claims with the incorrect claim value
+     * @param claimName  the name of the claim that could not be validated
+     * @param claimValue the value of the claim that could not be validated
+     * @param message    the exception message
+     */
+    public IncorrectClaimException(Header<?> header, Claims claims, String claimName, Object claimValue, String message) {
+        super(header, claims, claimName, claimValue, message);
     }
 
-    public IncorrectClaimException(Header header, Claims claims, String message, Throwable cause) {
-        super(header, claims, message, cause);
+    /**
+     * Creates a new instance with the specified header, claims, explanation message and underlying cause.
+     *
+     * @param header     the header inspected
+     * @param claims     the claims with the incorrect claim value
+     * @param claimName  the name of the claim that could not be validated
+     * @param claimValue the value of the claim that could not be validated
+     * @param message    the exception message
+     * @param cause      the underlying cause that resulted in this exception being thrown
+     */
+    public IncorrectClaimException(Header<?> header, Claims claims, String claimName, Object claimValue, String message, Throwable cause) {
+        super(header, claims, claimName, claimValue, message, cause);
     }
 }

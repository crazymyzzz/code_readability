@@ -19,17 +19,28 @@ package io.jsonwebtoken;
  * Exception thrown when receiving a JWT in a particular format/configuration that does not match the format expected
  * by the application.
  *
- * <p>For example, this exception would be thrown if parsing an unsigned plaintext JWT when the application
+ * <p>For example, this exception would be thrown if parsing an unprotected content JWT when the application
  * requires a cryptographically signed Claims JWS instead.</p>
  *
  * @since 0.2
  */
 public class UnsupportedJwtException extends JwtException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public UnsupportedJwtException(String message) {
         super(message);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public UnsupportedJwtException(String message, Throwable cause) {
         super(message, cause);
     }

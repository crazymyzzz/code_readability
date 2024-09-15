@@ -21,15 +21,26 @@ import io.jsonwebtoken.security.SecurityException;
  * Exception indicating that either calculating a signature or verifying an existing signature of a JWT failed.
  *
  * @since 0.1
- * @deprecated in favor of {@link io.jsonwebtoken.security.SecurityException}; this class will be removed before 1.0
+ * @deprecated in favor of {@link io.jsonwebtoken.security.SignatureException}; this class will be removed before 1.0
  */
 @Deprecated
 public class SignatureException extends SecurityException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public SignatureException(String message) {
         super(message);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public SignatureException(String message, Throwable cause) {
         super(message, cause);
     }

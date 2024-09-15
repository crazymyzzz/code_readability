@@ -16,14 +16,28 @@
 package io.jsonwebtoken.security;
 
 /**
+ * Exception thrown if there is problem calculating or verifying a digital signature or message authentication code.
+ *
  * @since 0.10.0
  */
+@SuppressWarnings("deprecation")
 public class SignatureException extends io.jsonwebtoken.SignatureException {
 
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

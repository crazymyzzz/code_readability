@@ -18,14 +18,28 @@ package io.jsonwebtoken.security;
 import io.jsonwebtoken.JwtException;
 
 /**
+ * A {@code JwtException} attributed to a problem with security-related elements, such as
+ * cryptographic keys, algorithms, or the underlying Java JCA API.
+ *
  * @since 0.10.0
  */
 public class SecurityException extends JwtException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public SecurityException(String message) {
         super(message);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public SecurityException(String message, Throwable cause) {
         super(message, cause);
     }

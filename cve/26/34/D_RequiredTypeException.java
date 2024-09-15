@@ -16,16 +16,28 @@
 package io.jsonwebtoken;
 
 /**
- * Exception thrown when {@link Claims#get(String, Class)} is called and the value does not match the type of the
- * {@code Class} argument.
+ * Exception thrown when attempting to obtain a value from a JWT or JWK and the existing value does not match the
+ * expected type.
  *
  * @since 0.6
  */
 public class RequiredTypeException extends JwtException {
+
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public RequiredTypeException(String message) {
         super(message);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public RequiredTypeException(String message, Throwable cause) {
         super(message, cause);
     }

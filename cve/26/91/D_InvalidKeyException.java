@@ -16,11 +16,30 @@
 package io.jsonwebtoken.security;
 
 /**
+ * A {@code KeyException} thrown when encountering a key that is not suitable for the required functionality, or
+ * when attempting to use a Key in an incorrect or prohibited manner.
+ *
  * @since 0.10.0
  */
 public class InvalidKeyException extends KeyException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public InvalidKeyException(String message) {
         super(message);
     }
+
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     * @since JJWT_RELEASE_VERSION
+     */
+    public InvalidKeyException(String message, Throwable cause) {
+        super(message, cause);
+    }
 }

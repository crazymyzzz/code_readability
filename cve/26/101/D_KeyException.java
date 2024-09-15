@@ -16,11 +16,29 @@
 package io.jsonwebtoken.security;
 
 /**
+ * General-purpose exception when encountering a problem with a cryptographic {@link java.security.Key}
+ * or {@link Jwk}.
+ *
  * @since 0.10.0
  */
 public class KeyException extends SecurityException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public KeyException(String message) {
         super(message);
     }
+
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param msg   the message explaining why the exception is thrown.
+     * @param cause the underlying cause that resulted in this exception being thrown.
+     */
+    public KeyException(String msg, Throwable cause) {
+        super(msg, cause);
+    }
 }

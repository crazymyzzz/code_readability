@@ -16,10 +16,18 @@
 package io.jsonwebtoken.security;
 
 /**
+ * Exception thrown when encountering a key that is not strong enough (of sufficient length) to be used with
+ * a particular algorithm or in a particular security context.
+ *
  * @since 0.10.0
  */
 public class WeakKeyException extends InvalidKeyException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public WeakKeyException(String message) {
         super(message);
     }

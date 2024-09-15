@@ -16,14 +16,27 @@
 package io.jsonwebtoken.io;
 
 /**
+ * Exception thrown when reconstituting a serialized byte array into a Java object.
+ *
  * @since 0.10.0
  */
 public class DeserializationException extends SerialException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param msg the message explaining why the exception is thrown.
+     */
     public DeserializationException(String msg) {
         super(msg);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public DeserializationException(String message, Throwable cause) {
         super(message, cause);
     }

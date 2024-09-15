@@ -16,14 +16,27 @@
 package io.jsonwebtoken.io;
 
 /**
+ * Exception thrown when converting a Java object to a formatted byte array.
+ *
  * @since 0.10.0
  */
 public class SerializationException extends SerialException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param msg the message explaining why the exception is thrown.
+     */
     public SerializationException(String msg) {
         super(msg);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public SerializationException(String message, Throwable cause) {
         super(message, cause);
     }

@@ -16,14 +16,27 @@
 package io.jsonwebtoken.io;
 
 /**
+ * An exception thrown during serialization or deserialization.
+ *
  * @since 0.10.0
  */
 public class SerialException extends IOException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param msg the message explaining why the exception is thrown.
+     */
     public SerialException(String msg) {
         super(msg);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public SerialException(String message, Throwable cause) {
         super(message, cause);
     }

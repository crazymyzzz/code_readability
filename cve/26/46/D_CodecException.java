@@ -16,14 +16,27 @@
 package io.jsonwebtoken.io;
 
 /**
+ * An exception thrown when encountering a problem during encoding or decoding.
+ *
  * @since 0.10.0
  */
 public class CodecException extends IOException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public CodecException(String message) {
         super(message);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public CodecException(String message, Throwable cause) {
         super(message, cause);
     }

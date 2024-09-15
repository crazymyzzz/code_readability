@@ -16,10 +16,18 @@
 package io.jsonwebtoken.io;
 
 /**
+ * An exception thrown when encountering a problem during encoding.
+ *
  * @since 0.10.0
  */
 public class EncodingException extends CodecException {
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public EncodingException(String message, Throwable cause) {
         super(message, cause);
     }

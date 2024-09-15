@@ -18,14 +18,28 @@ package io.jsonwebtoken.io;
 import io.jsonwebtoken.JwtException;
 
 /**
+ * JJWT's base exception for problems during data input or output operations, such as serialization,
+ * deserialization, marshalling, unmarshalling, etc.
+ *
  * @since 0.10.0
  */
 public class IOException extends JwtException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param msg the message explaining why the exception is thrown.
+     */
     public IOException(String msg) {
         super(msg);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public IOException(String message, Throwable cause) {
         super(message, cause);
     }

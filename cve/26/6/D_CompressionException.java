@@ -15,17 +15,30 @@
  */
 package io.jsonwebtoken;
 
+import io.jsonwebtoken.io.IOException;
+
 /**
- * Exception indicating that either compressing or decompressing an JWT body failed.
+ * Exception indicating that either compressing or decompressing a JWT body failed.
  *
  * @since 0.6.0
  */
-public class CompressionException extends JwtException {
+public class CompressionException extends IOException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public CompressionException(String message) {
         super(message);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public CompressionException(String message, Throwable cause) {
         super(message, cause);
     }

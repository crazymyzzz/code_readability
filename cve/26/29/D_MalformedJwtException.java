@@ -22,10 +22,21 @@ package io.jsonwebtoken;
  */
 public class MalformedJwtException extends JwtException {
 
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param message the message explaining why the exception is thrown.
+     */
     public MalformedJwtException(String message) {
         super(message);
     }
 
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
     public MalformedJwtException(String message, Throwable cause) {
         super(message, cause);
     }

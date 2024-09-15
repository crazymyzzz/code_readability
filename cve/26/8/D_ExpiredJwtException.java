@@ -22,18 +22,27 @@ package io.jsonwebtoken;
  */
 public class ExpiredJwtException extends ClaimJwtException {
 
-    public ExpiredJwtException(Header header, Claims claims, String message) {
+    /**
+     * Creates a new instance with the specified header, claims, and explanation message.
+     *
+     * @param header  jwt header
+     * @param claims  jwt claims (body)
+     * @param message the message explaining why the exception is thrown.
+     */
+    public ExpiredJwtException(Header<?> header, Claims claims, String message) {
         super(header, claims, message);
     }
 
     /**
-     * @param header jwt header
-     * @param claims jwt claims (body)
-     * @param message exception message
-     * @param cause cause
+     * Creates a new instance with the specified header, claims, explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     * @param header  jwt header
+     * @param claims  jwt claims (body)
      * @since 0.5
      */
-    public ExpiredJwtException(Header header, Claims claims, String message, Throwable cause) {
+    public ExpiredJwtException(Header<?> header, Claims claims, String message, Throwable cause) {
         super(header, claims, message, cause);
     }
 }

@@ -22,18 +22,27 @@ package io.jsonwebtoken;
  */
 public class PrematureJwtException extends ClaimJwtException {
 
-    public PrematureJwtException(Header header, Claims claims, String message) {
+    /**
+     * Creates a new instance with the specified explanation message.
+     *
+     * @param header  jwt header
+     * @param claims  jwt claims (body)
+     * @param message the message explaining why the exception is thrown.
+     */
+    public PrematureJwtException(Header<?> header, Claims claims, String message) {
         super(header, claims, message);
     }
 
     /**
-     * @param header jwt header
-     * @param claims jwt claims (body)
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param header  jwt header
+     * @param claims  jwt claims (body)
      * @param message exception message
-     * @param cause cause
+     * @param cause   cause
      * @since 0.5
      */
-    public PrematureJwtException(Header header, Claims claims, String message, Throwable cause) {
+    public PrematureJwtException(Header<?> header, Claims claims, String message, Throwable cause) {
         super(header, claims, message, cause);
     }
 }

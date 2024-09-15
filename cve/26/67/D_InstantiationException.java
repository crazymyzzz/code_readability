@@ -16,11 +16,19 @@
 package io.jsonwebtoken.lang;
 
 /**
+ * {@link RuntimeException} equivalent of {@link java.lang.InstantiationException}.
+ *
  * @since 0.1
  */
 public class InstantiationException extends RuntimeException {
 
-    public InstantiationException(String s, Throwable t) {
-        super(s, t);
+    /**
+     * Creates a new instance with the specified explanation message and underlying cause.
+     *
+     * @param message the message explaining why the exception is thrown.
+     * @param cause   the underlying cause that resulted in this exception being thrown.
+     */
+    public InstantiationException(String message, Throwable cause) {
+        super(message, cause);
     }
 }

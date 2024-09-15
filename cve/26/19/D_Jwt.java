@@ -18,11 +18,10 @@ package io.jsonwebtoken;
 /**
  * An expanded (not compact/serialized) JSON Web Token.
  *
- * @param <B> the type of the JWT body contents, either a String or a {@link Claims} instance.
- *
+ * @param <P> the type of the JWT payload, either a byte array or a {@link Claims} instance.
  * @since 0.1
  */
-public interface Jwt<H extends Header, B> {
+public interface Jwt<H extends Header<H>, P> {
 
     /**
      * Returns the JWT {@link Header} or {@code null} if not present.
@@ -32,9 +31,26 @@ public interface Jwt<H extends Header, B> {
     H getHeader();
 
     /**
-     * Returns the JWT body, either a {@code String} or a {@code Claims} instance.
+     * Returns the JWT payload, either a {@code byte[]} or a {@code Claims} instance.  Use
+     * {@link #getPayload()} instead, as this method will be removed prior to the 1.0 release.
+     *
+     * @return the JWT payload, either a {@code byte[]} or a {@code Claims} instance.
+     * @deprecated since JJWT_RELEASE_VERSION because it has been renamed to {@link #getPayload()}.  'Payload' (not
+     * body) is what the JWT specifications call this property, so it has been renamed to reflect the correct JWT
+     * nomenclature/taxonomy.
+     */
+    @SuppressWarnings("DeprecatedIsStillUsed")
+    @Deprecated
+    P getBody(); // TODO: remove for 1.0
+
+    /**
+     * Returns the JWT payload, either a {@code byte[]} or a {@code Claims} instance.  If the payload is a byte
+     * array, and <em>if</em> the JWT creator set the (optional) {@link Header#getContentType() contentType} header
+     * value, the application may inspect the {@code contentType} value to determine how to convert the byte array to
+     * the final content type as desired.
      *
-     * @return the JWT body, either a {@code String} or a {@code Claims} instance.
+     * @return the JWT payload, either a {@code byte[]} or a {@code Claims} instance.
+     * @since JJWT_RELEASE_VERSION
      */
-    B getBody();
+    P getPayload();
 }

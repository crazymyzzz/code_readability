@@ -24,12 +24,11 @@ import java.util.Map;
  * <p>This is ultimately a JSON map and any values can be added to it, but JWT standard names are provided as
  * type-safe getters and setters for convenience.</p>
  *
- * <p>Because this interface extends {@code Map&lt;String, Object&gt;}, if you would like to add your own properties,
+ * <p>Because this interface extends <code>Map&lt;String, Object&gt;</code>, if you would like to add your own properties,
  * you simply use map methods, for example:</p>
  *
- * <pre>
- * claims.{@link Map#put(Object, Object) put}("someKey", "someValue");
- * </pre>
+ * <blockquote><pre>
+ * claims.{@link Map#put(Object, Object) put}("someKey", "someValue");</pre></blockquote>
  *
  * <h2>Creation</h2>
  *
@@ -41,25 +40,25 @@ import java.util.Map;
 public interface Claims extends Map<String, Object>, ClaimsMutator<Claims> {
 
     /** JWT {@code Issuer} claims parameter name: <code>"iss"</code> */
-    public static final String ISSUER = "iss";
+    String ISSUER = "iss";
 
     /** JWT {@code Subject} claims parameter name: <code>"sub"</code> */
-    public static final String SUBJECT = "sub";
+    String SUBJECT = "sub";
 
     /** JWT {@code Audience} claims parameter name: <code>"aud"</code> */
-    public static final String AUDIENCE = "aud";
+    String AUDIENCE = "aud";
 
     /** JWT {@code Expiration} claims parameter name: <code>"exp"</code> */
-    public static final String EXPIRATION = "exp";
+    String EXPIRATION = "exp";
 
     /** JWT {@code Not Before} claims parameter name: <code>"nbf"</code> */
-    public static final String NOT_BEFORE = "nbf";
+    String NOT_BEFORE = "nbf";
 
     /** JWT {@code Issued At} claims parameter name: <code>"iat"</code> */
-    public static final String ISSUED_AT = "iat";
+    String ISSUED_AT = "iat";
 
     /** JWT {@code JWT ID} claims parameter name: <code>"jti"</code> */
-    public static final String ID = "jti";
+    String ID = "jti";
 
     /**
      * Returns the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.1">

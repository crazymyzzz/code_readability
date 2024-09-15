@@ -17,36 +17,42 @@ package io.jsonwebtoken.impl;
 
 import io.jsonwebtoken.Jws;
 import io.jsonwebtoken.JwsHeader;
+import io.jsonwebtoken.lang.Objects;
 
-public class DefaultJws<B> implements Jws<B> {
+public class DefaultJws<P> extends DefaultJwt<JwsHeader, P> implements Jws<P> {
 
-    private final JwsHeader header;
-    private final B body;
     private final String signature;
 
-    public DefaultJws(JwsHeader header, B body, String signature) {
-        this.header = header;
-        this.body = body;
+    public DefaultJws(JwsHeader header, P body, String signature) {
+        super(header, body);
         this.signature = signature;
     }
 
     @Override
-    public JwsHeader getHeader() {
-        return this.header;
+    public String getSignature() {
+        return this.signature;
     }
 
     @Override
-    public B getBody() {
-        return this.body;
+    protected StringBuilder toStringBuilder() {
+        return super.toStringBuilder().append(",signature=").append(signature);
     }
 
     @Override
-    public String getSignature() {
-        return this.signature;
+    public boolean equals(Object obj) {
+        if (obj == this) {
+            return true;
+        }
+        if (obj instanceof Jws) {
+            Jws<?> jws = (Jws<?>) obj;
+            return super.equals(jws) &&
+                Objects.nullSafeEquals(signature, jws.getSignature());
+        }
+        return false;
     }
 
     @Override
-    public String toString() {
-        return "header=" + header + ",body=" + body + ",signature=" + signature;
+    public int hashCode() {
+        return Objects.nullSafeHashCode(getHeader(), getPayload(), signature);
     }
 }

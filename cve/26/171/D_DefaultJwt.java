@@ -17,29 +17,67 @@ package io.jsonwebtoken.impl;
 
 import io.jsonwebtoken.Header;
 import io.jsonwebtoken.Jwt;
+import io.jsonwebtoken.io.Encoders;
+import io.jsonwebtoken.lang.Assert;
+import io.jsonwebtoken.lang.Objects;
 
-public class DefaultJwt<B> implements Jwt<Header,B> {
+public class DefaultJwt<H extends Header<H>, P> implements Jwt<H, P> {
 
-    private final Header header;
-    private final B body;
+    private final H header;
+    private final P payload;
 
-    public DefaultJwt(Header header, B body) {
-        this.header = header;
-        this.body = body;
+    public DefaultJwt(H header, P payload) {
+        this.header = Assert.notNull(header, "header cannot be null.");
+        this.payload = Assert.notNull(payload, "payload cannot be null.");
     }
 
     @Override
-    public Header getHeader() {
+    public H getHeader() {
         return header;
     }
 
     @Override
-    public B getBody() {
-        return body;
+    public P getBody() {
+        return getPayload();
     }
 
     @Override
-    public String toString() {
-        return "header=" + header + ",body=" + body;
+    public P getPayload() {
+        return this.payload;
+    }
+
+    protected StringBuilder toStringBuilder() {
+        StringBuilder sb = new StringBuilder(100);
+        sb.append("header=").append(header).append(",payload=");
+        if (payload instanceof byte[]) {
+            String encoded = Encoders.BASE64URL.encode((byte[]) payload);
+            sb.append(encoded);
+        } else {
+            sb.append(payload);
+        }
+        return sb;
+    }
+
+    @Override
+    public final String toString() {
+        return toStringBuilder().toString();
+    }
+
+    @Override
+    public boolean equals(Object obj) {
+        if (obj == this) {
+            return true;
+        }
+        if (obj instanceof Jwt) {
+            Jwt<?, ?> jwt = (Jwt<?, ?>) obj;
+            return Objects.nullSafeEquals(header, jwt.getHeader()) &&
+                    Objects.nullSafeEquals(payload, jwt.getPayload());
+        }
+        return false;
+    }
+
+    @Override
+    public int hashCode() {
+        return Objects.nullSafeHashCode(header, payload);
     }
 }

@@ -16,39 +16,25 @@
 package io.jsonwebtoken.impl;
 
 import io.jsonwebtoken.JwsHeader;
+import io.jsonwebtoken.impl.lang.Field;
 
 import java.util.Map;
+import java.util.Set;
 
-public class DefaultJwsHeader extends DefaultHeader implements JwsHeader {
+public class DefaultJwsHeader extends AbstractProtectedHeader<JwsHeader> implements JwsHeader {
 
-    public DefaultJwsHeader() {
-        super();
-    }
-
-    public DefaultJwsHeader(Map<String, Object> map) {
-        super(map);
-    }
+    static final Set<Field<?>> FIELDS = AbstractProtectedHeader.FIELDS; //same
 
-    @Override
-    public String getAlgorithm() {
-        return getString(ALGORITHM);
-    }
-
-    @Override
-    public JwsHeader setAlgorithm(String alg) {
-        setValue(ALGORITHM, alg);
-        return this;
+    public DefaultJwsHeader() {
+        super(FIELDS);
     }
 
-    @Override
-    public String getKeyId() {
-        return getString(KEY_ID);
+    public DefaultJwsHeader(Map<String, ?> map) {
+        super(FIELDS, map);
     }
 
     @Override
-    public JwsHeader setKeyId(String kid) {
-        setValue(KEY_ID, kid);
-        return this;
+    public String getName() {
+        return "JWS header";
     }
-
 }

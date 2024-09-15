@@ -1,5 +1,5 @@
 /*
- * Copyright (C) 2014 jsonwebtoken.io
+ * Copyright (C) 2021 jsonwebtoken.io
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
@@ -13,13 +13,15 @@
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
-package io.jsonwebtoken.impl.crypto;
+package io.jsonwebtoken.impl.security;
 
-import io.jsonwebtoken.SignatureAlgorithm;
+import io.jsonwebtoken.security.Jwk;
 
 import java.security.Key;
 
-public interface SignatureValidatorFactory {
+public interface JwkFactory<K extends Key, J extends Jwk<K>> {
 
-    SignatureValidator createSignatureValidator(SignatureAlgorithm alg, Key key);
+    JwkContext<K> newContext(JwkContext<?> src,  K key);
+
+    J createJwk(JwkContext<K> ctx);
 }

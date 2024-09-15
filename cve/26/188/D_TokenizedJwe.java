@@ -1,5 +1,5 @@
 /*
- * Copyright (C) 2014 jsonwebtoken.io
+ * Copyright (C) 2021 jsonwebtoken.io
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
@@ -13,9 +13,11 @@
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
-package io.jsonwebtoken.impl.crypto;
+package io.jsonwebtoken.impl;
 
-public interface JwtSignatureValidator {
+public interface TokenizedJwe extends TokenizedJwt {
 
-    boolean isValid(String jwtWithoutSignature, String base64UrlEncodedSignature);
+    String getEncryptedKey();
+
+    String getIv();
 }

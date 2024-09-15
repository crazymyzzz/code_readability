@@ -1,5 +1,5 @@
 /*
- * Copyright (C) 2014 jsonwebtoken.io
+ * Copyright © 2021 jsonwebtoken.io
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
@@ -13,11 +13,12 @@
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
-package io.jsonwebtoken.impl.crypto;
+package io.jsonwebtoken.impl.lang;
 
-import io.jsonwebtoken.security.SignatureException;
-
-public interface Signer {
+/**
+ * @since JJWT_RELEASE_VERSION
+ */
+public interface CheckedSupplier<T> {
 
-    byte[] sign(byte[] data) throws SignatureException;
+    T get() throws Exception;
 }

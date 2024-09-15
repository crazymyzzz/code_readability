@@ -1,5 +1,5 @@
 /*
- * Copyright (C) 2014 jsonwebtoken.io
+ * Copyright © 2022 jsonwebtoken.io
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
@@ -13,10 +13,9 @@
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
-package io.jsonwebtoken.impl.crypto;
+package io.jsonwebtoken.impl.lang;
 
-public interface SignatureValidator {
-
-    boolean isValid(byte[] data, byte[] signature);
+public interface FieldReadable {
 
+    <T> T get(Field<T> field);
 }

@@ -0,0 +1,25 @@
+/*
+ * Copyright (C) 2021 jsonwebtoken.io
+ *
+ * Licensed under the Apache License, Version 2.0 (the "License");
+ * you may not use this file except in compliance with the License.
+ * You may obtain a copy of the License at
+ *
+ *     http://www.apache.org/licenses/LICENSE-2.0
+ *
+ * Unless required by applicable law or agreed to in writing, software
+ * distributed under the License is distributed on an "AS IS" BASIS,
+ * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
+ * See the License for the specific language governing permissions and
+ * limitations under the License.
+ */
+package io.jsonwebtoken.impl;
+
+import io.jsonwebtoken.Header;
+import io.jsonwebtoken.HeaderMutator;
+import io.jsonwebtoken.lang.Builder;
+import io.jsonwebtoken.lang.MapMutator;
+
+// TODO: move this concept to the API when Java 8 is supported. Do we even need it?
+public interface HeaderBuilder<H extends Header<H>, T extends HeaderBuilder<H, T>> extends Builder<H>, MapMutator<String, Object, T>, HeaderMutator<T> {
+}

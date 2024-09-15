@@ -0,0 +1,44 @@
+/*
+ * Copyright © 2020 jsonwebtoken.io
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
+package io.jsonwebtoken.impl.lang;
+
+
+/**
+ * Function that always returns the same value
+ *
+ * @param <T> Input type
+ * @param <R> Return value type
+ */
+public final class ConstantFunction<T, R> implements Function<T, R> {
+
+    private static final Function<?, ?> NULL = new ConstantFunction<>(null);
+
+    private final R value;
+
+    public ConstantFunction(R value) {
+        this.value = value;
+    }
+
+    @SuppressWarnings("unchecked")
+    public static <T, R> Function<T, R> forNull() {
+        return (Function<T, R>) NULL;
+    }
+
+    @Override
+    public R apply(T t) {
+        return this.value;
+    }
+}

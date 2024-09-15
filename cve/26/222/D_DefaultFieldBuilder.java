@@ -0,0 +1,93 @@
+/*
+ * Copyright (C) 2022 jsonwebtoken.io
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
+import io.jsonwebtoken.lang.Assert;
+
+import java.util.Collection;
+import java.util.List;
+import java.util.Set;
+
+public class DefaultFieldBuilder<T> implements FieldBuilder<T> {
+
+    private String id;
+    private String name;
+    private boolean secret;
+    private final Class<T> type;
+    private Converter<T, ?> converter;
+    private Class<? extends Collection<T>> collectionType; // will be null if field doesn't represent a collection (list or set)
+
+    public DefaultFieldBuilder(Class<T> type) {
+        this.type = Assert.notNull(type, "Type cannot be null.");
+    }
+
+    @Override
+    public FieldBuilder<T> setId(String id) {
+        this.id = id;
+        return this;
+    }
+
+    @Override
+    public FieldBuilder<T> setName(String name) {
+        this.name = name;
+        return this;
+    }
+
+    @Override
+    public FieldBuilder<T> setSecret(boolean secret) {
+        this.secret = secret;
+        return this;
+    }
+
+    @SuppressWarnings("unchecked")
+    @Override
+    public FieldBuilder<List<T>> list() {
+        Class<?> clazz = List.class;
+        this.collectionType = (Class<? extends Collection<T>>) clazz;
+        return (FieldBuilder<List<T>>) this;
+    }
+
+    @SuppressWarnings("unchecked")
+    @Override
+    public FieldBuilder<Set<T>> set() {
+        Class<?> clazz = Set.class;
+        this.collectionType = (Class<? extends Collection<T>>) clazz;
+        return (FieldBuilder<Set<T>>) this;
+    }
+
+    @Override
+    public FieldBuilder<T> setConverter(Converter<T, ?> converter) {
+        this.converter = converter;
+        return this;
+    }
+
+    @SuppressWarnings({"rawtypes", "unchecked"})
+    @Override
+    public Field<T> build() {
+        Assert.notNull(this.type, "Type must be set.");
+        Converter conv = this.converter;
+        if (conv == null) {
+            conv = Converters.forType(this.type);
+        }
+        if (this.collectionType != null) {
+            conv = List.class.isAssignableFrom(collectionType) ? Converters.forList(conv) : Converters.forSet(conv);
+        }
+        if (this.secret) {
+            conv = new RedactedValueConverter(conv);
+        }
+        return new DefaultField<>(this.id, this.name, this.secret, this.type, this.collectionType, conv);
+    }
+}

@@ -22,11 +22,14 @@ import io.jsonwebtoken.io.SerializationException;
 import io.jsonwebtoken.io.Serializer;
 import io.jsonwebtoken.lang.Assert;
 import io.jsonwebtoken.lang.Strings;
+import io.jsonwebtoken.lang.Supplier;
 
 public class GsonSerializer<T> implements Serializer<T> {
 
-    static final Gson DEFAULT_GSON = new GsonBuilder().disableHtmlEscaping().create();
-    private Gson gson;
+    static final Gson DEFAULT_GSON = new GsonBuilder()
+            .registerTypeHierarchyAdapter(Supplier.class, GsonSupplierSerializer.INSTANCE)
+            .disableHtmlEscaping().create();
+    private final Gson gson;
 
     @SuppressWarnings("unused") //used via reflection by RuntimeClasspathDeserializerLocator
     public GsonSerializer() {
@@ -37,6 +40,17 @@ public class GsonSerializer<T> implements Serializer<T> {
     public GsonSerializer(Gson gson) {
         Assert.notNull(gson, "gson cannot be null.");
         this.gson = gson;
+
+        //ensure the necessary type adapter has been registered, and if not, throw an error:
+        String json = this.gson.toJson(TestSupplier.INSTANCE);
+        if (json.contains("value")) {
+            String msg = "Invalid Gson instance - it has not been registered with the necessary " +
+                    Supplier.class.getName() + " type adapter.  When using the GsonBuilder, ensure this " +
+                    "type adapter is registered by calling gsonBuilder.registerTypeHierarchyAdapter(" +
+                    Supplier.class.getName() + ".class, " +
+                    GsonSupplierSerializer.class.getName() + ".INSTANCE) before calling gsonBuilder.create()";
+            throw new IllegalArgumentException(msg);
+        }
     }
 
     @Override
@@ -62,4 +76,19 @@ public class GsonSerializer<T> implements Serializer<T> {
         }
         return this.gson.toJson(o).getBytes(Strings.UTF_8);
     }
+
+    private static class TestSupplier<T> implements Supplier<T> {
+
+        private static final TestSupplier<String> INSTANCE = new TestSupplier<>("test");
+        private final T value;
+
+        private TestSupplier(T value) {
+            this.value = value;
+        }
+
+        @Override
+        public T get() {
+            return value;
+        }
+    }
 }

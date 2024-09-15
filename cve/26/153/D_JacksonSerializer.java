@@ -16,7 +16,9 @@
 package io.jsonwebtoken.jackson.io;
 
 import com.fasterxml.jackson.core.JsonProcessingException;
+import com.fasterxml.jackson.databind.Module;
 import com.fasterxml.jackson.databind.ObjectMapper;
+import com.fasterxml.jackson.databind.module.SimpleModule;
 import io.jsonwebtoken.io.SerializationException;
 import io.jsonwebtoken.io.Serializer;
 import io.jsonwebtoken.lang.Assert;
@@ -26,7 +28,16 @@ import io.jsonwebtoken.lang.Assert;
  */
 public class JacksonSerializer<T> implements Serializer<T> {
 
-    static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper();
+    static final String MODULE_ID = "jjwt-jackson";
+    static final Module MODULE;
+
+    static {
+        SimpleModule module = new SimpleModule(MODULE_ID);
+        module.addSerializer(JacksonSupplierSerializer.INSTANCE);
+        MODULE = module;
+    }
+
+    static final ObjectMapper DEFAULT_OBJECT_MAPPER = new ObjectMapper().registerModule(MODULE);
 
     private final ObjectMapper objectMapper;
 
@@ -38,7 +49,7 @@ public class JacksonSerializer<T> implements Serializer<T> {
     @SuppressWarnings("WeakerAccess") //intended for end-users to use when providing a custom ObjectMapper
     public JacksonSerializer(ObjectMapper objectMapper) {
         Assert.notNull(objectMapper, "ObjectMapper cannot be null.");
-        this.objectMapper = objectMapper;
+        this.objectMapper = objectMapper.registerModule(MODULE);
     }
 
     @Override

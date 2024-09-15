@@ -18,7 +18,6 @@ package io.jsonwebtoken.jackson.io;
 import com.fasterxml.jackson.core.JsonParser;
 import com.fasterxml.jackson.databind.DeserializationContext;
 import com.fasterxml.jackson.databind.ObjectMapper;
-
 import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
 import com.fasterxml.jackson.databind.module.SimpleModule;
 import io.jsonwebtoken.io.DeserializationException;
@@ -37,7 +36,6 @@ public class JacksonDeserializer<T> implements Deserializer<T> {
     private final Class<T> returnType;
     private final ObjectMapper objectMapper;
 
-    @SuppressWarnings("unused") //used via reflection by RuntimeClasspathDeserializerLocator
     public JacksonDeserializer() {
         this(JacksonSerializer.DEFAULT_OBJECT_MAPPER);
     }
@@ -64,10 +62,10 @@ public class JacksonDeserializer<T> implements Deserializer<T> {
      * If you would like to use your own {@code ObjectMapper} instance that also supports custom types for
      * JWT {@code Claims}, you will need to first customize your {@code ObjectMapper} instance by registering
      * your custom types and then use the {@link #JacksonDeserializer(ObjectMapper)} constructor instead.
-     * 
+     *
      * @param claimTypeMap The claim name-to-class map used to deserialize claims into the given type
      */
-    public JacksonDeserializer(Map<String, Class> claimTypeMap) {
+    public JacksonDeserializer(Map<String, Class<?>> claimTypeMap) {
         // DO NOT reuse JacksonSerializer.DEFAULT_OBJECT_MAPPER as this could result in sharing the custom deserializer
         // between instances
         this(new ObjectMapper());
@@ -110,9 +108,9 @@ public class JacksonDeserializer<T> implements Deserializer<T> {
      */
     private static class MappedTypeDeserializer extends UntypedObjectDeserializer {
 
-        private final Map<String, Class> claimTypeMap;
+        private final Map<String, Class<?>> claimTypeMap;
 
-        private MappedTypeDeserializer(Map<String, Class> claimTypeMap) {
+        private MappedTypeDeserializer(Map<String, Class<?>> claimTypeMap) {
             super(null, null);
             this.claimTypeMap = claimTypeMap;
         }
@@ -122,7 +120,7 @@ public class JacksonDeserializer<T> implements Deserializer<T> {
             // check if the current claim key is mapped, if so traverse it's value
             String name = parser.currentName();
             if (claimTypeMap != null && name != null && claimTypeMap.containsKey(name)) {
-                Class type = claimTypeMap.get(name);
+                Class<?> type = claimTypeMap.get(name);
                 return parser.readValueAsTree().traverse(parser.getCodec()).readValueAs(type);
             }
             // otherwise default to super

@@ -16,10 +16,21 @@
 package io.jsonwebtoken.io;
 
 /**
+ * A {@code Serializer} is able to convert a Java object into a formatted data byte array.  It is expected this data
+ * can be reconstituted back into a Java object with a matching {@link Deserializer}.
+ *
+ * @param <T> The type of object to serialize.
  * @since 0.10.0
  */
 public interface Serializer<T> {
 
+    /**
+     * Convert the specified Java object into a formatted data byte array.
+     *
+     * @param t the object to serialize
+     * @return the serialized byte array representing the specified object.
+     * @throws SerializationException if there is a problem converting the object to a byte array.
+     */
     byte[] serialize(T t) throws SerializationException;
 
 }

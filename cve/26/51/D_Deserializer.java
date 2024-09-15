@@ -16,9 +16,19 @@
 package io.jsonwebtoken.io;
 
 /**
+ * A {@code Deserializer} is able to convert serialized data byte arrays into Java objects.
+ *
+ * @param <T> the type of object to be returned as a result of deserialization.
  * @since 0.10.0
  */
 public interface Deserializer<T> {
 
+    /**
+     * Convert the specified formatted data byte array into a Java object.
+     *
+     * @param bytes the formatted data byte array to convert
+     * @return the reconstituted Java object
+     * @throws DeserializationException if there is a problem converting the byte array to to an object.
+     */
     T deserialize(byte[] bytes) throws DeserializationException;
 }

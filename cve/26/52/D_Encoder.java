@@ -16,9 +16,20 @@
 package io.jsonwebtoken.io;
 
 /**
+ * An encoder converts data of one type into another formatted data value.
+ *
+ * @param <T> the type of data to convert
+ * @param <R> the type of the resulting formatted data
  * @since 0.10.0
  */
 public interface Encoder<T, R> {
 
+    /**
+     * Convert the specified data into another formatted data value.
+     *
+     * @param t the data to convert
+     * @return the resulting formatted data value
+     * @throws EncodingException if there is a problem during encoding
+     */
     R encode(T t) throws EncodingException;
 }

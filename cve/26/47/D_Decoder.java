@@ -16,9 +16,18 @@
 package io.jsonwebtoken.io;
 
 /**
+ * A decoder converts an already-encoded data value to a desired data type.
+ *
  * @since 0.10.0
  */
 public interface Decoder<T, R> {
 
+    /**
+     * Convert the specified encoded data value into the desired data type.
+     *
+     * @param t the encoded data
+     * @return the resulting expected data
+     * @throws DecodingException if there is a problem during decoding.
+     */
     R decode(T t) throws DecodingException;
 }

@@ -21,11 +21,13 @@ import java.util.Map;
 
 /**
  * Utility class to help with the manipulation of working with Maps.
+ *
  * @since 0.11.0
  */
 public final class Maps {
 
-    private Maps() {} //prevent instantiation
+    private Maps() {
+    } //prevent instantiation
 
     /**
      * Creates a new map builder with a single entry.
@@ -35,7 +37,8 @@ public final class Maps {
      *     // ...
      *     .build();
      * }</pre>
-     * @param key the key of an map entry to be added
+     *
+     * @param key   the key of an map entry to be added
      * @param value the value of map entry to be added
      * @param <K> the maps key type
      * @param <V> the maps value type
@@ -53,21 +56,24 @@ public final class Maps {
      *     // ...
      *     .build();
      * }</pre>
+     *
      * @param <K> the maps key type
      * @param <V> the maps value type
      */
-    public interface MapBuilder<K, V> {
+    public interface MapBuilder<K, V> extends Builder<Map<K, V>> {
         /**
          * Add a new entry to this map builder
-         * @param key the key of an map entry to be added
+         *
+         * @param key   the key of an map entry to be added
          * @param value the value of map entry to be added
          * @return the current MapBuilder to allow for method chaining.
          */
         MapBuilder<K, V> and(K key, V value);
 
         /**
-         * Returns a the resulting Map object from this MapBuilder.
-         * @return Returns a the resulting Map object from this MapBuilder.
+         * Returns the resulting Map object from this MapBuilder.
+         *
+         * @return the resulting Map object from this MapBuilder.
          */
         Map<K, V> build();
     }
@@ -80,6 +86,7 @@ public final class Maps {
             data.put(key, value);
             return this;
         }
+
         public Map<K, V> build() {
             return Collections.unmodifiableMap(data);
         }

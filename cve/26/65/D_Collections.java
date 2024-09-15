@@ -20,22 +20,155 @@ import java.util.Arrays;
 import java.util.Collection;
 import java.util.Enumeration;
 import java.util.Iterator;
+import java.util.LinkedHashSet;
 import java.util.List;
 import java.util.Map;
 import java.util.Properties;
+import java.util.Set;
 
+/**
+ * Utility methods for working with {@link Collection}s, {@link List}s, {@link Set}s, and {@link Maps}.
+ */
+@SuppressWarnings({"unused", "rawtypes"})
 public final class Collections {
 
-    private Collections(){} //prevent instantiation
+    private Collections() {
+    } //prevent instantiation
+
+    /**
+     * Returns a type-safe immutable empty {@code List}.
+     *
+     * @param <T> list element type
+     * @return a type-safe immutable empty {@code List}.
+     */
+    public static <T> List<T> emptyList() {
+        return java.util.Collections.emptyList();
+    }
+
+    /**
+     * Returns a type-safe immutable empty {@code Set}.
+     *
+     * @param <T> set element type
+     * @return a type-safe immutable empty {@code Set}.
+     */
+    @SuppressWarnings("unused")
+    public static <T> Set<T> emptySet() {
+        return java.util.Collections.emptySet();
+    }
+
+    /**
+     * Returns a type-safe immutable empty {@code Map}.
+     *
+     * @param <K> map key type
+     * @param <V> map value type
+     * @return a type-safe immutable empty {@code Map}.
+     */
+    @SuppressWarnings("unused")
+    public static <K, V> Map<K, V> emptyMap() {
+        return java.util.Collections.emptyMap();
+    }
+
+    /**
+     * Returns a type-safe immutable {@code List} containing the specified array elements.
+     *
+     * @param elements array elements to include in the list
+     * @param <T>      list element type
+     * @return a type-safe immutable {@code List} containing the specified array elements.
+     */
+    @SafeVarargs
+    public static <T> List<T> of(T... elements) {
+        if (elements == null || elements.length == 0) {
+            return java.util.Collections.emptyList();
+        }
+        return java.util.Collections.unmodifiableList(Arrays.asList(elements));
+    }
+
+    /**
+     * Returns a type-safe immutable {@code Set} containing the specified array elements.
+     *
+     * @param elements array elements to include in the set
+     * @param <T>      set element type
+     * @return a type-safe immutable {@code Set} containing the specified array elements.
+     */
+    @SafeVarargs
+    public static <T> Set<T> setOf(T... elements) {
+        if (elements == null || elements.length == 0) {
+            return java.util.Collections.emptySet();
+        }
+        Set<T> set = new LinkedHashSet<>(Arrays.asList(elements));
+        return immutable(set);
+    }
+
+    /**
+     * Shorter null-safe convenience alias for {@link java.util.Collections#unmodifiableList(List)} so both classes
+     * don't need to be imported.
+     *
+     * @param m   map to wrap in an immutable/unmodifiable collection
+     * @param <K> map key type
+     * @param <V> map value type
+     * @return an immutable wrapper for {@code m}.
+     * @since JJWT_RELEASE_VERSION
+     */
+    public static <K, V> Map<K, V> immutable(Map<K, V> m) {
+        return m != null ? java.util.Collections.unmodifiableMap(m) : null;
+    }
+
+    /**
+     * Shorter null-safe convenience alias for {@link java.util.Collections#unmodifiableSet(Set)} so both classes don't
+     * need to be imported.
+     *
+     * @param set set to wrap in an immutable Set
+     * @param <T> set element type
+     * @return an immutable wrapper for {@code set}
+     */
+    public static <T> Set<T> immutable(Set<T> set) {
+        return set != null ? java.util.Collections.unmodifiableSet(set) : null;
+    }
+
+    /**
+     * Shorter null-safe convenience alias for {@link java.util.Collections#unmodifiableList(List)} so both classes
+     * don't need to be imported.
+     *
+     * @param list list to wrap in an immutable List
+     * @param <T>  list element type
+     * @return an immutable wrapper for {@code list}
+     */
+    public static <T> List<T> immutable(List<T> list) {
+        return list != null ? java.util.Collections.unmodifiableList(list) : null;
+    }
+
+    /**
+     * Null-safe factory method that returns an immutable/unmodifiable view of the specified collection instance.
+     * Works for {@link List}, {@link Set} and {@link Collection} arguments.
+     *
+     * @param c   collection to wrap in an immutable/unmodifiable collection
+     * @param <C> type of collection
+     * @param <T> type of elements in the collection
+     * @return an immutable wrapper for {@code l}.
+     * @since JJWT_RELEASE_VERSION
+     */
+    @SuppressWarnings("unchecked")
+    public static <T, C extends Collection<T>> C immutable(C c) {
+        if (c == null) {
+            return null;
+        } else if (c instanceof Set) {
+            return (C) java.util.Collections.unmodifiableSet((Set<T>) c);
+        } else if (c instanceof List) {
+            return (C) java.util.Collections.unmodifiableList((List<T>) c);
+        } else {
+            return (C) java.util.Collections.unmodifiableCollection(c);
+        }
+    }
 
     /**
      * Return <code>true</code> if the supplied Collection is <code>null</code>
      * or empty. Otherwise, return <code>false</code>.
+     *
      * @param collection the Collection to check
      * @return whether the given Collection is empty
      */
-    public static boolean isEmpty(Collection collection) {
-        return (collection == null || collection.isEmpty());
+    public static boolean isEmpty(Collection<?> collection) {
+        return size(collection) == 0;
     }
 
     /**
@@ -45,7 +178,7 @@ public final class Collections {
      * @return the collection's size or {@code 0} if the collection is {@code null}.
      * @since 0.9.2
      */
-    public static int size(Collection collection) {
+    public static int size(Collection<?> collection) {
         return collection == null ? 0 : collection.size();
     }
 
@@ -56,18 +189,19 @@ public final class Collections {
      * @return the map's size or {@code 0} if the map is {@code null}.
      * @since 0.9.2
      */
-    public static int size(Map map) {
+    public static int size(Map<?, ?> map) {
         return map == null ? 0 : map.size();
     }
 
     /**
      * Return <code>true</code> if the supplied Map is <code>null</code>
      * or empty. Otherwise, return <code>false</code>.
+     *
      * @param map the Map to check
      * @return whether the given Map is empty
      */
-    public static boolean isEmpty(Map map) {
-        return (map == null || map.isEmpty());
+    public static boolean isEmpty(Map<?, ?> map) {
+        return size(map) == 0;
     }
 
     /**
@@ -75,6 +209,7 @@ public final class Collections {
      * converted into a List of the appropriate wrapper type.
      * <p>A <code>null</code> source value will be converted to an
      * empty List.
+     *
      * @param source the (potentially primitive) array
      * @return the converted List result
      * @see Objects#toObjectArray(Object)
@@ -83,9 +218,28 @@ public final class Collections {
         return Arrays.asList(Objects.toObjectArray(source));
     }
 
+    /**
+     * Concatenate the specified set with the specified array elements, resulting in a new {@link LinkedHashSet} with
+     * the array elements appended to the end of the existing Set.
+     *
+     * @param c        the set to append to
+     * @param elements the array elements to append to the end of the set
+     * @param <T>      set element type
+     * @return a new {@link LinkedHashSet} with the array elements appended to the end of the original set.
+     */
+    @SafeVarargs
+    public static <T> Set<T> concat(Set<T> c, T... elements) {
+        int size = Math.max(1, Collections.size(c) + io.jsonwebtoken.lang.Arrays.length(elements));
+        Set<T> set = new LinkedHashSet<>(size);
+        set.addAll(c);
+        java.util.Collections.addAll(set, elements);
+        return immutable(set);
+    }
+
     /**
      * Merge the given array into the given Collection.
-     * @param array the array to merge (may be <code>null</code>)
+     *
+     * @param array      the array to merge (may be <code>null</code>)
      * @param collection the target Collection to merge the array into
      */
     @SuppressWarnings("unchecked")
@@ -94,9 +248,7 @@ public final class Collections {
             throw new IllegalArgumentException("Collection must not be null");
         }
         Object[] arr = Objects.toObjectArray(array);
-        for (Object elem : arr) {
-            collection.add(elem);
-        }
+        java.util.Collections.addAll(collection, arr);
     }
 
     /**
@@ -104,8 +256,9 @@ public final class Collections {
      * copying all properties (key-value pairs) over.
      * <p>Uses <code>Properties.propertyNames()</code> to even catch
      * default properties linked into the original Properties instance.
+     *
      * @param props the Properties instance to merge (may be <code>null</code>)
-     * @param map the target Map to merge the properties into
+     * @param map   the target Map to merge the properties into
      */
     @SuppressWarnings("unchecked")
     public static void mergePropertiesIntoMap(Properties props, Map map) {
@@ -113,7 +266,7 @@ public final class Collections {
             throw new IllegalArgumentException("Map must not be null");
         }
         if (props != null) {
-            for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
+            for (Enumeration en = props.propertyNames(); en.hasMoreElements(); ) {
                 String key = (String) en.nextElement();
                 Object value = props.getProperty(key);
                 if (value == null) {
@@ -128,8 +281,9 @@ public final class Collections {
 
     /**
      * Check whether the given Iterator contains the given element.
+     *
      * @param iterator the Iterator to check
-     * @param element the element to look for
+     * @param element  the element to look for
      * @return <code>true</code> if found, <code>false</code> else
      */
     public static boolean contains(Iterator iterator, Object element) {
@@ -146,8 +300,9 @@ public final class Collections {
 
     /**
      * Check whether the given Enumeration contains the given element.
+     *
      * @param enumeration the Enumeration to check
-     * @param element the element to look for
+     * @param element     the element to look for
      * @return <code>true</code> if found, <code>false</code> else
      */
     public static boolean contains(Enumeration enumeration, Object element) {
@@ -166,8 +321,9 @@ public final class Collections {
      * Check whether the given Collection contains the given element instance.
      * <p>Enforces the given instance to be present, rather than returning
      * <code>true</code> for an equal element as well.
+     *
      * @param collection the Collection to check
-     * @param element the element to look for
+     * @param element    the element to look for
      * @return <code>true</code> if found, <code>false</code> else
      */
     public static boolean containsInstance(Collection collection, Object element) {
@@ -184,7 +340,8 @@ public final class Collections {
     /**
      * Return <code>true</code> if any element in '<code>candidates</code>' is
      * contained in '<code>source</code>'; otherwise returns <code>false</code>.
-     * @param source the source Collection
+     *
+     * @param source     the source Collection
      * @param candidates the candidates to search for
      * @return whether any of the candidates has been found
      */
@@ -205,7 +362,8 @@ public final class Collections {
      * '<code>source</code>'. If no element in '<code>candidates</code>' is present in
      * '<code>source</code>' returns <code>null</code>. Iteration order is
      * {@link Collection} implementation specific.
-     * @param source the source Collection
+     *
+     * @param source     the source Collection
      * @param candidates the candidates to search for
      * @return the first present object, or <code>null</code> if not found
      */
@@ -223,9 +381,10 @@ public final class Collections {
 
     /**
      * Find a single value of the given type in the given Collection.
+     *
      * @param collection the Collection to search
-     * @param type the type to look for
-     * @param <T> the generic type parameter for {@code type}
+     * @param type       the type to look for
+     * @param <T>        the generic type parameter for {@code type}
      * @return a value of the given type found if there is a clear match,
      * or <code>null</code> if none or more than one such value found
      */
@@ -251,8 +410,9 @@ public final class Collections {
      * Find a single value of one of the given types in the given Collection:
      * searching the Collection for a value of the first type, then
      * searching for a value of the second type, etc.
+     *
      * @param collection the collection to search
-     * @param types the types to look for, in prioritized order
+     * @param types      the types to look for, in prioritized order
      * @return a value of one of the given types found if there is a clear match,
      * or <code>null</code> if none or more than one such value found
      */
@@ -271,6 +431,7 @@ public final class Collections {
 
     /**
      * Determine whether the given Collection only contains a single unique object.
+     *
      * @param collection the Collection to check
      * @return <code>true</code> if the collection contains a single reference or
      * multiple references to the same instance, <code>false</code> else
@@ -285,8 +446,7 @@ public final class Collections {
             if (!hasCandidate) {
                 hasCandidate = true;
                 candidate = elem;
-            }
-            else if (candidate != elem) {
+            } else if (candidate != elem) {
                 return false;
             }
         }
@@ -295,6 +455,7 @@ public final class Collections {
 
     /**
      * Find the common element type of the given Collection, if any.
+     *
      * @param collection the Collection to check
      * @return the common element type, or <code>null</code> if no clear
      * common type has been found (or the collection was empty)
@@ -308,8 +469,7 @@ public final class Collections {
             if (val != null) {
                 if (candidate == null) {
                     candidate = val.getClass();
-                }
-                else if (candidate != val.getClass()) {
+                } else if (candidate != val.getClass()) {
                     return null;
                 }
             }
@@ -321,14 +481,15 @@ public final class Collections {
      * Marshal the elements from the given enumeration into an array of the given type.
      * Enumeration elements must be assignable to the type of the given array. The array
      * returned will be a different instance than the array given.
+     *
      * @param enumeration the collection to convert to an array
-     * @param array an array instance that matches the type of array to return
-     * @param <A> the element type of the array that will be created
-     * @param <E> the element type contained within the enumeration.
+     * @param array       an array instance that matches the type of array to return
+     * @param <A>         the element type of the array that will be created
+     * @param <E>         the element type contained within the enumeration.
      * @return a new array of type {@code A} that contains the elements in the specified {@code enumeration}.
      */
-    public static <A,E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
-        ArrayList<A> elements = new ArrayList<A>();
+    public static <A, E extends A> A[] toArray(Enumeration<E> enumeration, A[] array) {
+        ArrayList<A> elements = new ArrayList<>();
         while (enumeration.hasMoreElements()) {
             elements.add(enumeration.nextElement());
         }
@@ -337,12 +498,13 @@ public final class Collections {
 
     /**
      * Adapt an enumeration to an iterator.
+     *
      * @param enumeration the enumeration
-     * @param <E> the type of elements in the enumeration
+     * @param <E>         the type of elements in the enumeration
      * @return the iterator
      */
     public static <E> Iterator<E> toIterator(Enumeration<E> enumeration) {
-        return new EnumerationIterator<E>(enumeration);
+        return new EnumerationIterator<>(enumeration);
     }
 
     /**
@@ -350,7 +512,7 @@ public final class Collections {
      */
     private static class EnumerationIterator<E> implements Iterator<E> {
 
-        private Enumeration<E> enumeration;
+        private final Enumeration<E> enumeration;
 
         public EnumerationIterator(Enumeration<E> enumeration) {
             this.enumeration = enumeration;

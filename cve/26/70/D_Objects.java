@@ -20,18 +20,23 @@ import java.io.IOException;
 import java.lang.reflect.Array;
 import java.util.Arrays;
 
+/**
+ * Utility methods for working with object instances to reduce pattern repetition and otherwise
+ * increased cyclomatic complexity.
+ */
 public final class Objects {
 
-    private Objects(){} //prevent instantiation
+    private Objects() {
+    } //prevent instantiation
 
     private static final int INITIAL_HASH = 7;
-    private static final int MULTIPLIER   = 31;
+    private static final int MULTIPLIER = 31;
 
-    private static final String EMPTY_STRING            = "";
-    private static final String NULL_STRING             = "null";
-    private static final String ARRAY_START             = "{";
-    private static final String ARRAY_END               = "}";
-    private static final String EMPTY_ARRAY             = ARRAY_START + ARRAY_END;
+    private static final String EMPTY_STRING = "";
+    private static final String NULL_STRING = "null";
+    private static final String ARRAY_START = "{";
+    private static final String ARRAY_END = "}";
+    private static final String EMPTY_ARRAY = ARRAY_START + ARRAY_END;
     private static final String ARRAY_ELEMENT_SEPARATOR = ", ";
 
     /**
@@ -102,6 +107,16 @@ public final class Objects {
         return array == null || array.length == 0;
     }
 
+    /**
+     * Returns {@code true} if the specified character array is null or of zero length, {@code false} otherwise.
+     *
+     * @param chars the character array to check
+     * @return {@code true} if the specified character array is null or of zero length, {@code false} otherwise.
+     */
+    public static boolean isEmpty(char[] chars) {
+        return chars == null || chars.length == 0;
+    }
+
     /**
      * Check whether the given array contains the given element.
      *
@@ -145,8 +160,8 @@ public final class Objects {
     public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive) {
         for (Enum<?> candidate : enumValues) {
             if (caseSensitive ?
-                candidate.toString().equals(constant) :
-                candidate.toString().equalsIgnoreCase(constant)) {
+                    candidate.toString().equals(constant) :
+                    candidate.toString().equalsIgnoreCase(constant)) {
                 return true;
             }
         }
@@ -171,8 +186,8 @@ public final class Objects {
             }
         }
         throw new IllegalArgumentException(
-            String.format("constant [%s] does not exist in enum type %s",
-                          constant, enumValues.getClass().getComponentType().getName()));
+                String.format("constant [%s] does not exist in enum type %s",
+                        constant, enumValues.getClass().getComponentType().getName()));
     }
 
     /**
@@ -180,9 +195,9 @@ public final class Objects {
      * consisting of the input array contents plus the given object.
      *
      * @param array the array to append to (can be <code>null</code>)
-     * @param <A> the type of each element in the specified {@code array}
+     * @param <A>   the type of each element in the specified {@code array}
      * @param obj   the object to append
-     * @param <O> the type of the specified object, which must equal to or extend the {@code &lt;A&gt;} type.
+     * @param <O>   the type of the specified object, which must be equal to or extend the <code>&lt;A&gt;</code> type.
      * @return the new array (of the same component type; never <code>null</code>)
      */
     public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
@@ -300,6 +315,8 @@ public final class Objects {
      * methods for arrays in this class. If the object is <code>null</code>,
      * this method returns 0.
      *
+     * @param obj the object to use for obtaining a hashcode
+     * @return the object's hashcode, which could be 0 if the object is null.
      * @see #nullSafeHashCode(Object[])
      * @see #nullSafeHashCode(boolean[])
      * @see #nullSafeHashCode(byte[])
@@ -309,8 +326,6 @@ public final class Objects {
      * @see #nullSafeHashCode(int[])
      * @see #nullSafeHashCode(long[])
      * @see #nullSafeHashCode(short[])
-     * @param obj the object to use for obtaining a hashcode
-     * @return the object's hashcode, which could be 0 if the object is null.
      */
     public static int nullSafeHashCode(Object obj) {
         if (obj == null) {
@@ -351,10 +366,11 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the array to obtain a hashcode
      * @return the array's hashcode, which could be 0 if the array is null.
      */
-    public static int nullSafeHashCode(Object[] array) {
+    public static int nullSafeHashCode(Object... array) {
         if (array == null) {
             return 0;
         }
@@ -369,6 +385,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the boolean array to obtain a hashcode
      * @return the boolean array's hashcode, which could be 0 if the array is null.
      */
@@ -387,6 +404,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the byte array to obtain a hashcode
      * @return the byte array's hashcode, which could be 0 if the array is null.
      */
@@ -405,6 +423,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the char array to obtain a hashcode
      * @return the char array's hashcode, which could be 0 if the array is null.
      */
@@ -423,6 +442,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the double array to obtain a hashcode
      * @return the double array's hashcode, which could be 0 if the array is null.
      */
@@ -441,6 +461,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the float array to obtain a hashcode
      * @return the float array's hashcode, which could be 0 if the array is null.
      */
@@ -459,6 +480,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the int array to obtain a hashcode
      * @return the int array's hashcode, which could be 0 if the array is null.
      */
@@ -477,6 +499,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the long array to obtain a hashcode
      * @return the long array's hashcode, which could be 0 if the array is null.
      */
@@ -495,6 +518,7 @@ public final class Objects {
     /**
      * Return a hash code based on the contents of the specified array.
      * If <code>array</code> is <code>null</code>, this method returns 0.
+     *
      * @param array the short array to obtain a hashcode
      * @return the short array's hashcode, which could be 0 if the array is null.
      */
@@ -939,6 +963,12 @@ public final class Objects {
         return sb.toString();
     }
 
+    /**
+     * Iterate over the specified {@link Closeable} instances, invoking
+     * {@link Closeable#close()} on each one, ignoring any potential {@link IOException}s.
+     *
+     * @param closeables the closeables to close.
+     */
     public static void nullSafeClose(Closeable... closeables) {
         if (closeables == null) {
             return;

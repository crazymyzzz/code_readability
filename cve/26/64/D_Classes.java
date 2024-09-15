@@ -17,41 +17,36 @@ package io.jsonwebtoken.lang;
 
 import java.io.InputStream;
 import java.lang.reflect.Constructor;
+import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 
 /**
+ * Utility methods for working with {@link Class}es.
+ *
  * @since 0.1
  */
 public final class Classes {
 
-    private Classes() {} //prevent instantiation
+    private Classes() {
+    } //prevent instantiation
 
-    /**
-     * @since 0.1
-     */
     private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
         @Override
-        protected ClassLoader doGetClassLoader() throws Throwable {
+        protected ClassLoader doGetClassLoader() {
             return Thread.currentThread().getContextClassLoader();
         }
     };
 
-    /**
-     * @since 0.1
-     */
     private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
         @Override
-        protected ClassLoader doGetClassLoader() throws Throwable {
+        protected ClassLoader doGetClassLoader() {
             return Classes.class.getClassLoader();
         }
     };
 
-    /**
-     * @since 0.1
-     */
     private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
         @Override
-        protected ClassLoader doGetClassLoader() throws Throwable {
+        protected ClassLoader doGetClassLoader() {
             return ClassLoader.getSystemClassLoader();
         }
     };
@@ -65,14 +60,14 @@ public final class Classes {
      * the JRE's <code>ClassNotFoundException</code>.
      *
      * @param fqcn the fully qualified class name to load
-     * @param <T> The type of Class returned
+     * @param <T>  The type of Class returned
      * @return the located class
      * @throws UnknownClassException if the class cannot be found.
      */
     @SuppressWarnings("unchecked")
     public static <T> Class<T> forName(String fqcn) throws UnknownClassException {
 
-        Class clazz = THREAD_CL_ACCESSOR.loadClass(fqcn);
+        Class<?> clazz = THREAD_CL_ACCESSOR.loadClass(fqcn);
 
         if (clazz == null) {
             clazz = CLASS_CL_ACCESSOR.loadClass(fqcn);
@@ -93,7 +88,7 @@ public final class Classes {
             throw new UnknownClassException(msg);
         }
 
-        return clazz;
+        return (Class<T>) clazz;
     }
 
     /**
@@ -123,6 +118,14 @@ public final class Classes {
         return is;
     }
 
+    /**
+     * Returns {@code true} if the specified {@code fullyQualifiedClassName} can be found in any of the thread
+     * context, class, or system classloaders, or {@code false} otherwise.
+     *
+     * @param fullyQualifiedClassName the fully qualified class name to check
+     * @return {@code true} if the specified {@code fullyQualifiedClassName} can be found in any of the thread
+     * context, class, or system classloaders, or {@code false} otherwise.
+     */
     public static boolean isAvailable(String fullyQualifiedClassName) {
         try {
             forName(fullyQualifiedClassName);
@@ -132,22 +135,56 @@ public final class Classes {
         }
     }
 
+    /**
+     * Creates and returns a new instance of the class with the specified fully qualified class name using the
+     * classes default no-argument constructor.
+     *
+     * @param fqcn the fully qualified class name
+     * @param <T>  the type of object created
+     * @return a new instance of the specified class name
+     */
     @SuppressWarnings("unchecked")
     public static <T> T newInstance(String fqcn) {
-        return (T)newInstance(forName(fqcn));
+        return (T) newInstance(forName(fqcn));
     }
 
-    public static <T> T newInstance(String fqcn, Class[] ctorArgTypes, Object... args) {
+    /**
+     * Creates and returns a new instance of the specified fully qualified class name using the
+     * specified {@code args} arguments provided to the constructor with {@code ctorArgTypes}
+     *
+     * @param fqcn         the fully qualified class name
+     * @param ctorArgTypes the argument types of the constructor to invoke
+     * @param args         the arguments to supply when invoking the constructor
+     * @param <T>          the type of object created
+     * @return the newly created object
+     */
+    public static <T> T newInstance(String fqcn, Class<?>[] ctorArgTypes, Object... args) {
         Class<T> clazz = forName(fqcn);
         Constructor<T> ctor = getConstructor(clazz, ctorArgTypes);
         return instantiate(ctor, args);
     }
 
+    /**
+     * Creates and returns a new instance of the specified fully qualified class name using a constructor that matches
+     * the specified {@code args} arguments.
+     *
+     * @param fqcn fully qualified class name
+     * @param args the arguments to supply to the constructor
+     * @param <T>  the type of the object created
+     * @return the newly created object
+     */
     @SuppressWarnings("unchecked")
     public static <T> T newInstance(String fqcn, Object... args) {
-        return (T)newInstance(forName(fqcn), args);
+        return (T) newInstance(forName(fqcn), args);
     }
 
+    /**
+     * Creates a new instance of the specified {@code clazz} via {@code clazz.newInstance()}.
+     *
+     * @param clazz the class to invoke
+     * @param <T>   the type of the object created
+     * @return the newly created object
+     */
     public static <T> T newInstance(Class<T> clazz) {
         if (clazz == null) {
             String msg = "Class method parameter cannot be null.";
@@ -160,8 +197,17 @@ public final class Classes {
         }
     }
 
+    /**
+     * Returns a new instance of the specified {@code clazz}, invoking the associated constructor with the specified
+     * {@code args} arguments.
+     *
+     * @param clazz the class to invoke
+     * @param args  the arguments matching an associated class constructor
+     * @param <T>   the type of the created object
+     * @return the newly created object
+     */
     public static <T> T newInstance(Class<T> clazz, Object... args) {
-        Class[] argTypes = new Class[args.length];
+        Class<?>[] argTypes = new Class[args.length];
         for (int i = 0; i < args.length; i++) {
             argTypes[i] = args[i].getClass();
         }
@@ -169,7 +215,17 @@ public final class Classes {
         return instantiate(ctor, args);
     }
 
-    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class... argTypes) {
+    /**
+     * Returns the {@link Constructor} for the specified {@code Class} with arguments matching the specified
+     * {@code argTypes}.
+     *
+     * @param clazz    the class to inspect
+     * @param argTypes the argument types for the desired constructor
+     * @param <T>      the type of object to create
+     * @return the constructor matching the specified argument types
+     * @throws IllegalStateException if the constructor for the specified {@code argTypes} does not exist.
+     */
+    public static <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... argTypes) throws IllegalStateException {
         try {
             return clazz.getConstructor(argTypes);
         } catch (NoSuchMethodException e) {
@@ -178,6 +234,16 @@ public final class Classes {
 
     }
 
+    /**
+     * Creates a new object using the specified {@link Constructor}, invoking it with the specified constructor
+     * {@code args} arguments.
+     *
+     * @param ctor the constructor to invoke
+     * @param args the arguments to supply to the constructor
+     * @param <T>  the type of object to create
+     * @return the new object instance
+     * @throws InstantiationException if the constructor cannot be invoked successfully
+     */
     public static <T> T instantiate(Constructor<T> ctor, Object... args) {
         try {
             return ctor.newInstance(args);
@@ -190,24 +256,51 @@ public final class Classes {
     /**
      * Invokes the fully qualified class name's method named {@code methodName} with parameters of type {@code argTypes}
      * using the {@code args} as the method arguments.
-     * @param fqcn fully qualified class name to locate
+     *
+     * @param fqcn       fully qualified class name to locate
      * @param methodName name of the method to invoke on the class
-     * @param argTypes the method argument types supported by the {@code methodName} method
-     * @param args the runtime arguments to use when invoking the located class method
-     * @param <T> the expected type of the object returned from the invoked method.
+     * @param argTypes   the method argument types supported by the {@code methodName} method
+     * @param args       the runtime arguments to use when invoking the located class method
+     * @param <T>        the expected type of the object returned from the invoked method.
      * @return the result returned by the invoked method
      * @since 0.10.0
      */
+    public static <T> T invokeStatic(String fqcn, String methodName, Class<?>[] argTypes, Object... args) {
+        try {
+            Class<?> clazz = Classes.forName(fqcn);
+            return invokeStatic(clazz, methodName, argTypes, args);
+        } catch (Exception e) {
+            String msg = "Unable to invoke class method " + fqcn + "#" + methodName + ".  Ensure the necessary " +
+                    "implementation is in the runtime classpath.";
+            throw new IllegalStateException(msg, e);
+        }
+    }
+
+    /**
+     * Invokes the {@code clazz}'s matching static method (named {@code methodName} with exact argument types
+     * of {@code argTypes}) with the given {@code args} arguments, and returns the method return value.
+     *
+     * @param clazz      the class to invoke
+     * @param methodName the name of the static method on {@code clazz} to invoke
+     * @param argTypes   the types of the arguments accepted by the method
+     * @param args       the actual runtime arguments to use when invoking the method
+     * @param <T>        the type of object expected to be returned from the method
+     * @return the result returned by the invoked method.
+     * @since JJWT_RELEASE_VERSION
+     */
     @SuppressWarnings("unchecked")
-    public static <T> T invokeStatic(String fqcn, String methodName, Class[] argTypes, Object... args) {
+    public static <T> T invokeStatic(Class<?> clazz, String methodName, Class<?>[] argTypes, Object... args) {
         try {
-            Class clazz = Classes.forName(fqcn);
             Method method = clazz.getDeclaredMethod(methodName, argTypes);
             method.setAccessible(true);
-            return(T)method.invoke(null, args);
-        } catch (Exception e) {
-            String msg = "Unable to invoke class method " + fqcn + "#" + methodName + ".  Ensure the necessary " +
-                "implementation is in the runtime classpath.";
+            return (T) method.invoke(null, args);
+        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
+            Throwable cause = e.getCause();
+            if (cause instanceof RuntimeException) {
+                throw ((RuntimeException) cause); //propagate
+            }
+            String msg = "Unable to invoke class method " + clazz.getName() + "#" + methodName +
+                    ". Ensure the necessary implementation is in the runtime classpath.";
             throw new IllegalStateException(msg, e);
         }
     }
@@ -215,8 +308,8 @@ public final class Classes {
     /**
      * @since 1.0
      */
-    private static interface ClassLoaderAccessor {
-        Class loadClass(String fqcn);
+    private interface ClassLoaderAccessor {
+        Class<?> loadClass(String fqcn);
 
         InputStream getResourceStream(String name);
     }
@@ -226,8 +319,8 @@ public final class Classes {
      */
     private static abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {
 
-        public Class loadClass(String fqcn) {
-            Class clazz = null;
+        public Class<?> loadClass(String fqcn) {
+            Class<?> clazz = null;
             ClassLoader cl = getClassLoader();
             if (cl != null) {
                 try {

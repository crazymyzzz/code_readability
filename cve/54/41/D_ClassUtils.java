@@ -19,7 +19,6 @@ import lombok.AccessLevel;
 import lombok.NoArgsConstructor;
 import org.flywaydb.core.api.FlywayException;
 
-import java.beans.Expression;
 import java.io.File;
 import java.lang.annotation.ElementType;
 import java.lang.annotation.Retention;
@@ -60,47 +59,6 @@ public class ClassUtils {
         }
     }
 
-    @SuppressWarnings({"unchecked"})
-    public static <T> T instantiate(String className, ClassLoader classLoader, Object... params) {
-        try {
-            return (T) new Expression(Class.forName(className, false, classLoader), "new", params).getValue();
-        } catch (Exception e) {
-            throw new FlywayException("Unable to instantiate class " + className + " : " + e.getMessage(), e);
-        }
-    }
-
-    /**
-     * Creates a new instance of this class.
-     *
-     * @param clazz The class to instantiate.
-     * @return The new instance.
-     * @throws FlywayException Thrown when the instantiation failed.
-     */
-    public static <T> T instantiate(Class<T> clazz) {
-        try {
-            return clazz.getDeclaredConstructor().newInstance();
-        } catch (Exception e) {
-            throw new FlywayException("Unable to instantiate class " + clazz.getName() + " : " + e.getMessage(), e);
-        }
-    }
-
-    /**
-     * Instantiate all these classes.
-     *
-     * @param classes Fully qualified class names to instantiate.
-     * @param classLoader The ClassLoader to use.
-     * @return The list of instances.
-     */
-    public static <T> List<T> instantiateAll(String[] classes, ClassLoader classLoader) {
-        List<T> clazzes = new ArrayList<>();
-        for (String clazz : classes) {
-            if (StringUtils.hasLength(clazz)) {
-                clazzes.add(ClassUtils.instantiate(clazz, classLoader));
-            }
-        }
-        return clazzes;
-    }
-
     /**
      * Determine whether the {@link Class} identified by the supplied name is present
      * and can be loaded. Will return {@code false} if either the class or

@@ -176,7 +176,8 @@ public class ClassUtils {
             //Custom classloader with for example classes defined using URLClassLoader#defineClass(String name, byte[] b, int off, int len)
             return null;
         }
-        return UrlUtils.decodeURL(codeSource.getLocation().getPath());
+
+        return UrlUtils.decodeURLSafe(codeSource.getLocation().getPath());
     }
 
     public static String getLibDir(Class<?> clazz) {

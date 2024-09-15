@@ -1136,7 +1136,8 @@ public class StandardWrapper extends ContainerBase
         if (getServlet() == null) {
             Class<?> clazz = null;
             try {
-                clazz = getParentClassLoader().loadClass(getServletClass());
+                clazz = getParent().getLoader().getClassLoader().loadClass(
+                        getServletClass());
                 processServletSecurityAnnotation(clazz);
             } catch (ClassNotFoundException e) {
                 // Safe to ignore. No class means no annotations to process

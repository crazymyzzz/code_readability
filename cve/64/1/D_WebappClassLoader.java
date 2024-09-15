@@ -362,7 +362,7 @@ public class WebappClassLoader
      * Path where resources loaded from JARs will be extracted.
      */
     protected File loaderDir = null;
-
+    protected String canonicalLoaderDir = null;
 
     /**
      * The PermissionCollection for each CodeSource for a web
@@ -577,6 +577,18 @@ public class WebappClassLoader
      */
     public void setWorkDir(File workDir) {
         this.loaderDir = new File(workDir, "loader");
+        if (loaderDir == null) {
+            canonicalLoaderDir = null;
+        } else { 
+            try {
+                canonicalLoaderDir = loaderDir.getCanonicalPath();
+                if (!canonicalLoaderDir.endsWith(File.separator)) {
+                    canonicalLoaderDir += File.separator;
+                }
+            } catch (IOException ioe) {
+                canonicalLoaderDir = null;
+            }
+        }
     }
 
      /**
@@ -2514,6 +2526,18 @@ public class WebappClassLoader
                                         (".class"))) {
                                     resourceFile = new File
                                         (loaderDir, jarEntry2.getName());
+                                    try {
+                                        if (!resourceFile.getCanonicalPath().startsWith(
+                                                canonicalLoaderDir)) {
+                                            throw new IllegalArgumentException(
+                                                    sm.getString("webappClassLoader.illegalJarPath",
+                                                jarEntry2.getName()));
+                                        }
+                                    } catch (IOException ioe) {
+                                        throw new IllegalArgumentException(
+                                                sm.getString("webappClassLoader.validationErrorJarPath",
+                                                        jarEntry2.getName()), ioe);
+                                    }                                 
                                     resourceFile.getParentFile().mkdirs();
                                     FileOutputStream os = null;
                                     InputStream is = null;

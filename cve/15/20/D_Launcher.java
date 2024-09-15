
@@ -297,6 +321,9 @@ public AppClassLoader run() {
         public Class loadClass(String name, boolean resolve)
             throws ClassNotFoundException
         {
+            if (VM.isBootedKernelVM()) {
+                DownloadManager.getBootClassPathEntryForClass(name);
+            }
             int i = name.lastIndexOf('.');
             if (i != -1) {
                 SecurityManager sm = System.getSecurityManager();
@@ -353,39 +380,66 @@ private static AccessControlContext getContext(File[] cp)
 

 
-    public static URLClassPath getBootstrapClassPath() {
-        String prop = AccessController.doPrivileged(
-            new GetPropertyAction("sun.boot.class.path"));
-        URL[] urls;
-        if (prop != null) {
-            final String path = prop;
-            urls = AccessController.doPrivileged(
-                new PrivilegedAction<URL[]>() {
-                    public URL[] run() {
-                        File[] classPath = getClassPath(path);
-                        int len = classPath.length;
-                        Set<File> seenDirs = new HashSet<File>();
-                        for (int i = 0; i < len; i++) {
-                            File curEntry = classPath[i];
-                            // Negative test used to properly handle
-                            // nonexistent jars on boot class path
-                            if (!curEntry.isDirectory()) {
-                                curEntry = curEntry.getParentFile();
-                            }
-                            if (curEntry != null && seenDirs.add(curEntry)) {
-                                MetaIndex.registerDirectory(curEntry);
+    private static URLClassPath bootstrapClassPath;
+
+    public static synchronized URLClassPath getBootstrapClassPath() {
+        if (bootstrapClassPath == null) {
+            String prop = AccessController.doPrivileged(
+                new GetPropertyAction("sun.boot.class.path"));
+            URL[] urls;
+            if (prop != null) {
+                final String path = prop;
+                urls = AccessController.doPrivileged(
+                    new PrivilegedAction<URL[]>() {
+                        public URL[] run() {
+                            File[] classPath = getClassPath(path);
+                            int len = classPath.length;
+                            Set<File> seenDirs = new HashSet<File>();
+                            for (int i = 0; i < len; i++) {
+                                File curEntry = classPath[i];
+                                // Negative test used to properly handle
+                                // nonexistent jars on boot class path
+                                if (!curEntry.isDirectory()) {
+                                    curEntry = curEntry.getParentFile();
+                                }
+                                if (curEntry != null && seenDirs.add(curEntry)) {
+                                    MetaIndex.registerDirectory(curEntry);
+                                }
                             }
+                            return pathToURLs(classPath);
                         }
-                        return pathToURLs(classPath);
                     }
-                }
-            );
-        } else {
-            urls = new URL[0];
+                );
+            } else {
+                urls = new URL[0];
+            }
+
+            bootstrapClassPath = new URLClassPath(urls, factory);
+            if (VM.isBootedKernelVM()) {
+                final File[] additionalBootStrapPaths =
+                    DownloadManager.getAdditionalBootStrapPaths();
+                AccessController.doPrivileged(new PrivilegedAction() {
+                    public Object run() {
+                        for (int i=0; i<additionalBootStrapPaths.length; i++) {
+                            bootstrapClassPath.addURL(
+                                getFileURL(additionalBootStrapPaths[i]));
+                        }
+                        return null;
+                    }
+                });
+            }
         }
-        return new URLClassPath(urls, factory);
+        return bootstrapClassPath;
+    }
+



@@ -1280,6 +1281,21 @@ public static Enumeration<URL> getSystemResources(String name)
      * Find resources from the VM's built-in classloader.
      */
     private static URL getBootstrapResource(String name) {
+        try {
+            // If this is a known JRE resource, ensure that its bundle is
+            // downloaded.  If it isn't known, we just ignore the download
+            // failure and check to see if we can find the resource anyway
+            // (which is possible if the boot class path has been modified).
+            if (sun.misc.VM.isBootedKernelVM()) {
+                sun.jkernel.DownloadManager.getBootClassPathEntryForResource(
+                    name);
+            }
+        } catch (NoClassDefFoundError e) {
+            // This happens while Java itself is being compiled; DownloadManager
+            // isn't accessible when this code is first invoked.  It isn't an
+            // issue, as if we can't find DownloadManager, we can safely assume
+            // that additional code is not available for download.
+        }
         URLClassPath ucp = getBootstrapClassPath();
         Resource res = ucp.getResource(name);
         return res != null ? res.getURL() : null;
@@ -1305,13 +1321,9 @@ public boolean hasMoreElements() {
 
     // Returns the URLClassPath that is used for finding system resources.
     static URLClassPath getBootstrapClassPath() {
-        if (bootstrapClassPath == null) {
-            bootstrapClassPath = sun.misc.Launcher.getBootstrapClassPath();
-        }
-        return bootstrapClassPath;
+        return sun.misc.Launcher.getBootstrapClassPath();
     }
 

@@ -1800,6 +1812,24 @@ private static String[] initializePath(String propname) {
     // Invoked in the java.lang.Runtime class to implement load and loadLibrary.
     static void loadLibrary(Class fromClass, String name,
                             boolean isAbsolute) {
+        try {
+            if (VM.isBootedKernelVM() && !DownloadManager.isJREComplete() &&
+                    !DownloadManager.isCurrentThreadDownloading()) {
+                DownloadManager.downloadFile("bin/" +
+                    System.mapLibraryName(name));
+                // it doesn't matter if the downloadFile call returns false --
+                // it probably just means that this is a user library, as
+                // opposed to a JRE library
+            }
+        } catch (IOException e) {
+            throw new UnsatisfiedLinkError("Error downloading library " +
+                                                name + ": " + e);
+        } catch (NoClassDefFoundError e) {
+            // This happens while Java itself is being compiled; DownloadManager
+            // isn't accessible when this code is first invoked.  It isn't an
+            // issue, as if we can't find DownloadManager, we can safely assume
+            // that additional code is not available for download.
+        }
         ClassLoader loader =
             (fromClass == null) ? null : fromClass.getClassLoader();
         if (sys_paths == null) {

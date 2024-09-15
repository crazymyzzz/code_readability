
@@ -94,16 +96,29 @@ public class ExpandWar {
         docBase.mkdir();
 
         // Expand the WAR into the new document base directory
+        String canonicalDocBasePrefix = docBase.getCanonicalPath();
+        if (!canonicalDocBasePrefix.endsWith(File.separator)) {
+            canonicalDocBasePrefix += File.separator;
+        }
         JarURLConnection juc = (JarURLConnection) war.openConnection();
         juc.setUseCaches(false);
         JarFile jarFile = null;
         InputStream input = null;
+        boolean success = false;
         try {
             jarFile = juc.getJarFile();
             Enumeration<JarEntry> jarEntries = jarFile.entries();
             while (jarEntries.hasMoreElements()) {
                 JarEntry jarEntry = jarEntries.nextElement();
                 String name = jarEntry.getName();
+                File expandedFile = new File(docBase, name);
+                if (!expandedFile.getCanonicalPath().startsWith(
+                        canonicalDocBasePrefix)) {
+                    // Trying to expand outside the docBase
+                    // Throw an exception to stop the deployment
+                    throw new IllegalArgumentException(
+                            sm.getString("expandWar.illegalPath",war, name));
+                }
                 int last = name.lastIndexOf('/');
                 if (last >= 0) {
                     File parent = new File(docBase,
@@ -116,21 +131,24 @@ public class ExpandWar {
                 input = jarFile.getInputStream(jarEntry);
 
                 // Bugzilla 33636
-                File expandedFile = expand(input, docBase, name);
+                expand(input, expandedFile);
                 long lastModified = jarEntry.getTime();
-                if ((lastModified != -1) && (lastModified != 0) && (expandedFile != null)) {
+                if ((lastModified != -1) && (lastModified != 0)) {
                     expandedFile.setLastModified(lastModified);
                 }
 
                 input.close();
                 input = null;
             }
+            success = true;
         } catch (IOException e) {
-            // If something went wrong, delete expanded dir to keep things 
-            // clean
-            deleteDir(docBase);
             throw e;
         } finally {
+            if (!success) {
+                // If something went wrong, delete expanded dir to keep things 
+                // clean
+                deleteDir(docBase);
+            }
             if (input != null) {
                 try {
                     input.close();

@@ -215,26 +296,61 @@ public class ExpandWar {
     
     /**
      * Delete the specified directory, including all of its contents and
-     * subdirectories recursively.
+     * sub-directories recursively. Any failure will be logged.
      *
      * @param dir File object representing the directory to be deleted
      */
     public static boolean delete(File dir) {
+        // Log failure by default
+        return delete(dir, true);
+    }
+
+    /**
+     * Delete the specified directory, including all of its contents and
+     * sub-directories recursively.
+     *
+     * @param dir File object representing the directory to be deleted
+     * @param logFailure <code>true</code> if failure to delete the resource
+     *                   should be logged
+     */
+    public static boolean delete(File dir, boolean logFailure) {
+        boolean result;
         if (dir.isDirectory()) {
-            return deleteDir(dir);
+            result = deleteDir(dir, logFailure);
         } else {
-            return dir.delete();
+            if (dir.exists()) {
+                result = dir.delete();
+            } else {
+                result = true;
+            }
         }
+        if (logFailure && !result) {
+            log.error(sm.getString(
+                    "expandWar.deleteFailed", dir.getAbsolutePath()));
+        }
+        return result;
     }
     
     
     /**
      * Delete the specified directory, including all of its contents and
-     * subdirectories recursively.
+     * sub-directories recursively. Any failure will be logged.
      *
      * @param dir File object representing the directory to be deleted
      */
     public static boolean deleteDir(File dir) {
+        return deleteDir(dir, true);
+    }
+
+    /**
+     * Delete the specified directory, including all of its contents and
+     * sub-directories recursively.
+     *
+     * @param dir File object representing the directory to be deleted
+     * @param logFailure <code>true</code> if failure to delete the resource
+     *                   should be logged
+     */
+    public static boolean deleteDir(File dir, boolean logFailure) {
 
         String files[] = dir.list();
         if (files == null) {
@@ -243,12 +359,25 @@ public class ExpandWar {
         for (int i = 0; i < files.length; i++) {
             File file = new File(dir, files[i]);
             if (file.isDirectory()) {
-                deleteDir(file);
+                deleteDir(file, logFailure);
             } else {
                 file.delete();
             }
         }
-        return dir.delete();
+
+        boolean result;
+        if (dir.exists()) {
+            result = dir.delete();
+        } else {
+            result = true;
+        }
+        
+        if (logFailure && !result) {
+            log.error(sm.getString(
+                    "expandWar.deleteFailed", dir.getAbsolutePath()));
+        }
+        
+        return result;
 
     }
 
@@ -263,11 +392,27 @@ public class ExpandWar {
      * @return A handle to the expanded File
      *
      * @exception IOException if an input/output error occurs
+     * 
+     * @deprecated
      */
     protected static File expand(InputStream input, File docBase, String name)
         throws IOException {
-
         File file = new File(docBase, name);
+        expand(input, file);
+        return file;
+    }
+
+
+    /**
+     * Expand the specified input stream into the specified file.
+     *
+     * @param input InputStream to be copied
+     * @param file The file to be created
+     *
+     * @exception IOException if an input/output error occurs
+     */
+    private static void expand(InputStream input, File file)
+        throws IOException {
         BufferedOutputStream output = null;
         try {
             output = 
@@ -288,8 +433,6 @@ public class ExpandWar {
                 }
             }
         }
-
-        return file;
     }
 
 

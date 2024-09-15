
@@ -418,36 +417,26 @@ public final class Files {
    *
    * @return the newly-created directory
    * @throws IllegalStateException if the directory could not be created
+   * @throws UnsupportedOperationException if the system does not support creating temporary
+   *     directories securely
    * @deprecated For Android users, see the <a
    *     href="https://developer.android.com/training/data-storage" target="_blank">Data and File
    *     Storage overview</a> to select an appropriate temporary directory (perhaps {@code
-   *     context.getCacheDir()}). For developers on Java 7 or later, use {@link
-   *     java.nio.file.Files#createTempDirectory}, transforming it to a {@link File} using {@link
-   *     java.nio.file.Path#toFile() toFile()} if needed.
+   *     context.getCacheDir()}), and create your own directory under that. (For example, you might
+   *     use {@code new File(context.getCacheDir(), "directoryname").mkdir()}, or, if you need an
+   *     arbitrary number of temporary directories, you might have to generate multiple directory
+   *     names in a loop until {@code mkdir()} returns {@code true}.) For developers on Java 7 or
+   *     later, use {@link java.nio.file.Files#createTempDirectory}, transforming it to a {@link
+   *     File} using {@link java.nio.file.Path#toFile() toFile()} if needed. To restrict permissions
+   *     as this method does, pass {@code
+   *     PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"))} to your
+   *     call to {@code createTempDirectory}.
    */
   @Beta
   @Deprecated
   @J2ObjCIncompatible
   public static File createTempDir() {
-    File baseDir = new File(System.getProperty("java.io.tmpdir"));
-    @SuppressWarnings("GoodTime") // reading system time without TimeSource
-    String baseName = System.currentTimeMillis() + "-";
-
-    for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
-      File tempDir = new File(baseDir, baseName + counter);
-      if (tempDir.mkdir()) {
-        return tempDir;
-      }
-    }
-    throw new IllegalStateException(
-        "Failed to create directory within "
-            + TEMP_DIR_ATTEMPTS
-            + " attempts (tried "
-            + baseName
-            + "0 to "
-            + baseName
-            + (TEMP_DIR_ATTEMPTS - 1)
-            + ')');
+    return TempFileCreator.INSTANCE.createTempDir();
   }
 
   /**

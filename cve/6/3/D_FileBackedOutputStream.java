
@@ -109,16 +118,13 @@ public final class FileBackedOutputStream extends OutputStream {
    * @param fileThreshold the number of bytes before the stream should switch to buffering to a file
    * @param resetOnFinalize if true, the {@link #reset} method will be called when the {@link
    *     ByteSource} returned by {@link #asByteSource} is finalized.
+   * @throws IllegalArgumentException if {@code fileThreshold} is negative
    */
   public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
-    this(fileThreshold, resetOnFinalize, null);
-  }
-
-  private FileBackedOutputStream(
-      int fileThreshold, boolean resetOnFinalize, @CheckForNull File parentDirectory) {
+    checkArgument(
+        fileThreshold >= 0, "fileThreshold must be non-negative, but was %s", fileThreshold);
     this.fileThreshold = fileThreshold;
     this.resetOnFinalize = resetOnFinalize;
-    this.parentDirectory = parentDirectory;
     memory = new MemoryOutput();
     out = memory;
 
@@ -229,7 +235,7 @@ public final class FileBackedOutputStream extends OutputStream {
   @GuardedBy("this")
   private void update(int len) throws IOException {
     if (memory != null && (memory.getCount() + len > fileThreshold)) {
-      File temp = File.createTempFile("FileBackedOutputStream", null, parentDirectory);
+      File temp = TempFileCreator.INSTANCE.createTempFile("FileBackedOutputStream");
       if (resetOnFinalize) {
         // Finalizers are not guaranteed to be called on system shutdown;
         // this is insurance.

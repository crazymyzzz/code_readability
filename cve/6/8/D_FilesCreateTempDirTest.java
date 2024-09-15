
@@ -26,12 +35,32 @@ import java.io.File;
  * @author Chris Nokleberg
  */
 
-public class FilesCreateTempDirTest extends IoTestCase {
-  public void testCreateTempDir() {
+@SuppressWarnings("deprecation") // tests of a deprecated method
+public class FilesCreateTempDirTest extends TestCase {
+  public void testCreateTempDir() throws IOException {
+    if (JAVA_IO_TMPDIR.value().equals("/sdcard")) {
+      assertThrows(IllegalStateException.class, Files::createTempDir);
+      return;
+    }
     File temp = Files.createTempDir();
-    assertTrue(temp.exists());
-    assertTrue(temp.isDirectory());
-    assertThat(temp.listFiles()).isEmpty();
-    assertTrue(temp.delete());
+    try {
+      assertTrue(temp.exists());
+      assertTrue(temp.isDirectory());
+      assertThat(temp.listFiles()).isEmpty();
+
+      if (isAndroid()) {
+        return;
+      }
+      PosixFileAttributes attributes =
+          java.nio.file.Files.getFileAttributeView(temp.toPath(), PosixFileAttributeView.class)
+              .readAttributes();
+      assertThat(attributes.permissions()).containsExactly(OWNER_READ, OWNER_WRITE, OWNER_EXECUTE);
+    } finally {
+      assertTrue(temp.delete());
+    }
+  }

 }

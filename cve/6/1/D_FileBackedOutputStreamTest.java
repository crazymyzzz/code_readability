
@@ -61,10 +69,21 @@ public class FileBackedOutputStreamTest extends IoTestCase {
 
     // Write data to go over the threshold
     if (chunk2 > 0) {
+      if (JAVA_IO_TMPDIR.value().equals("/sdcard")) {
+        assertThrows(IOException.class, () -> write(out, data, chunk1, chunk2, singleByte));
+        return;
+      }
       write(out, data, chunk1, chunk2, singleByte);
       file = out.getFile();
       assertEquals(dataSize, file.length());
       assertTrue(file.exists());
+      assertThat(file.getName()).contains("FileBackedOutputStream");
+      if (!isAndroid()) {
+        PosixFileAttributes attributes =
+            java.nio.file.Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class)
+                .readAttributes();
+        assertThat(attributes.permissions()).containsExactly(OWNER_READ, OWNER_WRITE);
+      }
     }
     out.close();
 
@@ -109,6 +128,10 @@ public class FileBackedOutputStreamTest extends IoTestCase {
     FileBackedOutputStream out = new FileBackedOutputStream(50);
     ByteSource source = out.asByteSource();
 
+    if (JAVA_IO_TMPDIR.value().equals("/sdcard")) {
+      assertThrows(IOException.class, () -> out.write(data));
+      return;
+    }
     out.write(data);
     assertTrue(Arrays.equals(data, source.read()));
 


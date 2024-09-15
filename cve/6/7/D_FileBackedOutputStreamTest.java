@@ -16,9 +16,17 @@
 
 package com.google.common.io;
 
+import static com.google.common.base.StandardSystemProperty.JAVA_IO_TMPDIR;
+import static com.google.common.truth.Truth.assertThat;
+import static java.nio.file.attribute.PosixFilePermission.OWNER_READ;
+import static java.nio.file.attribute.PosixFilePermission.OWNER_WRITE;
+import static org.junit.Assert.assertThrows;
+
 import java.io.File;
 import java.io.IOException;
 import java.io.OutputStream;
+import java.nio.file.attribute.PosixFileAttributeView;
+import java.nio.file.attribute.PosixFileAttributes;
 import java.util.Arrays;
 
 /**
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
 
@@ -140,4 +163,8 @@ public class FileBackedOutputStreamTest extends IoTestCase {
 
     out.close();
   }
+
+  private static boolean isAndroid() {
+    return System.getProperty("java.runtime.name", "").contains("Android");
+  }
 }

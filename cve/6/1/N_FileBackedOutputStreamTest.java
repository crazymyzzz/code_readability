
public class FileBackedOutputStreamTest extends IoTestCase {

  private void testThreshold(
      int fileThreshold, int dataSize, boolean singleByte, boolean resetOnFinalize)
      throws IOException {
    byte[] data = newPreFilledByteArray(dataSize);
    FileBackedOutputStream out = new FileBackedOutputStream(fileThreshold, resetOnFinalize);
    ByteSource source = out.asByteSource();
    int chunk1 = Math.min(dataSize, fileThreshold);
    int chunk2 = dataSize - chunk1;

    // Write just enough to not trip the threshold
    if (chunk1 > 0) {
      write(out, data, 0, chunk1, singleByte);
      assertTrue(ByteSource.wrap(data).slice(0, chunk1).contentEquals(source));
    }
    File file = out.getFile();
    assertNull(file);

    // Write data to go over the threshold
    if (chunk2 > 0) {
      if (JAVA_IO_TMPDIR.value().equals("/sdcard")) {
        assertThrows(IOException.class, () -> write(out, data, chunk1, chunk2, singleByte));
        return;
      }
      write(out, data, chunk1, chunk2, singleByte);
      file = out.getFile();
      assertEquals(dataSize, file.length());
      assertTrue(file.exists());
      assertThat(file.getName()).contains("FileBackedOutputStream");
      if (!isAndroid()) {
        PosixFileAttributes attributes =
            java.nio.file.Files.getFileAttributeView(file.toPath(), PosixFileAttributeView.class)
                .readAttributes();
        assertThat(attributes.permissions()).containsExactly(OWNER_READ, OWNER_WRITE);
      }
    }
    out.close();

    // Check that source returns the right data
    assertTrue(Arrays.equals(data, source.read()));

    // Make sure that reset deleted the file
    out.reset();
    if (file != null) {
      assertFalse(file.exists());
    }
  }



  // TODO(chrisn): only works if we ensure we have crossed file threshold

  public void testWriteErrorAfterClose() throws Exception {
    byte[] data = newPreFilledByteArray(100);
    FileBackedOutputStream out = new FileBackedOutputStream(50);
    ByteSource source = out.asByteSource();

    if (JAVA_IO_TMPDIR.value().equals("/sdcard")) {
      assertThrows(IOException.class, () -> out.write(data));
      return;
    }
    out.write(data);
    assertTrue(Arrays.equals(data, source.read()));

    out.close();
    try {
      out.write(42);
      fail("expected exception");
    } catch (IOException expected) {
    }

    // Verify that write had no effect
    assertTrue(Arrays.equals(data, source.read()));
    out.reset();
  }



}

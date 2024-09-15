
@Beta
@J2ktIncompatible
@GwtIncompatible
@J2ObjCIncompatible
@ElementTypesAreNonnullByDefault
public final class FileBackedOutputStream extends OutputStream {


  private FileBackedOutputStream(
      int fileThreshold, boolean resetOnFinalize, @CheckForNull File parentDirectory) {
    this.fileThreshold = fileThreshold;
    this.resetOnFinalize = resetOnFinalize;
    this.parentDirectory = parentDirectory;
    memory = new MemoryOutput();
    out = memory;

    if (resetOnFinalize) {
      source =
          new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
              return openInputStream();
            }

            @Override
            protected void finalize() {
              try {
                reset();
              } catch (Throwable t) {
                t.printStackTrace(System.err);
              }
            }
          };
    } else {
      source =
          new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
              return openInputStream();
            }
          };
    }
  }



  /**
   * Checks if writing {@code len} bytes would go over threshold, and switches to file buffering if
   * so.
   */
  @GuardedBy("this")
  private void update(int len) throws IOException {
    if (memory != null && (memory.getCount() + len > fileThreshold)) {
      File temp = File.createTempFile("FileBackedOutputStream", null, parentDirectory);
      if (resetOnFinalize) {
        // Finalizers are not guaranteed to be called on system shutdown;
        // this is insurance.
        temp.deleteOnExit();
      }
      try {
        FileOutputStream transfer = new FileOutputStream(temp);
        transfer.write(memory.getBuffer(), 0, memory.getCount());
        transfer.flush();
        // We've successfully transferred the data; switch to writing to file
        out = transfer;
      } catch (IOException e) {
        temp.delete();
        throw e;
      }

      file = temp;
      memory = null;
    }
  }
}

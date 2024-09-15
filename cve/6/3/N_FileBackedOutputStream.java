/**
 * An {@link OutputStream} that starts buffering to a byte array, but switches to file buffering
 * once the data reaches a configurable size.
 *
 * <p>When this stream creates a temporary file, it restricts the file's permissions to the current
 * user or, in the case of Android, the current app. If that is not possible (as is the case under
 * the very old Android Ice Cream Sandwich release), then this stream throws an exception instead of
 * creating a file that would be more accessible. (This behavior is new in Guava 32.0.0. Previous
 * versions would create a file that is more accessible, as discussed in <a
 * href="https://github.com/google/guava/issues/2575">Guava issue 2575</a>. TODO: b/283778848 - Fill
 * in CVE number once it's available.)
 *
 * <p>Temporary files created by this stream may live in the local filesystem until either:
 *
 * <ul>
 *   <li>{@link #reset} is called (removing the data in this stream and deleting the file), or...
 *   <li>this stream (or, more precisely, its {@link #asByteSource} view) is finalized during
 *       garbage collection, <strong>AND</strong> this stream was not constructed with {@linkplain
 *       #FileBackedOutputStream(int) the 1-arg constructor} or the {@linkplain
 *       #FileBackedOutputStream(int, boolean) 2-arg constructor} passing {@code false} in the
 *       second parameter.
 * </ul>
 *
 * <p>This class is thread-safe.
 *
 * @author Chris Nokleberg
 * @since 1.0
 */
@Beta
@J2ktIncompatible
@GwtIncompatible
@J2ObjCIncompatible
@ElementTypesAreNonnullByDefault
public final class FileBackedOutputStream extends OutputStream {


  /**
   * Creates a new instance that uses the given file threshold, and optionally resets the data when
   * the {@link ByteSource} returned by {@link #asByteSource} is finalized.
   *
   * @param fileThreshold the number of bytes before the stream should switch to buffering to a file
   * @param resetOnFinalize if true, the {@link #reset} method will be called when the {@link
   *     ByteSource} returned by {@link #asByteSource} is finalized.
   * @throws IllegalArgumentException if {@code fileThreshold} is negative
   */
  public FileBackedOutputStream(int fileThreshold, boolean resetOnFinalize) {
    checkArgument(
        fileThreshold >= 0, "fileThreshold must be non-negative, but was %s", fileThreshold);
    this.fileThreshold = fileThreshold;
    this.resetOnFinalize = resetOnFinalize;
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
      File temp = TempFileCreator.INSTANCE.createTempFile("FileBackedOutputStream");
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

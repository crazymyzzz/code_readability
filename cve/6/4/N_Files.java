
@J2ktIncompatible
@GwtIncompatible
@ElementTypesAreNonnullByDefault
public final class Files {



  /**
   * Atomically creates a new directory somewhere beneath the system's temporary directory (as
   * defined by the {@code java.io.tmpdir} system property), and returns its name.
   *
   * <p>The temporary directory is created with permissions restricted to the current user or, in
   * the case of Android, the current app. If that is not possible (as is the case under the very
   * old Android Ice Cream Sandwich release), then this method throws an exception instead of
   * creating a directory that would be more accessible. (This behavior is new in Guava 32.0.0.
   * Previous versions would create a directory that is more accessible, as discussed in <a
   * href="https://github.com/google/guava/issues/4011">CVE-2020-8908</a>.)
   *
   * <p>Use this method instead of {@link File#createTempFile(String, String)} when you wish to
   * create a directory, not a regular file. A common pitfall is to call {@code createTempFile},
   * delete the file and create a directory in its place, but this leads a race condition which can
   * be exploited to create security vulnerabilities, especially when executable files are to be
   * written into the directory.
   *
   * <p>This method assumes that the temporary volume is writable, has free inodes and free blocks,
   * and that it will not be called thousands of times per second.
   *
   * <p><b>{@link java.nio.file.Path} equivalent:</b> {@link
   * java.nio.file.Files#createTempDirectory}.
   *
   * @return the newly-created directory
   * @throws IllegalStateException if the directory could not be created
   * @throws UnsupportedOperationException if the system does not support creating temporary
   *     directories securely
   * @deprecated For Android users, see the <a
   *     href="https://developer.android.com/training/data-storage" target="_blank">Data and File
   *     Storage overview</a> to select an appropriate temporary directory (perhaps {@code
   *     context.getCacheDir()}), and create your own directory under that. (For example, you might
   *     use {@code new File(context.getCacheDir(), "directoryname").mkdir()}, or, if you need an
   *     arbitrary number of temporary directories, you might have to generate multiple directory
   *     names in a loop until {@code mkdir()} returns {@code true}.) For developers on Java 7 or
   *     later, use {@link java.nio.file.Files#createTempDirectory}, transforming it to a {@link
   *     File} using {@link java.nio.file.Path#toFile() toFile()} if needed. To restrict permissions
   *     as this method does, pass {@code
   *     PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------"))} to your
   *     call to {@code createTempDirectory}.
   */
  @Beta
  @Deprecated
  @J2ObjCIncompatible
  public static File createTempDir() {
    return TempFileCreator.INSTANCE.createTempDir();
  }

 
}

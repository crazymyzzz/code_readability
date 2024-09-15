

/**
 * Unit test for {@link Files#createTempDir}.
 *
 * @author Chris Nokleberg
 */

public class FilesCreateTempDirTest extends IoTestCase {
  public void testCreateTempDir() {
    File temp = Files.createTempDir();
    assertTrue(temp.exists());
    assertTrue(temp.isDirectory());
    assertThat(temp.listFiles()).isEmpty();
    assertTrue(temp.delete());
  }
}



public abstract class AbstractTestResourceSet {



    @Test
    public final void testWriteDirB() {
        WebResource d1 = resourceRoot.getResource(getMount() + "/d1/");
        InputStream is = new ByteArrayInputStream("test".getBytes());
        if (d1.exists()) {
            Assert.assertFalse(resourceRoot.write(getMount() + "/d1/", is, false));
        } else if (d1.isVirtual()) {
            Assert.assertTrue(resourceRoot.write(
                    getMount() + "/d1/", is, false));
            File file = new File(getBaseDir(), "d1");
            Assert.assertTrue(file.exists());
            Assert.assertTrue(file.delete());
        } else {
            Assert.fail("Unhandled condition in unit test");
        }
    }

  
}

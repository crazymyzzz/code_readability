

public abstract class AbstractTestResourceSet {


    @Test
    public final void testWriteDirB() {
        WebResource d1 = resourceRoot.getResource(getMount() + "/d1/");
        InputStream is = new ByteArrayInputStream("test".getBytes());
        if (d1.exists() || d1.isVirtual()) {
            Assert.assertFalse(resourceRoot.write(getMount() + "/d1/", is, false));
        } else {
            Assert.fail("Unhandled condition in unit test");
        }
    }

    
}

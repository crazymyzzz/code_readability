

public class Security218CliTest {

    @PresetData(PresetData.DataSet.ANONYMOUS_READONLY)
    @Test
    @Issue("SECURITY-218")
    public void probeCommonsCollections1() throws Exception {
        probe(Payload.CommonsCollections1, 1);
    }
    
    @PresetData(PresetData.DataSet.ANONYMOUS_READONLY)
    @Test
    @Issue("SECURITY-317")
    public void probeCommonsCollections3() throws Exception {
        probe(Payload.CommonsCollections3, 1);
    }


    @PresetData(PresetData.DataSet.ANONYMOUS_READONLY)
    @Test
    @Issue("SECURITY-317")
    public void probeCommonsCollections5() throws Exception {
        probe(Payload.CommonsCollections5, 1);
    }

    @PresetData(PresetData.DataSet.ANONYMOUS_READONLY)
    @Test
    @Issue("SECURITY-317")
    public void probeCommonsCollections6() throws Exception {
        probe(Payload.CommonsCollections6, 1);
    }

    
}

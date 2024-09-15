

public class JsonDeserializationTest {


    @Test(description = "Deserialize ref callback")
    public void testDeserializeRefCallback() throws Exception {
        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /simplecallback:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback1:\n" +
                "          $ref: '#/components/callbacks/Callback'\n" +
                "      callbacks:\n" +
                "        testCallback1:\n" +
                "          $ref: '#/components/callbacks/Callback'\n" +
                "components:\n" +
                "  callbacks:\n" +
                "    Callback:\n" +
                "      /post:\n" +
                "        description: Post Path Item\n";

        OpenAPI oas = Yaml.mapper().readValue(yaml, OpenAPI.class);
        assertEquals(oas.getPaths().get("/simplecallback").getGet().getCallbacks().get("testCallback1").get$ref(), "#/components/callbacks/Callback");
    }

    
}

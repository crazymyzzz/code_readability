

public class MapPropertyDeserializerTest {


    @Test(description = "it should read an example within an inlined schema")
    public void testIssue1261InlineSchemaExample() throws Exception {
        Operation operation = Yaml.mapper().readValue(
                "      responses:\n" +
                        "        \"200\":\n" +
                        "          content:\n" +
                        "            '*/*':\n" +
                        "              description: OK\n" +
                        "              schema:\n" +
                        "                type: object\n" +
                        "                properties:\n" +
                        "                  id:\n" +
                        "                    type: integer\n" +
                        "                    format: int32\n" +
                        "                  name:\n" +
                        "                    type: string\n" +
                        "                required: [id, name]\n" +
                        "                example: ok", Operation.class);

        ApiResponse response = operation.getResponses().get("200");
        assertNotNull(response);
        Schema schema = response.getContent().get("*/*").getSchema();
        Object example = schema.getExample();
        assertNotNull(example);
        assertTrue(example instanceof String);
        assertEquals(example, "ok");
    }
}
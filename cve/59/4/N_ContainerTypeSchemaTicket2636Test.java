

public class ContainerTypeSchemaTicket2636Test extends AbstractAnnotationTest {

    @Test
    public void testContainerTypeSchemaTicket2636() throws Exception {
        String expectedYaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /path:\n" +
                "    get:\n" +
                "      summary: Op\n" +
                "      description: 'RequestBody contains a Schema class that extends a Map '\n" +
                "      operationId: getWithNoParameters\n" +
                "      requestBody:\n" +
                "        content:\n" +
                "          application/json:\n" +
                "            schema:\n" +
                "              $ref: '#/components/schemas/MyModel'\n" +
                "        required: true\n" +
                "      responses:\n" +
                "        \"200\":\n" +
                "          description: voila!\n" +
                "components:\n" +
                "  schemas:\n" +
                "    MyModel:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        empty:\n" +
                "          type: boolean\n" +
                "      additionalProperties:\n" +
                "        type: string";
        compareAsYaml(RequestBodyInheritanceModelIssue.class, expectedYaml);
    }

    
}

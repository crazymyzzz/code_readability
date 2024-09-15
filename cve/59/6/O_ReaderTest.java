

/**
 * Test for the Reader Class
 */
public class ReaderTest {


    @Test(description = "More Responses")
    public void testMoreResponses() {
        Reader reader = new Reader(new OpenAPI());
        OpenAPI openAPI = reader.read(EnhancedResponsesResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        404:\n" +
                "          description: not found!\n" +
                "        400:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n";

        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses with composition")
    public void testGetResponsesWithComposition() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(ResponsesResource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "      deprecated: true\n" +
                "  /allOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getAllOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                allOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "  /anyOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getAnyOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                anyOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "  /oneOf:\n" +
                "    get:\n" +
                "      summary: Test inheritance / polymorphism\n" +
                "      operationId: getOneOf\n" +
                "      parameters:\n" +
                "      - name: number\n" +
                "        in: query\n" +
                "        description: Test inheritance / polymorphism\n" +
                "        required: true\n" +
                "        schema:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        example: 1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: bean answer\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                oneOf:\n" +
                "                - $ref: '#/components/schemas/MultipleSub1Bean'\n" +
                "                - $ref: '#/components/schemas/MultipleSub2Bean'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    MultipleSub1Bean:\n" +
                "      type: object\n" +
                "      description: MultipleSub1Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          c:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "    MultipleSub2Bean:\n" +
                "      type: object\n" +
                "      description: MultipleSub2Bean\n" +
                "      allOf:\n" +
                "      - $ref: '#/components/schemas/MultipleBaseBean'\n" +
                "      - type: object\n" +
                "        properties:\n" +
                "          d:\n" +
                "            type: integer\n" +
                "            format: int32\n" +
                "    MultipleBaseBean:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        beanType:\n" +
                "          type: string\n" +
                "        a:\n" +
                "          type: integer\n" +
                "          format: int32\n" +
                "        b:\n" +
                "          type: string\n" +
                "      description: MultipleBaseBean";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }


    /*
    TODO: in a scenario like the one in ticket 2793, currently no NPE is thrown
    but map is still not supported. When solved, update expected yaml in test case accordingly
     */
    @Test(description = "no NPE resolving map")
    public void testTicket2793() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket2793Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /distances:\n" +
                "    get:\n" +
                "      operationId: getDistances\n" +
                "      responses:\n" +
                "        200:\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/DistancesResponse'\n" +
                "components:\n" +
                "  schemas:\n" +
                "    DistancesResponse:\n" +
                "      type: object\n" +
                "      properties:\n" +
                "        empty:\n" +
                "          type: boolean\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }



    @Test(description = "Responses with ref")
    public void testResponseWithRef() {
        Components components = new Components();
        components.addResponses("invalidJWT", new ApiResponse().description("when JWT token invalid/expired"));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);

        OpenAPI openAPI = reader.read(RefResponsesResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "        401:\n" +
                "          $ref: '#/components/responses/invalidJWT'\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n" +
                "  responses:\n" +
                "    invalidJWT:\n" +
                "      description: when JWT token invalid/expired";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test(description = "Responses with filter")
    public void testResponseWithFilter() {
        Components components = new Components();
        components.addResponses("invalidJWT", new ApiResponse().description("when JWT token invalid/expired"));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);
        Reader reader = new Reader(oas);

        OpenAPI openAPI = reader.read(SimpleResponsesResource.class);


        OpenAPISpecFilter filterImpl = new RefResponseFilter();
        SpecFilter f = new SpecFilter();
        openAPI = f.filter(openAPI, filterImpl, null, null, null);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "        object\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      parameters: []\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          content:\n" +
                "            application/json:\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
                "        default:\n" +
                "          description: boo\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                $ref: '#/components/schemas/GenericError'\n" +
                "        401:\n" +
                "          $ref: '#/components/responses/invalidJWT'\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  schemas:\n" +
                "    GenericError:\n" +
                "      type: object\n" +
                "    SampleResponseSchema:\n" +
                "      type: object\n" +
                "  responses:\n" +
                "    invalidJWT:\n" +
                "      description: when JWT token invalid/expired";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }



    @Test(description = "Header with Ref")
    public void testHeaderWithRef() {
        Components components = new Components();
        components.addHeaders("Header", new Header().description("Header Description"));

        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefHeaderResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /path:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      description: Defines a simple get operation with no inputs and a complex output\n" +
                "      operationId: getWithPayloadResponse\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "          headers:\n" +
                "            Rate-Limit-Limit:\n" +
                "              description: The number of allowed requests in the current period\n" +
                "              $ref: '#/components/headers/Header'\n" +
                "              style: simple\n" +
                "              schema:\n" +
                "                type: integer\n" +
                "      deprecated: true\n" +
                "components:\n" +
                "  headers:\n" +
                "    Header:\n" +
                "      description: Header Description\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }



    @Test(description = "Callback with Ref")
    public void testCallbackWithRef() {
        Components components = new Components();
        components.addCallbacks("Callback", new Callback().addPathItem("/post", new PathItem().description("Post Path Item")));
        OpenAPI oas = new OpenAPI()
                .info(new Info().description("info"))
                .components(components);

        Reader reader = new Reader(oas);
        OpenAPI openAPI = reader.read(RefCallbackResource.class);

        String yaml = "openapi: 3.0.1\n" +
                "info:\n" +
                "  description: info\n" +
                "paths:\n" +
                "  /simplecallback:\n" +
                "    get:\n" +
                "      summary: Simple get operation\n" +
                "      operationId: getWithNoParameters\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: voila!\n" +
                "      callbacks:\n" +
                "        testCallback1:\n" +
                "          $ref: '#/components/callbacks/Callback'\n" +
                "components:\n" +
                "  callbacks:\n" +
                "    Callback:\n" +
                "      /post:\n" +
                "        description: Post Path Item\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
    }

    @Test
    public void testTicket3015() {
        Reader reader = new Reader(new OpenAPI());

        OpenAPI openAPI = reader.read(Ticket3015Resource.class);
        String yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/test:\n" +
                "    get:\n" +
                "      operationId: schemaImpl\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: string\n" +
                "                format: uri\n" +
                "        400:\n" +
                "          description: Bad Request\n" +
                "        500:\n" +
                "          description: Internal Server Error\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        PrimitiveType.customExcludedClasses().add(URI.class.getName());
        openAPI = reader.read(Ticket3015Resource.class);
        yaml = "openapi: 3.0.1\n" +
                "paths:\n" +
                "  /test/test:\n" +
                "    get:\n" +
                "      operationId: schemaImpl_1\n" +
                "      responses:\n" +
                "        200:\n" +
                "          description: OK\n" +
                "          content:\n" +
                "            '*/*':\n" +
                "              schema:\n" +
                "                type: object\n" +
                "                properties:\n" +
                "                  scheme:\n" +
                "                    type: string\n" +
                "                  fragment:\n" +
                "                    type: string\n" +
                "                  authority:\n" +
                "                    type: string\n" +
                "                  userInfo:\n" +
                "                    type: string\n" +
                "                  host:\n" +
                "                    type: string\n" +
                "                  port:\n" +
                "                    type: integer\n" +
                "                    format: int32\n" +
                "                  path:\n" +
                "                    type: string\n" +
                "                  query:\n" +
                "                    type: string\n" +
                "                  schemeSpecificPart:\n" +
                "                    type: string\n" +
                "                  rawSchemeSpecificPart:\n" +
                "                    type: string\n" +
                "                  rawAuthority:\n" +
                "                    type: string\n" +
                "                  rawUserInfo:\n" +
                "                    type: string\n" +
                "                  rawPath:\n" +
                "                    type: string\n" +
                "                  rawQuery:\n" +
                "                    type: string\n" +
                "                  rawFragment:\n" +
                "                    type: string\n" +
                "                  absolute:\n" +
                "                    type: boolean\n" +
                "                  opaque:\n" +
                "                    type: boolean\n" +
                "        400:\n" +
                "          description: Bad Request\n" +
                "        500:\n" +
                "          description: Internal Server Error\n";
        SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
        PrimitiveType.customExcludedClasses().remove(URI.class.getName());
    }




}
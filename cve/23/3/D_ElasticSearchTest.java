@@ -47,8 +47,8 @@ public abstract class ElasticSearchTest {
 
     protected static final ElasticsearchContainer container =
             new ElasticsearchContainer(
-                    DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch-oss")
-                            .withTag("7.10.2")); // this should match the client version
+                    DockerImageName.parse("elasticsearch")
+                            .withTag("7.17.16")); // this should match the client version
 
     @Autowired protected ObjectMapper objectMapper;
 

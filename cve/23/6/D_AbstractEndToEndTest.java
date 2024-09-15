@@ -65,8 +65,8 @@ public abstract class AbstractEndToEndTest {
 
     private static final ElasticsearchContainer container =
             new ElasticsearchContainer(
-                    DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch-oss")
-                            .withTag("7.10.2")); // this should match the client version
+                    DockerImageName.parse("elasticsearch")
+                            .withTag("7.17.16")); // this should match the client version
 
     private static RestClient restClient;
 

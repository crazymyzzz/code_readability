

@TestPropertySource(
        properties = {
            "conductor.indexing.enabled=true",
            "conductor.elasticsearch.version=7",
            "conductor.queue.type=xxx"
        })
public abstract class AbstractEndToEndTest {

    private static final ElasticsearchContainer container =
            new ElasticsearchContainer(
                    DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch-oss")
                            .withTag("7.10.2")); // this should match the client version

    
}

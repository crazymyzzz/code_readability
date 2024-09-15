

@TestPropertySource(
        properties = {
            "conductor.indexing.enabled=true",
            "conductor.elasticsearch.version=7",
            "conductor.queue.type=xxx"
        })
public abstract class AbstractEndToEndTest {


    private static final ElasticsearchContainer container =
            new ElasticsearchContainer(
                    DockerImageName.parse("elasticsearch")
                            .withTag("7.17.16")); // this should match the client version

    
}

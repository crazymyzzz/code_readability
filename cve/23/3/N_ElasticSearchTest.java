

@ContextConfiguration(
        classes = {TestObjectMapperConfiguration.class, ElasticSearchTest.TestConfiguration.class})
@RunWith(SpringRunner.class)
@TestPropertySource(
        properties = {"conductor.indexing.enabled=true", "conductor.elasticsearch.version=7"})
public abstract class ElasticSearchTest {

    

    protected static final ElasticsearchContainer container =
            new ElasticsearchContainer(
                    DockerImageName.parse("elasticsearch")
                            .withTag("7.17.16")); // this should match the client version

    
}

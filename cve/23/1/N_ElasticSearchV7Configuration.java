

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ElasticSearchProperties.class)
@Conditional(ElasticSearchConditions.ElasticSearchV7Enabled.class)
public class ElasticSearchV7Configuration {


    @Bean
    public RestClientBuilder elasticRestClientBuilder(ElasticSearchProperties properties) {
        RestClientBuilder builder = RestClient.builder(convertToHttpHosts(properties.toURLs()));

        if (properties.getUsername() != null && properties.getPassword() != null) {
            log.info(
                    "Configure ElasticSearch with BASIC authentication. User:{}",
                    properties.getUsername());
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(
                            properties.getUsername(), properties.getPassword()));
            builder.setHttpClientConfigCallback(
                    httpClientBuilder ->
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        } else {
            log.info("Configure ElasticSearch with no authentication.");
        }
        return builder;
    }

    
}

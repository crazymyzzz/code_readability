@@ -59,7 +59,7 @@ public class ElasticSearchV7Configuration {
     }
 
     @Bean
-    public RestClientBuilder restClientBuilder(ElasticSearchProperties properties) {
+    public RestClientBuilder elasticRestClientBuilder(ElasticSearchProperties properties) {
         RestClientBuilder builder = RestClient.builder(convertToHttpHosts(properties.toURLs()));
 
         if (properties.getUsername() != null && properties.getPassword() != null) {

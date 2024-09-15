
/**
 * This is {@link CoreSamlConfiguration}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
@Configuration("coreSamlConfiguration")
@EnableConfigurationProperties(CasConfigurationProperties.class)
public class CoreSamlConfiguration {


    @Lazy
    @Bean(name = "shibboleth.VelocityEngine")
    public VelocityEngine velocityEngineFactoryBean() {
        val properties = new Properties();
        properties.put(RuntimeConstants.INPUT_ENCODING, StandardCharsets.UTF_8.name());
        properties.put(RuntimeConstants.ENCODING_DEFAULT, StandardCharsets.UTF_8.name());
        properties.put(RuntimeConstants.RESOURCE_LOADERS, "file, classpath, string");
        properties.put(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, FileUtils.getTempDirectory().getAbsolutePath());
        properties.put(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, Boolean.FALSE);
        properties.put("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
        properties.put("resource.loader.string.class", StringResourceLoader.class.getName());
        properties.put("resource.loader.file.class", FileResourceLoader.class.getName());

        return new VelocityEngine(properties);
    }

    
}

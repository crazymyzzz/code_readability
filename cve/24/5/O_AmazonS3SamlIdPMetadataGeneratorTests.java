

/**
 * This is {@link AmazonS3SamlIdPMetadataGeneratorTests}.
 *
 * @author Misagh Moayyed
 * @since 6.0.0
 */
@SpringBootTest(classes = {
    RefreshAutoConfiguration.class,
    RefreshAutoConfiguration.class,
    CasCoreWebflowConfiguration.class,
    CasWebflowContextConfiguration.class,
    CasCoreServicesConfiguration.class,
    CasCoreWebConfiguration.class,
    CasCoreHttpConfiguration.class,
    CasCoreConfiguration.class,
    CasCoreAuthenticationConfiguration.class,
    CasCoreTicketsConfiguration.class,
    CasRegisteredServicesTestConfiguration.class,
    CasCoreTicketCatalogConfiguration.class,
    CasCookieConfiguration.class,
    CasWebApplicationServiceFactoryConfiguration.class,
    CasCoreAuthenticationSupportConfiguration.class,
    CasCoreAuthenticationServiceSelectionStrategyConfiguration.class,
    CasCoreUtilConfiguration.class,
    AmazonS3SamlMetadataConfiguration.class,
    AmazonS3SamlIdPMetadataConfiguration.class,
    SamlIdPMetadataConfiguration.class
})
@TestPropertySource(properties = {
    "cas.authn.samlIdp.metadata.amazonS3.idpMetadataBucketName=thebucket",
    "cas.authn.samlIdp.metadata.amazonS3.endpoint=http://127.0.0.1:4572",
    "cas.authn.samlIdp.metadata.amazonS3.credentialAccessKey=test",
    "cas.authn.samlIdp.metadata.amazonS3.credentialSecretKey=test",
    "cas.authn.samlIdp.metadata.amazonS3.crypto.encryption.key=AZ5y4I9qzKPYUVNL2Td4RMbpg6Z-ldui8VEFg8hsj1M",
    "cas.authn.samlIdp.metadata.amazonS3.crypto.signing.key=cAPyoHMrOMWrwydOXzBA-ufZQM-TilnLjbRgMQWlUlwFmy07bOtAgCIdNBma3c5P4ae_JV6n1OpOAYqSh2NkmQ"
})
@EnabledIfPortOpen(port = 4572)
@EnabledIfContinuousIntegration
@Tag("AmazonWebServices")
public class AmazonS3SamlIdPMetadataGeneratorTests {
    static {
        System.setProperty(SkipMd5CheckStrategy.DISABLE_GET_OBJECT_MD5_VALIDATION_PROPERTY, "true");
        System.setProperty("com.amazonaws.services.s3.disableGetObjectMD5Validation", "true");
    }

    @Autowired
    @Qualifier("samlIdPMetadataLocator")
    protected SamlIdPMetadataLocator samlIdPMetadataLocator;
    @Autowired
    @Qualifier("samlIdPMetadataGenerator")
    private SamlIdPMetadataGenerator samlIdPMetadataGenerator;

    @Test
    public void verifyOperation() {
        samlIdPMetadataGenerator.generate();
        assertNotNull(samlIdPMetadataLocator.getMetadata());
        assertNotNull(samlIdPMetadataLocator.getEncryptionCertificate());
        assertNotNull(samlIdPMetadataLocator.getEncryptionKey());
        assertNotNull(samlIdPMetadataLocator.getSigningCertificate());
        assertNotNull(samlIdPMetadataLocator.getSigningKey());
    }
}

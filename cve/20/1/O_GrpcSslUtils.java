
@SuppressWarnings("OptionalIsPresent")
public class GrpcSslUtils {


    /**
     * Get an {@code HttpServerOptions} for this server configuration, or null if SSL should not be enabled.
     *
     * @return whether plain text is used.
     */
    static boolean applySslOptions(GrpcServerConfiguration config, HttpServerOptions options)
            throws IOException {

        // Disable plain-text is the ssl configuration is set.
        if ((config.ssl.certificate.isPresent() || config.ssl.keyStore.isPresent())
                && config.plainText) {
            LOGGER.info("Disabling gRPC plain-text as the SSL certificate is configured");
            config.plainText = false;
        }

        if (config.plainText) {
            options.setSsl(false);
            return true;
        } else {
            options.setSsl(true);
        }

        SslServerConfig sslConfig = config.ssl;
        final Optional<Path> certFile = sslConfig.certificate;
        final Optional<Path> keyFile = sslConfig.key;
        final Optional<Path> keyStoreFile = sslConfig.keyStore;
        final Optional<Path> trustStoreFile = sslConfig.trustStore;
        final Optional<String> trustStorePassword = sslConfig.trustStorePassword;

        options.setUseAlpn(config.alpn);
        if (config.alpn) {
            options.setAlpnVersions(Arrays.asList(HttpVersion.HTTP_2, HttpVersion.HTTP_1_1));
        }

        if (certFile.isPresent() && keyFile.isPresent()) {
            createPemKeyCertOptions(certFile.get(), keyFile.get(), options);
        } else if (keyStoreFile.isPresent()) {
            final Path keyStorePath = keyStoreFile.get();
            final Optional<String> keyStoreFileType = sslConfig.keyStoreType;
            String type;
            if (keyStoreFileType.isPresent()) {
                type = keyStoreFileType.get().toLowerCase();
            } else {
                type = findKeystoreFileType(keyStorePath);
            }

            byte[] data = getFileContent(keyStorePath);
            switch (type) {
                case "pkcs12": {
                    PfxOptions o = new PfxOptions()
                            .setValue(Buffer.buffer(data));
                    if (sslConfig.keyStorePassword.isPresent()) {
                        o.setPassword(sslConfig.keyStorePassword.get());
                    }
                    options.setPfxKeyCertOptions(o);
                    break;
                }
                case "jks": {
                    JksOptions o = new JksOptions()
                            .setValue(Buffer.buffer(data));
                    if (sslConfig.keyStorePassword.isPresent()) {
                        o.setPassword(sslConfig.keyStorePassword.get());
                    }
                    options.setKeyStoreOptions(o);
                    break;
                }
                default:
                    throw new IllegalArgumentException(
                            "Unknown keystore type: " + type + " valid types are jks or pkcs12");
            }

        }

        if (trustStoreFile.isPresent()) {
            if (trustStorePassword.isEmpty()) {
                throw new IllegalArgumentException("No trust store password provided");
            }
            String type;
            final Optional<String> trustStoreFileType = sslConfig.trustStoreType;
            final Path trustStoreFilePath = trustStoreFile.get();
            if (trustStoreFileType.isPresent()) {
                type = trustStoreFileType.get();
            } else {
                type = findKeystoreFileType(trustStoreFilePath);
            }
            createTrustStoreOptions(trustStoreFilePath, trustStorePassword.get(), type, options);
        }

        for (String cipher : sslConfig.cipherSuites.orElse(Collections.emptyList())) {
            options.addEnabledCipherSuite(cipher);
        }

        for (String protocol : sslConfig.protocols) {
            if (!protocol.isEmpty()) {
                options.addEnabledSecureTransportProtocol(protocol);
            }
        }
        options.setClientAuth(sslConfig.clientAuth);
        return false;
    }

    
}

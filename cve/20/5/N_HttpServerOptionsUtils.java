

public class HttpServerOptionsUtils {

    /**
     * Get an {@code HttpServerOptions} for this server configuration, or null if SSL should not be enabled
     */
    public static HttpServerOptions createSslOptions(HttpBuildTimeConfig buildTimeConfig, HttpConfiguration httpConfiguration,
            LaunchMode launchMode, List<String> websocketSubProtocols)
            throws IOException {
        if (!httpConfiguration.hostEnabled) {
            return null;
        }

        ServerSslConfig sslConfig = httpConfiguration.ssl;

        final Optional<Path> certFile = sslConfig.certificate.file;
        final Optional<Path> keyFile = sslConfig.certificate.keyFile;
        final List<Path> keys = new ArrayList<>();
        final List<Path> certificates = new ArrayList<>();
        if (sslConfig.certificate.keyFiles.isPresent()) {
            keys.addAll(sslConfig.certificate.keyFiles.get());
        }
        if (sslConfig.certificate.files.isPresent()) {
            certificates.addAll(sslConfig.certificate.files.get());
        }
        if (keyFile.isPresent()) {
            keys.add(keyFile.get());
        }
        if (certFile.isPresent()) {
            certificates.add(certFile.get());
        }

        // credentials provider
        Map<String, String> credentials = Map.of();
        if (sslConfig.certificate.credentialsProvider.isPresent()) {
            String beanName = sslConfig.certificate.credentialsProviderName.orElse(null);
            CredentialsProvider credentialsProvider = CredentialsProviderFinder.find(beanName);
            String name = sslConfig.certificate.credentialsProvider.get();
            credentials = credentialsProvider.getCredentials(name);
        }
        final Optional<Path> keyStoreFile = sslConfig.certificate.keyStoreFile;
        final Optional<String> keyStorePassword = getCredential(sslConfig.certificate.keyStorePassword, credentials,
                sslConfig.certificate.keyStorePasswordKey);
        final Optional<String> keyStoreKeyPassword = getCredential(sslConfig.certificate.keyStoreKeyPassword, credentials,
                sslConfig.certificate.keyStoreKeyPasswordKey);
        final Optional<Path> trustStoreFile = sslConfig.certificate.trustStoreFile;
        final Optional<String> trustStorePassword = getCredential(sslConfig.certificate.trustStorePassword, credentials,
                sslConfig.certificate.trustStorePasswordKey);
        final HttpServerOptions serverOptions = new HttpServerOptions();

        //ssl
        if (JdkSSLEngineOptions.isAlpnAvailable()) {
            serverOptions.setUseAlpn(httpConfiguration.http2);
            if (httpConfiguration.http2) {
                serverOptions.setAlpnVersions(Arrays.asList(HttpVersion.HTTP_2, HttpVersion.HTTP_1_1));
            }
        }
        setIdleTimeout(httpConfiguration, serverOptions);

        if (!certificates.isEmpty() && !keys.isEmpty()) {
            createPemKeyCertOptions(certificates, keys, serverOptions);
        } else if (keyStoreFile.isPresent()) {
            KeyStoreOptions options = createKeyStoreOptions(
                    keyStoreFile.get(),
                    keyStorePassword.orElse("password"),
                    sslConfig.certificate.keyStoreFileType,
                    sslConfig.certificate.keyStoreProvider,
                    sslConfig.certificate.keyStoreKeyAlias,
                    keyStoreKeyPassword);
            serverOptions.setKeyCertOptions(options);
        }

        if (trustStoreFile.isPresent()) {
            if (!trustStorePassword.isPresent()) {
                throw new IllegalArgumentException("No trust store password provided");
            }
            KeyStoreOptions options = createKeyStoreOptions(
                    trustStoreFile.get(),
                    trustStorePassword.get(),
                    sslConfig.certificate.trustStoreFileType,
                    sslConfig.certificate.trustStoreProvider,
                    sslConfig.certificate.trustStoreCertAlias,
                    Optional.empty());
            serverOptions.setTrustOptions(options);
        }

        for (String cipher : sslConfig.cipherSuites.orElse(Collections.emptyList())) {
            serverOptions.addEnabledCipherSuite(cipher);
        }

        serverOptions.setEnabledSecureTransportProtocols(sslConfig.protocols);
        serverOptions.setSsl(true);
        serverOptions.setSni(sslConfig.sni);
        int sslPort = httpConfiguration.determineSslPort(launchMode);
        // -2 instead of -1 (see http) to have vert.x assign two different random ports if both http and https shall be random
        serverOptions.setPort(sslPort == 0 ? -2 : sslPort);
        serverOptions.setClientAuth(buildTimeConfig.tlsClientAuth);

        applyCommonOptions(serverOptions, buildTimeConfig, httpConfiguration, websocketSubProtocols);

        return serverOptions;
    }

    /**
     * Get an {@code HttpServerOptions} for this server configuration, or null if SSL should not be enabled
     */
    public static HttpServerOptions createSslOptionsForManagementInterface(ManagementInterfaceBuildTimeConfig buildTimeConfig,
            ManagementInterfaceConfiguration httpConfiguration,
            LaunchMode launchMode, List<String> websocketSubProtocols)
            throws IOException {
        if (!httpConfiguration.hostEnabled) {
            return null;
        }

        ServerSslConfig sslConfig = httpConfiguration.ssl;

        final Optional<Path> certFile = sslConfig.certificate.file;
        final Optional<Path> keyFile = sslConfig.certificate.keyFile;
        final List<Path> keys = new ArrayList<>();
        final List<Path> certificates = new ArrayList<>();
        if (sslConfig.certificate.keyFiles.isPresent()) {
            keys.addAll(sslConfig.certificate.keyFiles.get());
        }
        if (sslConfig.certificate.files.isPresent()) {
            certificates.addAll(sslConfig.certificate.files.get());
        }
        if (keyFile.isPresent()) {
            keys.add(keyFile.get());
        }
        if (certFile.isPresent()) {
            certificates.add(certFile.get());
        }

        // credentials provider
        Map<String, String> credentials = Map.of();
        if (sslConfig.certificate.credentialsProvider.isPresent()) {
            String beanName = sslConfig.certificate.credentialsProviderName.orElse(null);
            CredentialsProvider credentialsProvider = CredentialsProviderFinder.find(beanName);
            String name = sslConfig.certificate.credentialsProvider.get();
            credentials = credentialsProvider.getCredentials(name);
        }
        final Optional<Path> keyStoreFile = sslConfig.certificate.keyStoreFile;
        final Optional<String> keyStorePassword = getCredential(sslConfig.certificate.keyStorePassword, credentials,
                sslConfig.certificate.keyStorePasswordKey);
        final Optional<String> keyStoreKeyPassword = getCredential(sslConfig.certificate.keyStoreKeyPassword, credentials,
                sslConfig.certificate.keyStoreKeyPasswordKey);
        final Optional<Path> trustStoreFile = sslConfig.certificate.trustStoreFile;
        final Optional<String> trustStorePassword = getCredential(sslConfig.certificate.trustStorePassword, credentials,
                sslConfig.certificate.trustStorePasswordKey);
        final HttpServerOptions serverOptions = new HttpServerOptions();

        //ssl
        if (JdkSSLEngineOptions.isAlpnAvailable()) {
            serverOptions.setUseAlpn(true);
            serverOptions.setAlpnVersions(Arrays.asList(HttpVersion.HTTP_2, HttpVersion.HTTP_1_1));
        }
        int idleTimeout = (int) httpConfiguration.idleTimeout.toMillis();
        serverOptions.setIdleTimeout(idleTimeout);
        serverOptions.setIdleTimeoutUnit(TimeUnit.MILLISECONDS);

        if (!certificates.isEmpty() && !keys.isEmpty()) {
            createPemKeyCertOptions(certificates, keys, serverOptions);
        } else if (keyStoreFile.isPresent()) {
            KeyStoreOptions options = createKeyStoreOptions(
                    keyStoreFile.get(),
                    keyStorePassword.orElse("password"),
                    sslConfig.certificate.keyStoreFileType,
                    sslConfig.certificate.keyStoreProvider,
                    sslConfig.certificate.keyStoreKeyAlias,
                    keyStoreKeyPassword);
            serverOptions.setKeyCertOptions(options);
        }

        if (trustStoreFile.isPresent()) {
            if (!trustStorePassword.isPresent()) {
                throw new IllegalArgumentException("No trust store password provided");
            }
            KeyStoreOptions options = createKeyStoreOptions(
                    trustStoreFile.get(),
                    trustStorePassword.get(),
                    sslConfig.certificate.trustStoreFileType,
                    sslConfig.certificate.trustStoreProvider,
                    sslConfig.certificate.trustStoreCertAlias,
                    Optional.empty());
            serverOptions.setTrustOptions(options);
        }

        for (String cipher : sslConfig.cipherSuites.orElse(Collections.emptyList())) {
            serverOptions.addEnabledCipherSuite(cipher);
        }

        serverOptions.setEnabledSecureTransportProtocols(sslConfig.protocols);

        serverOptions.setSsl(true);
        serverOptions.setSni(sslConfig.sni);
        int sslPort = httpConfiguration.determinePort(launchMode);
        // -2 instead of -1 (see http) to have vert.x assign two different random ports if both http and https shall be random
        serverOptions.setPort(sslPort == 0 ? -2 : sslPort);
        serverOptions.setClientAuth(buildTimeConfig.tlsClientAuth);

        applyCommonOptionsForManagementInterface(serverOptions, buildTimeConfig, httpConfiguration, websocketSubProtocols);

        return serverOptions;
    }

    
}

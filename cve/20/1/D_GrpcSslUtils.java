@@ -117,12 +117,7 @@ static boolean applySslOptions(GrpcServerConfiguration config, HttpServerOptions
         for (String cipher : sslConfig.cipherSuites.orElse(Collections.emptyList())) {
             options.addEnabledCipherSuite(cipher);
         }
-
-        for (String protocol : sslConfig.protocols) {
-            if (!protocol.isEmpty()) {
-                options.addEnabledSecureTransportProtocol(protocol);
-            }
-        }
+        options.setEnabledSecureTransportProtocols(sslConfig.protocols);
         options.setClientAuth(sslConfig.clientAuth);
         return false;
     }

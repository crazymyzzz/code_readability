@@ -119,11 +119,7 @@ public static HttpServerOptions createSslOptions(HttpBuildTimeConfig buildTimeCo
             serverOptions.addEnabledCipherSuite(cipher);
         }
 
-        for (String protocol : sslConfig.protocols) {
-            if (!protocol.isEmpty()) {
-                serverOptions.addEnabledSecureTransportProtocol(protocol);
-            }
-        }
+        serverOptions.setEnabledSecureTransportProtocols(sslConfig.protocols);
         serverOptions.setSsl(true);
         serverOptions.setSni(sslConfig.sni);
         int sslPort = httpConfiguration.determineSslPort(launchMode);
@@ -224,11 +220,8 @@ public static HttpServerOptions createSslOptionsForManagementInterface(Managemen
             serverOptions.addEnabledCipherSuite(cipher);
         }
 
-        for (String protocol : sslConfig.protocols) {
-            if (!protocol.isEmpty()) {
-                serverOptions.addEnabledSecureTransportProtocol(protocol);
-            }
-        }
+        serverOptions.setEnabledSecureTransportProtocols(sslConfig.protocols);
+
         serverOptions.setSsl(true);
         serverOptions.setSni(sslConfig.sni);
         int sslPort = httpConfiguration.determinePort(launchMode);

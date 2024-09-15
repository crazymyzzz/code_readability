@@ -133,7 +133,7 @@ public class TlsHelper {
                 SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
                 return SslContextBuilder
                     .forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey())
-                    .sslProvider(SslProvider.JDK)
+                    .sslProvider(provider)
                     .clientAuth(ClientAuth.OPTIONAL)
                     .build();
             } else {

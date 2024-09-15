
 
@@ -168,8 +194,8 @@ public class JSSESocketFactory implements ServerSocketFactory {
         if (session.getCipherSuite().equals("SSL_NULL_WITH_NULL_NULL"))
             throw new IOException("SSL handshake failed. Ciper suite in SSL Session is SSL_NULL_WITH_NULL_NULL");
 
-        if (!allowUnsafeLegacyRenegotiation) {
-            // Prevent futher handshakes by removing all cipher suites
+        if (!allowUnsafeLegacyRenegotiation && !RFC_5746_SUPPORTED) {
+            // Prevent further handshakes by removing all cipher suites
             ((SSLSocket) sock).setEnabledCipherSuites(new String[0]);
         }
     }

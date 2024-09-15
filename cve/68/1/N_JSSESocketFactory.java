

/**
 * SSL server socket factory. It <b>requires</b> a valid RSA key and
 * JSSE.<br/>
 * keytool -genkey -alias tomcat -keyalg RSA</br>
 * Use "changeit" as password (this is the default we use).
 * 
 * @author Harish Prabandham
 * @author Costin Manolache
 * @author Stefan Freyr Stefansson
 * @author EKR -- renamed to JSSESocketFactory
 * @author Jan Luehe
 * @author Bill Barker
 */
public class JSSESocketFactory implements ServerSocketFactory {


    
    @Override
    public void handshake(Socket sock) throws IOException {
        // We do getSession instead of startHandshake() so we can call this multiple times
        SSLSession session = ((SSLSocket)sock).getSession();
        if (session.getCipherSuite().equals("SSL_NULL_WITH_NULL_NULL"))
            throw new IOException("SSL handshake failed. Ciper suite in SSL Session is SSL_NULL_WITH_NULL_NULL");

        if (!allowUnsafeLegacyRenegotiation && !RFC_5746_SUPPORTED) {
            // Prevent further handshakes by removing all cipher suites
            ((SSLSocket) sock).setEnabledCipherSuites(new String[0]);
        }
    }

    
}



/**
 * Simple implementation of <b>Realm</b> that reads an XML file to configure
 * the valid users, passwords, and roles.  The file format (and default file
 * location) are identical to those currently supported by Tomcat 3.X.
 *
 * @author Craig R. McClanahan
 * @version $Id$
 */

public abstract class RealmBase extends LifecycleMBeanBase implements Realm {



    /**
     * Return the Principal associated with the specified username, which
     * matches the digest calculated using the given parameters using the
     * method described in RFC 2069; otherwise return <code>null</code>.
     *
     * @param username Username of the Principal to look up
     * @param clientDigest Digest which has been submitted by the client
     * @param nOnce Unique (or supposedly unique) token which has been used
     * for this request
     * @param realm Realm name
     * @param md5a2 Second MD5 digest used to calculate the digest :
     * MD5(Method + ":" + uri)
     */
    @Override
    public Principal authenticate(String username, String clientDigest,
                                  String nOnce, String nc, String cnonce,
                                  String qop, String realm,
                                  String md5a2) {

        String md5a1 = getDigest(username, realm);
        if (md5a1 == null)
            return null;
        String serverDigestValue = md5a1 + ":" + nOnce + ":" + nc + ":"
            + cnonce + ":" + qop + ":" + md5a2;

        byte[] valueBytes = null;
        if(getDigestEncoding() == null) {
            valueBytes = serverDigestValue.getBytes();
        } else {
            try {
                valueBytes = serverDigestValue.getBytes(getDigestEncoding());
            } catch (UnsupportedEncodingException uee) {
                log.error("Illegal digestEncoding: " + getDigestEncoding(), uee);
                throw new IllegalArgumentException(uee.getMessage());
            }
        }

        String serverDigest = null;
        // Bugzilla 32137
        synchronized(md5Helper) {
            serverDigest = md5Encoder.encode(md5Helper.digest(valueBytes));
        }

        if (log.isDebugEnabled()) {
            log.debug("Digest : " + clientDigest + " Username:" + username 
                    + " ClientSigest:" + clientDigest + " nOnce:" + nOnce 
                    + " nc:" + nc + " cnonce:" + cnonce + " qop:" + qop 
                    + " realm:" + realm + "md5a2:" + md5a2 
                    + " Server digest:" + serverDigest);
        }
        
        if (serverDigest.equals(clientDigest)) {
            return getPrincipal(username);
        }

        return null;
    }


    
}

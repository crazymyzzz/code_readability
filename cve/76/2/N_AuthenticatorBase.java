


/**
 * Basic implementation of the <b>Valve</b> interface that enforces the
 * <code>&lt;security-constraint&gt;</code> elements in the web application
 * deployment descriptor.  This functionality is implemented as a Valve
 * so that it can be omitted in environments that do not require these
 * features.  Individual implementations of each supported authentication
 * method can subclass this base class as required.
 * <p>
 * <b>USAGE CONSTRAINT</b>:  When this class is utilized, the Context to
 * which it is attached (or a parent Container in a hierarchy) must have an
 * associated Realm that can be used for authenticating users and enumerating
 * the roles to which they have been assigned.
 * <p>
 * <b>USAGE CONSTRAINT</b>:  This Valve is only useful when processing HTTP
 * requests.  Requests of any other type will simply be passed through.
 *
 * @author Craig R. McClanahan
 * @version $Id$
 */


public abstract class AuthenticatorBase extends ValveBase
        implements Authenticator {




    /**
     * Enforce the security restrictions in the web application deployment
     * descriptor of our associated Context.
     *
     * @param request Request to be processed
     * @param response Response to be processed
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if thrown by a processing element
     */
    @Override
    public void invoke(Request request, Response response)
        throws IOException, ServletException {

        if (log.isDebugEnabled())
            log.debug("Security checking request " +
                request.getMethod() + " " + request.getRequestURI());
        LoginConfig config = this.context.getLoginConfig();

        // Have we got a cached authenticated Principal to record?
        if (cache) {
            Principal principal = request.getUserPrincipal();
            if (principal == null) {
                Session session = request.getSessionInternal(false);
                if (session != null) {
                    principal = session.getPrincipal();
                    if (principal != null) {
                        if (log.isDebugEnabled())
                            log.debug("We have cached auth type " +
                                session.getAuthType() +
                                " for principal " +
                                session.getPrincipal());
                        request.setAuthType(session.getAuthType());
                        request.setUserPrincipal(principal);
                    }
                }
            }
        }

        // Special handling for form-based logins to deal with the case
        // where the login form (and therefore the "j_security_check" URI
        // to which it submits) might be outside the secured area
        String contextPath = this.context.getPath();
        String requestURI = request.getDecodedRequestURI();
        if (requestURI.startsWith(contextPath) &&
            requestURI.endsWith(Constants.FORM_ACTION)) {
            if (!authenticate(request, response, config)) {
                if (log.isDebugEnabled())
                    log.debug(" Failed authenticate() test ??" + requestURI );
                return;
            }
        }

        // The Servlet may specify security constraints through annotations.
        // Ensure that they have been processed before constraints are checked
        Wrapper wrapper = (Wrapper) request.getMappingData().wrapper;
        if (wrapper != null) {
            wrapper.servletSecurityAnnotationScan();
        }

        Realm realm = this.context.getRealm();
        // Is this request URI subject to a security constraint?
        SecurityConstraint [] constraints
            = realm.findSecurityConstraints(request, this.context);
       
        if (constraints == null && !context.getPreemptiveAuthentication()) {
            if (log.isDebugEnabled())
                log.debug(" Not subject to any constraint");
            getNext().invoke(request, response);
            return;
        }

        // Make sure that constrained resources are not cached by web proxies
        // or browsers as caching can provide a security hole
        if (constraints != null && disableProxyCaching && 
            // FIXME: Disabled for Mozilla FORM support over SSL 
            // (improper caching issue)
            //!request.isSecure() &&
            !"POST".equalsIgnoreCase(request.getMethod())) {
            if (securePagesWithPragma) {
                // FIXME: These cause problems with downloading office docs
                // from IE under SSL and may not be needed for newer Mozilla
                // clients.
                response.setHeader("Pragma", "No-cache");
                response.setHeader("Cache-Control", "no-cache");
            } else {
                response.setHeader("Cache-Control", "private");
            }
            response.setHeader("Expires", DATE_ONE);
        }

        int i;
        if (constraints != null) {
            // Enforce any user data constraint for this security constraint
            if (log.isDebugEnabled()) {
                log.debug(" Calling hasUserDataPermission()");
            }
            if (!realm.hasUserDataPermission(request, response,
                                             constraints)) {
                if (log.isDebugEnabled()) {
                    log.debug(" Failed hasUserDataPermission() test");
                }
                /*
                 * ASSERT: Authenticator already set the appropriate
                 * HTTP status code, so we do not have to do anything special
                 */
                return;
            }
        }

        // Since authenticate modifies the response on failure,
        // we have to check for allow-from-all first.
        boolean authRequired;
        if (constraints == null) {
            authRequired = false;
        } else {
            authRequired = true;
            for(i=0; i < constraints.length && authRequired; i++) {
                if(!constraints[i].getAuthConstraint()) {
                    authRequired = false;
                } else if(!constraints[i].getAllRoles()) {
                    String [] roles = constraints[i].findAuthRoles();
                    if(roles == null || roles.length == 0) {
                        authRequired = false;
                    }
                }
            }
        }

        if (!authRequired) {
            authRequired =
                request.getCoyoteRequest().getMimeHeaders().getValue(
                        "authorization") != null;
        }

        if (!authRequired) {
            X509Certificate[] certs = (X509Certificate[]) request.getAttribute(
                    Globals.CERTIFICATES_ATTR);
            authRequired = certs != null && certs.length > 0;
        }

        if(authRequired) {  
            if (log.isDebugEnabled()) {
                log.debug(" Calling authenticate()");
            }
            if (!authenticate(request, response, config)) {
                if (log.isDebugEnabled()) {
                    log.debug(" Failed authenticate() test");
                }
                /*
                 * ASSERT: Authenticator already set the appropriate
                 * HTTP status code, so we do not have to do anything
                 * special
                 */
                return;
            } 
            
        }
    
        if (constraints != null) {
            if (log.isDebugEnabled()) {
                log.debug(" Calling accessControl()");
            }
            if (!realm.hasResourcePermission(request, response,
                                             constraints,
                                             this.context)) {
                if (log.isDebugEnabled()) {
                    log.debug(" Failed accessControl() test");
                }
                /*
                 * ASSERT: AccessControl method has already set the
                 * appropriate HTTP status code, so we do not have to do
                 * anything special
                 */
                return;
            }
        }
    
        // Any and all specified constraints have been satisfied
        if (log.isDebugEnabled()) {
            log.debug(" Successfully passed all security constraints");
        }
        getNext().invoke(request, response);

    }

}



/**
 * Standard implementation of the <b>Wrapper</b> interface that represents
 * an individual servlet definition.  No child Containers are allowed, and
 * the parent Container must be a Context.
 *
 * @author Craig R. McClanahan
 * @author Remy Maucherat
 * @version $Id$
 */
@SuppressWarnings("deprecation") // SingleThreadModel
public class StandardWrapper extends ContainerBase
    implements ServletConfig, Wrapper, NotificationEmitter {



    private void processServletSecurityAnnotation(Servlet servlet) {
        // Calling this twice isn't harmful so no syncs
        servletSecurityAnnotationScanRequired = false;

        ServletSecurity secAnnotation =
            servlet.getClass().getAnnotation(ServletSecurity.class);
        Context ctxt = (Context) getParent();
        if (secAnnotation != null) {
            ctxt.addServletSecurity(
                    new ApplicationServletRegistration(this, ctxt),
                    new ServletSecurityElement(secAnnotation));
        }
    }

    
}

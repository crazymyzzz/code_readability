

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


    /**
     * {@inheritDoc}
     * @throws ClassNotFoundException 
     */
    @Override
    public void servletSecurityAnnotationScan() throws ServletException {
        if (getServlet() == null) {
            Class<?> clazz = null;
            try {
                clazz = getParentClassLoader().loadClass(getServletClass());
                processServletSecurityAnnotation(clazz);
            } catch (ClassNotFoundException e) {
                // Safe to ignore. No class means no annotations to process
            }
        } else {
            if (servletSecurityAnnotationScanRequired) {
                processServletSecurityAnnotation(getServlet().getClass());
            }
        }
    }

    
}

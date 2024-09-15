
/**
 * Startup event listener for a <b>Context</b> that configures the properties
 * of that Context, and the associated defined servlets.
 *
 * @author Craig R. McClanahan
 * @author Jean-Francois Arcand
 * @version $Id$
 */

public class ContextConfig
    implements LifecycleListener {




    /**
     * Create (if necessary) and return a Digester configured to process the
     * web application deployment descriptor (web.xml).
     */
    protected void createWebXmlDigester(boolean namespaceAware,
            boolean validation) {
        
        if (!namespaceAware && !validation) {
            webDigester = webDigesters[0];
            webFragmentDigester = webFragmentDigesters[0];
            
        } else if (!namespaceAware && validation) {
            webDigester = webDigesters[1];
            webFragmentDigester = webFragmentDigesters[1];
            
        } else if (namespaceAware && !validation) {
            webDigester = webDigesters[2];
            webFragmentDigester = webFragmentDigesters[2];
            
        } else {
            webDigester = webDigesters[3];
            webFragmentDigester = webFragmentDigesters[3];
        }
    }

    
    
}

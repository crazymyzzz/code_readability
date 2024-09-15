


/**
 * Startup event listener for a <b>Host</b> that configures the properties
 * of that Host, and the associated defined contexts.
 *
 * @author Craig R. McClanahan
 * @author Remy Maucherat
 * @version $Revision$ $Date$
 */
public class HostConfig
    implements LifecycleListener {


    /**
     * Deploy WAR files.
     */
    protected void deployWARs(File appBase, String[] files) {
        
        if (files == null)
            return;
        
        for (int i = 0; i < files.length; i++) {
            
            if (files[i].equalsIgnoreCase("META-INF"))
                continue;
            if (files[i].equalsIgnoreCase("WEB-INF"))
                continue;
            File dir = new File(appBase, files[i]);
            if (files[i].toLowerCase().endsWith(".war") && dir.isFile()
                    && !invalidWars.contains(files[i]) ) {
                
                // Calculate the context path and make sure it is unique
                String contextPath = "/" + files[i].replace('#','/');
                int period = contextPath.lastIndexOf(".");
                contextPath = contextPath.substring(0, period);
                
                // Check for WARs with /../ /./ or similar sequences in the name
                if (!validateContextPath(appBase, contextPath)) {
                    log.error(sm.getString(
                            "hostConfig.illegalWarName", files[i]));
                    invalidWars.add(files[i]);
                    continue;
                }

                if (contextPath.equals("/ROOT"))
                    contextPath = "";
                
                if (isServiced(contextPath))
                    continue;
                
                String file = files[i];
                
                deployWAR(contextPath, dir, file);
                
            }
            
        }
        
    }


    

}

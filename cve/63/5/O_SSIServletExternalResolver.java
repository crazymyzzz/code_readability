

/**
 * An implementation of SSIExternalResolver that is used with servlets.
 * 
 * @author Dan Sandberg
 * @author David Becker
 * @version $Revision$, $Date$
 */
public class SSIServletExternalResolver implements SSIExternalResolver {


    protected String getAbsolutePath(String path) throws IOException {
        String pathWithoutContext = SSIServletRequestUtil.getRelativePath(req);
        String prefix = getPathWithoutFileName(pathWithoutContext);
        if (prefix == null) {
            throw new IOException("Couldn't remove filename from path: "
                    + pathWithoutContext);
        }
        String fullPath = prefix + path;
        String retVal = SSIServletRequestUtil.normalize(fullPath);
        if (retVal == null) {
            throw new IOException("Normalization yielded null on path: "
                    + fullPath);
        }
        return retVal;
    }




    protected ServletContextAndPath getServletContextAndPathFromVirtualPath(
            String virtualPath) throws IOException {

        if (!virtualPath.startsWith("/") && !virtualPath.startsWith("\\")) {
            return new ServletContextAndPath(context,
                    getAbsolutePath(virtualPath));
        } else {
            String normalized = SSIServletRequestUtil.normalize(virtualPath);
            if (isVirtualWebappRelative) {
                return new ServletContextAndPath(context, normalized);
            } else {
                ServletContext normContext = context.getContext(normalized);
                if (normContext == null) {
                    throw new IOException("Couldn't get context for path: "
                            + normalized);
                }
                //If it's the root context, then there is no context element
                // to remove,
                // ie:
                // '/file1.shtml' vs '/appName1/file1.shtml'
                if (!isRootContext(normContext)) {
                    String noContext = getPathWithoutContext(
                            normContext.getContextPath(), normalized);
                    if (noContext == null) {
                        throw new IOException(
                                "Couldn't remove context from path: "
                                        + normalized);
                    }
                    return new ServletContextAndPath(normContext, noContext);
                } else {
                    return new ServletContextAndPath(normContext, normalized);
                }
            }
        }
    }


   
}


/**
 * Filesystem Directory Context implementation helper class.
 *
 * @author Remy Maucherat
 * @version $Revision$ $Date$
 */

public class FileDirContext extends BaseDirContext {



    // ------------------------------------------------------ Protected Methods


    /**
     * Return a context-relative path, beginning with a "/", that represents
     * the canonical version of the specified path after ".." and "." elements
     * are resolved out.  If the specified path attempts to go outside the
     * boundaries of the current context (i.e. too many ".." path elements
     * are present), return <code>null</code> instead.
     *
     * @param path Path to be normalized
     */
    protected String normalize(String path) {

        return RequestUtil.normalize(path, File.separatorChar == '\\');

    }

    


}


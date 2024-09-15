

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

    String normalized = path;

    // Normalize the slashes and add leading slash if necessary
    if (File.separatorChar == '\\' && normalized.indexOf('\\') >= 0)
        normalized = normalized.replace('\\', '/');
    if (!normalized.startsWith("/"))
        normalized = "/" + normalized;

    // Resolve occurrences of "//" in the normalized path
    while (true) {
        int index = normalized.indexOf("//");
        if (index < 0)
        break;
        normalized = normalized.substring(0, index) +
        normalized.substring(index + 1);
    }

    // Resolve occurrences of "/./" in the normalized path
    while (true) {
        int index = normalized.indexOf("/./");
        if (index < 0)
        break;
        normalized = normalized.substring(0, index) +
        normalized.substring(index + 2);
    }

    // Resolve occurrences of "/../" in the normalized path
    while (true) {
        int index = normalized.indexOf("/../");
        if (index < 0)
        break;
        if (index == 0)
        return (null);  // Trying to go outside our context
        int index2 = normalized.lastIndexOf('/', index - 1);
        normalized = normalized.substring(0, index2) +
        normalized.substring(index + 3);
    }

    // Return the normalized path that we have completed
    return (normalized);

    }


    


}


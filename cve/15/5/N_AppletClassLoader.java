
public class AppletClassLoader extends URLClassLoader {


    /*
     * Finds the applet class with the specified name. First searches
     * loaded JAR files then the applet code base for the class.
     */
    protected Class findClass(String name) throws ClassNotFoundException {

        int index = name.indexOf(";");
        String cookie = "";
        if(index != -1) {
                cookie = name.substring(index, name.length());
                name = name.substring(0, index);
        }

        // check loaded JAR files
        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
        }

        // Otherwise, try loading the class from the code base URL

        // 4668479: Option to turn off codebase lookup in AppletClassLoader
        // during resource requests. [stanley.ho]
        if (codebaseLookup == false)
            throw new ClassNotFoundException(name);

//      final String path = name.replace('.', '/').concat(".class").concat(cookie);
        String encodedName = ParseUtil.encodePath(name.replace('.', '/'), false);
        final String path = (new StringBuffer(encodedName)).append(".class").append(cookie).toString();
        try {
            byte[] b = (byte[]) AccessController.doPrivileged(
                               new PrivilegedExceptionAction() {
                public Object run() throws IOException {
                   try {
                        URL finalURL = new URL(base, path);

                        // Make sure the codebase won't be modified
                        if (base.getProtocol().equals(finalURL.getProtocol()) &&
                            base.getHost().equals(finalURL.getHost()) &&
                            base.getPort() == finalURL.getPort()) {
                            return getBytes(finalURL);
                        }
                        else {
                            return null;
                        }
                    } catch (Exception e) {
                        return null;
                    }
                }
            }, acc);

            if (b != null) {
                return defineClass(name, b, 0, b.length, codesource);
            } else {
                throw new ClassNotFoundException(name);
            }
        } catch (PrivilegedActionException e) {
            throw new ClassNotFoundException(name, e.getException());
        }
    }

    /**
     * Returns the permissions for the given codesource object.
     * The implementation of this method first calls super.getPermissions,
     * to get the permissions
     * granted by the super class, and then adds additional permissions
     * based on the URL of the codesource.
     * <p>
     * If the protocol is "file"
     * and the path specifies a file, permission is granted to read all files
     * and (recursively) all files and subdirectories contained in
     * that directory. This is so applets with a codebase of
     * file:/blah/some.jar can read in file:/blah/, which is needed to
     * be backward compatible. We also add permission to connect back to
     * the "localhost".
     *
     * @param codesource the codesource
     * @return the permissions granted to the codesource
     */
    protected PermissionCollection getPermissions(CodeSource codesource)
    {
        final PermissionCollection perms = super.getPermissions(codesource);

        URL url = codesource.getLocation();

        String path = null;
        Permission p;

        try {
            p = url.openConnection().getPermission();
        } catch (java.io.IOException ioe) {
            p = null;
        }

        if (p instanceof FilePermission) {
            path = p.getName();
        } else if ((p == null) && (url.getProtocol().equals("file"))) {
            path = url.getFile().replace('/', File.separatorChar);
            path = ParseUtil.decode(path);
        }

        if (path != null) {
            final String rawPath = path;
            if (!path.endsWith(File.separator)) {
                int endIndex = path.lastIndexOf(File.separatorChar);
                if (endIndex != -1) {
                        path = path.substring(0, endIndex + 1) + "-";
                        perms.add(new FilePermission(path,
                            SecurityConstants.FILE_READ_ACTION));
                }
            }
            final File f = new File(rawPath);
            final boolean isDirectory = f.isDirectory();
            // grant codebase recursive read permission
            // this should only be granted to non-UNC file URL codebase and
            // the codesource path must either be a directory, or a file
            // that ends with .jar or .zip
            if (allowRecursiveDirectoryRead && (isDirectory ||
                    rawPath.toLowerCase().endsWith(".jar") ||
                    rawPath.toLowerCase().endsWith(".zip"))) {

            Permission bperm;
                try {
                    bperm = base.openConnection().getPermission();
                } catch (java.io.IOException ioe) {
                    bperm = null;
                }
                if (bperm instanceof FilePermission) {
                    String bpath = bperm.getName();
                    if (bpath.endsWith(File.separator)) {
                        bpath += "-";
                    }
                    perms.add(new FilePermission(bpath,
                        SecurityConstants.FILE_READ_ACTION));
                } else if ((bperm == null) && (base.getProtocol().equals("file"))) {
                    String bpath = base.getFile().replace('/', File.separatorChar);
                    bpath = ParseUtil.decode(bpath);
                    if (bpath.endsWith(File.separator)) {
                        bpath += "-";
                    }
                    perms.add(new FilePermission(bpath, SecurityConstants.FILE_READ_ACTION));
                }

            }
        }
        return perms;
    }



    

    /**
     * Release this AppletClassLoader and its ThreadGroup/AppContext.
     * If nothing else has grabbed this AppletClassLoader, its ThreadGroup
     * and AppContext will be destroyed.
     *
     * Because this method may destroy the AppletClassLoader's ThreadGroup,
     * this method should NOT be called from within the AppletClassLoader's
     * ThreadGroup.
     *
     * Changed modifier to protected in order to be able to overwrite this
     * function in PluginClassLoader.java
     */
    protected void release() {

        AppContext tempAppContext = null;

        synchronized(grabReleaseSynchronizer) {
            if (usageCount > 1)  {
                --usageCount;
            } else {
                synchronized(threadGroupSynchronizer) {
                    tempAppContext = resetAppContext();
                }
            }
        }

        // Dispose appContext outside any sync block to
        // prevent potential deadlock.
        if (tempAppContext != null)  {
            try {
                tempAppContext.dispose(); // nuke the world!
            } catch (IllegalThreadStateException e) { }
        }
    }

    
} // class AppContextCreator

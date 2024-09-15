
public abstract class ClassLoader {



    /**
     * Find resources from the VM's built-in classloader.
     */
    private static URL getBootstrapResource(String name) {
        try {
            // If this is a known JRE resource, ensure that its bundle is
            // downloaded.  If it isn't known, we just ignore the download
            // failure and check to see if we can find the resource anyway
            // (which is possible if the boot class path has been modified).
            if (sun.misc.VM.isBootedKernelVM()) {
                sun.jkernel.DownloadManager.getBootClassPathEntryForResource(
                    name);
            }
        } catch (NoClassDefFoundError e) {
            // This happens while Java itself is being compiled; DownloadManager
            // isn't accessible when this code is first invoked.  It isn't an
            // issue, as if we can't find DownloadManager, we can safely assume
            // that additional code is not available for download.
        }
        URLClassPath ucp = getBootstrapClassPath();
        Resource res = ucp.getResource(name);
        return res != null ? res.getURL() : null;
    }



    // Returns the URLClassPath that is used for finding system resources.
    static URLClassPath getBootstrapClassPath() {
        return sun.misc.Launcher.getBootstrapClassPath();
    }




    // Invoked in the java.lang.Runtime class to implement load and loadLibrary.
    static void loadLibrary(Class fromClass, String name,
                            boolean isAbsolute) {
        try {
            if (VM.isBootedKernelVM() && !DownloadManager.isJREComplete() &&
                    !DownloadManager.isCurrentThreadDownloading()) {
                DownloadManager.downloadFile("bin/" +
                    System.mapLibraryName(name));
                // it doesn't matter if the downloadFile call returns false --
                // it probably just means that this is a user library, as
                // opposed to a JRE library
            }
        } catch (IOException e) {
            throw new UnsatisfiedLinkError("Error downloading library " +
                                                name + ": " + e);
        } catch (NoClassDefFoundError e) {
            // This happens while Java itself is being compiled; DownloadManager
            // isn't accessible when this code is first invoked.  It isn't an
            // issue, as if we can't find DownloadManager, we can safely assume
            // that additional code is not available for download.
        }
        ClassLoader loader =
            (fromClass == null) ? null : fromClass.getClassLoader();
        if (sys_paths == null) {
            usr_paths = initializePath("java.library.path");
            sys_paths = initializePath("sun.boot.library.path");
        }
        if (isAbsolute) {
            if (loadLibrary0(fromClass, new File(name))) {
                return;
            }
            throw new UnsatisfiedLinkError("Can't load library: " + name);
        }
        if (loader != null) {
            String libfilename = loader.findLibrary(name);
            if (libfilename != null) {
                File libfile = new File(libfilename);
                if (!libfile.isAbsolute()) {
                    throw new UnsatisfiedLinkError(
    "ClassLoader.findLibrary failed to return an absolute path: " + libfilename);
                }
                if (loadLibrary0(fromClass, libfile)) {
                    return;
                }
                throw new UnsatisfiedLinkError("Can't load " + libfilename);
            }
        }
        for (int i = 0 ; i < sys_paths.length ; i++) {
            File libfile = new File(sys_paths[i], System.mapLibraryName(name));
            if (loadLibrary0(fromClass, libfile)) {
                return;
            }
        }
        if (loader != null) {
            for (int i = 0 ; i < usr_paths.length ; i++) {
                File libfile = new File(usr_paths[i],
                                        System.mapLibraryName(name));
                if (loadLibrary0(fromClass, libfile)) {
                    return;
                }
            }
        }
        // Oops, it failed
        throw new UnsatisfiedLinkError("no " + name + " in java.library.path");
    }

    
}

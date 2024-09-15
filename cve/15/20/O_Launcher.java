
/**
 * This class is used by the system to launch the main application.
Launcher */
public class Launcher {
    

    /**
     * The class loader used for loading from java.class.path.
     * runs in a restricted security context.
     */
    static class AppClassLoader extends URLClassLoader {

        /**
         * Override loadClass so we can checkPackageAccess.
         */
        public Class loadClass(String name, boolean resolve)
            throws ClassNotFoundException
        {
            int i = name.lastIndexOf('.');
            if (i != -1) {
                SecurityManager sm = System.getSecurityManager();
                if (sm != null) {
                    sm.checkPackageAccess(name.substring(0, i));
                }
            }
            return (super.loadClass(name, resolve));
        }

        
    }

    public static URLClassPath getBootstrapClassPath() {
        String prop = AccessController.doPrivileged(
            new GetPropertyAction("sun.boot.class.path"));
        URL[] urls;
        if (prop != null) {
            final String path = prop;
            urls = AccessController.doPrivileged(
                new PrivilegedAction<URL[]>() {
                    public URL[] run() {
                        File[] classPath = getClassPath(path);
                        int len = classPath.length;
                        Set<File> seenDirs = new HashSet<File>();
                        for (int i = 0; i < len; i++) {
                            File curEntry = classPath[i];
                            // Negative test used to properly handle
                            // nonexistent jars on boot class path
                            if (!curEntry.isDirectory()) {
                                curEntry = curEntry.getParentFile();
                            }
                            if (curEntry != null && seenDirs.add(curEntry)) {
                                MetaIndex.registerDirectory(curEntry);
                            }
                        }
                        return pathToURLs(classPath);
                    }
                }
            );
        } else {
            urls = new URL[0];
        }
        return new URLClassPath(urls, factory);
    }

    
}

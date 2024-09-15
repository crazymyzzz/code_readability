


public class ICC_Profile implements Serializable {



    /*
     * this version is called from doPrivileged in privilegedOpenProfile.
     * the whole method is privileged!
     */
    private static FileInputStream privilegedOpenProfile(String fileName) {
        FileInputStream fis = null;
        String path, dir, fullPath;

        File f = new File(fileName); /* try absolute file name */

        if ((!f.isFile()) &&
                ((path = System.getProperty("java.iccprofile.path")) != null)){
                                    /* try relative to java.iccprofile.path */
                StringTokenizer st =
                    new StringTokenizer(path, File.pathSeparator);
                while (st.hasMoreTokens() && (!f.isFile())) {
                    dir = st.nextToken();
                        fullPath = dir + File.separatorChar + fileName;
                    f = new File(fullPath);
                }
            }

        if ((!f.isFile()) &&
                ((path = System.getProperty("java.class.path")) != null)) {
                                    /* try relative to java.class.path */
                StringTokenizer st =
                    new StringTokenizer(path, File.pathSeparator);
                while (st.hasMoreTokens() && (!f.isFile())) {
                    dir = st.nextToken();
                        fullPath = dir + File.separatorChar + fileName;
                    f = new File(fullPath);
                }
            }

        if (!f.isFile()) { /* try the directory of built-in profiles */
                dir = System.getProperty("java.home") +
                    File.separatorChar + "lib" + File.separatorChar + "cmm";
                fullPath = dir + File.separatorChar + fileName;
                f = new File(fullPath);
            }

        if (f.isFile()) {
            try {
                fis = new FileInputStream(f);
            } catch (FileNotFoundException e) {
            }
        }
        return fis;
    }


    
}

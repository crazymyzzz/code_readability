@@ -1823,10 +1823,18 @@ private static FileInputStream privilegedOpenProfile(String fileName) {
             }
 
         if (!f.isFile()) { /* try the directory of built-in profiles */
-                dir = System.getProperty("java.home") +
-                    File.separatorChar + "lib" + File.separatorChar + "cmm";
-                fullPath = dir + File.separatorChar + fileName;
+            dir = System.getProperty("java.home") +
+                File.separatorChar + "lib" + File.separatorChar + "cmm";
+            fullPath = dir + File.separatorChar + fileName;
                 f = new File(fullPath);
+                if (!f.isFile()) {
+                    //make sure file was installed in the kernel mode
+                    try {
+                        //kernel uses platform independent paths =>
+                        //   should not use platform separator char
+                        sun.jkernel.DownloadManager.downloadFile("lib/cmm/"+fileName);
+                    } catch (IOException ioe) {}
+                }
             }
 
         if (f.isFile()) {

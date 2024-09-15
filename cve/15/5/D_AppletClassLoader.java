
@@ -188,7 +194,21 @@ protected Class findClass(String name) throws ClassNotFoundException {
             byte[] b = (byte[]) AccessController.doPrivileged(
                                new PrivilegedExceptionAction() {
                 public Object run() throws IOException {
-                    return getBytes(new URL(base, path));
+                   try {
+                        URL finalURL = new URL(base, path);
+
+                        // Make sure the codebase won't be modified
+                        if (base.getProtocol().equals(finalURL.getProtocol()) &&
+                            base.getHost().equals(finalURL.getHost()) &&
+                            base.getPort() == finalURL.getPort()) {
+                            return getBytes(finalURL);
+                        }
+                        else {
+                            return null;
+                        }
+                    } catch (Exception e) {
+                        return null;
+                    }
                 }
             }, acc);
 
@@ -243,51 +263,48 @@ protected PermissionCollection getPermissions(CodeSource codesource)
         }
 
         if (path != null) {
+            final String rawPath = path;
             if (!path.endsWith(File.separator)) {
                 int endIndex = path.lastIndexOf(File.separatorChar);
                 if (endIndex != -1) {
-                        path = path.substring(0, endIndex+1) + "-";
+                        path = path.substring(0, endIndex + 1) + "-";
                         perms.add(new FilePermission(path,
                             SecurityConstants.FILE_READ_ACTION));
                 }
             }
-            perms.add(new SocketPermission("localhost",
-                SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
-            AccessController.doPrivileged(new PrivilegedAction() {
-                public Object run() {
-                    try {
-                        String host = InetAddress.getLocalHost().getHostName();
-                        perms.add(new SocketPermission(host,
-                            SecurityConstants.SOCKET_CONNECT_ACCEPT_ACTION));
-                    } catch (UnknownHostException uhe) {
-
-                    }
-                    return null;
-                }
-            });
+            final File f = new File(rawPath);
+            final boolean isDirectory = f.isDirectory();
+            // grant codebase recursive read permission
+            // this should only be granted to non-UNC file URL codebase and
+            // the codesource path must either be a directory, or a file
+            // that ends with .jar or .zip
+            if (allowRecursiveDirectoryRead && (isDirectory ||
+                    rawPath.toLowerCase().endsWith(".jar") ||
+                    rawPath.toLowerCase().endsWith(".zip"))) {
 
             Permission bperm;
-            try {
-                bperm = base.openConnection().getPermission();
-            } catch (java.io.IOException ioe) {
-                bperm = null;
-            }
-            if (bperm instanceof FilePermission) {
-                String bpath = bperm.getName();
-                if (bpath.endsWith(File.separator)) {
-                    bpath += "-";
+                try {
+                    bperm = base.openConnection().getPermission();
+                } catch (java.io.IOException ioe) {
+                    bperm = null;
                 }
-                perms.add(new FilePermission(bpath,
-                    SecurityConstants.FILE_READ_ACTION));
-            } else if ((bperm == null) && (base.getProtocol().equals("file"))) {
-                String bpath = base.getFile().replace('/', File.separatorChar);
-                bpath = ParseUtil.decode(bpath);
-                if (bpath.endsWith(File.separator)) {
-                    bpath += "-";
+                if (bperm instanceof FilePermission) {
+                    String bpath = bperm.getName();
+                    if (bpath.endsWith(File.separator)) {
+                        bpath += "-";
+                    }
+                    perms.add(new FilePermission(bpath,
+                        SecurityConstants.FILE_READ_ACTION));
+                } else if ((bperm == null) && (base.getProtocol().equals("file"))) {
+                    String bpath = base.getFile().replace('/', File.separatorChar);
+                    bpath = ParseUtil.decode(bpath);
+                    if (bpath.endsWith(File.separator)) {
+                        bpath += "-";
+                    }
+                    perms.add(new FilePermission(bpath, SecurityConstants.FILE_READ_ACTION));
                 }
-                perms.add(new FilePermission(bpath, SecurityConstants.FILE_READ_ACTION));
-            }
 
+            }
         }
         return perms;
     }

@@ -740,11 +757,7 @@ protected void release() {
                 --usageCount;
             } else {
                 synchronized(threadGroupSynchronizer) {
-                    // Store app context in temp variable
-                    tempAppContext = appContext;
-                    usageCount = 0;
-                    appContext = null;
-                    threadGroup = null;
+                    tempAppContext = resetAppContext();
                 }
             }
         }


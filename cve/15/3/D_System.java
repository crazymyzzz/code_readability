@@ -1106,12 +1106,22 @@ private static void initializeSystemClass() {
         initProperties(props);
         sun.misc.Version.init();
 
+        // Workaround until DownloadManager initialization is revisited.
+        // Make JavaLangAccess available early enough for internal
+        // Shutdown hooks to be registered
+        setJavaLangAccess();
+
         // Gets and removes system properties that configure the Integer
         // cache used to support the object identity semantics of autoboxing.
         // At this time, the size of the cache may be controlled by the
-        // -XX:AutoBoxCacheMax=<size> option.
+        // vm option -XX:AutoBoxCacheMax=<size>.
         Integer.getAndRemoveCacheProperties();
 
+        // Load the zip library now in order to keep java.util.zip.ZipFile
+        // from trying to use itself to load this library later.
+        loadLibrary("zip");
+
+
         FileInputStream fdIn = new FileInputStream(FileDescriptor.in);
         FileOutputStream fdOut = new FileOutputStream(FileDescriptor.out);
         FileOutputStream fdErr = new FileOutputStream(FileDescriptor.err);
@@ -1119,10 +1129,6 @@ private static void initializeSystemClass() {
         setOut0(new PrintStream(new BufferedOutputStream(fdOut, 128), true));
         setErr0(new PrintStream(new BufferedOutputStream(fdErr, 128), true));
 
-        // Load the zip library now in order to keep java.util.zip.ZipFile
-        // from trying to use itself to load this library later.
-        loadLibrary("zip");
-
         // Setup Java signal handlers for HUP, TERM, and INT (where available).
         Terminator.setup();
 
@@ -1152,7 +1158,9 @@ private static void initializeSystemClass() {
         // way as other threads; we must do it ourselves here.
         Thread current = Thread.currentThread();
         current.getThreadGroup().add(current);
+    }
 
+    private static void setJavaLangAccess() {
         // Allow privileged classes outside of java.lang
         sun.misc.SharedSecrets.setJavaLangAccess(new sun.misc.JavaLangAccess(){
             public sun.reflect.ConstantPool getConstantPool(Class klass) {

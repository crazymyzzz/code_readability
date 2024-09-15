@@ -41,24 +41,27 @@ public class ClassPathResource extends LoadableResource {
     private final boolean detectEncoding;
     private final String parentURL;
 
+    private final boolean stream;
+
     public ClassPathResource(Location location, String fileNameWithAbsolutePath, ClassLoader classLoader,
                              Charset encoding) {
-        this(location, fileNameWithAbsolutePath, classLoader, encoding, false, "");
+        this(location, fileNameWithAbsolutePath, classLoader, encoding, false, "", false);
     }
 
     public ClassPathResource(Location location, String fileNameWithAbsolutePath, ClassLoader classLoader,
-                             Charset encoding, String parentURL) {
-        this(location, fileNameWithAbsolutePath, classLoader, encoding, false, parentURL);
+                             Charset encoding, String parentURL, boolean stream) {
+        this(location, fileNameWithAbsolutePath, classLoader, encoding, false, parentURL, stream);
     }
 
     public ClassPathResource(Location location, String fileNameWithAbsolutePath, ClassLoader classLoader,
-                             Charset encoding, Boolean detectEncoding, String parentURL) {
+                             Charset encoding, Boolean detectEncoding, String parentURL, boolean stream) {
         this.fileNameWithAbsolutePath = fileNameWithAbsolutePath;
         this.fileNameWithRelativePath = location == null ? fileNameWithAbsolutePath : location.getPathRelativeToThis(fileNameWithAbsolutePath);
         this.classLoader = classLoader;
         this.encoding = encoding;
         this.detectEncoding = detectEncoding;
         this.parentURL = parentURL;
+        this.stream = stream;
     }
 
     @Override
@@ -156,4 +159,10 @@ public class ClassPathResource extends LoadableResource {
     public int hashCode() {
         return Objects.hash(fileNameWithAbsolutePath, parentURL);
     }
+
+    @Override
+    public boolean shouldStream() {
+        return stream;
+    }
+
 }
\ No newline at end of file

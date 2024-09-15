@@ -17,24 +17,43 @@ package org.flywaydb.core.internal.util;
 
 import lombok.AccessLevel;
 import lombok.NoArgsConstructor;
+import lombok.SneakyThrows;
 import org.flywaydb.core.api.FlywayException;
+import org.flywaydb.core.internal.reports.html.HtmlReportGenerator;
 
 import java.io.*;
+import java.net.URI;
+import java.net.URL;
 import java.nio.charset.Charset;
 import java.nio.file.Files;
 import java.nio.file.Path;
+import java.nio.file.Paths;
+import java.util.ArrayList;
+import java.util.Enumeration;
+import java.util.List;
+import java.util.stream.Collectors;
 
 /**
  * Utility class for copying files and their contents. Inspired by Spring's own.
  */
 @NoArgsConstructor(access = AccessLevel.PRIVATE)
 public class FileUtils {
+    public static String getFilename(String path) {
+        if (StringUtils.hasText(path)) {
+            return path.substring(path.replace("/", "\\").lastIndexOf("\\") + 1);
+        } else {
+            return "";
+        }
+    }
+
     /**
      * Copy the contents of the given Reader into a String.
      * Closes the reader when done.
      *
      * @param in the reader to copy from
+     *
      * @return the String that has been copied to
+     *
      * @throws IOException in case of I/O errors
      */
     public static String copyToString(Reader in) throws IOException {
@@ -54,9 +73,11 @@ public class FileUtils {
      * Copy the contents of the given InputStream into a new String based on this encoding.
      * Closes the stream when done.
      *
-     * @param in the stream to copy from
+     * @param in       the stream to copy from
      * @param encoding The encoding to use.
+     *
      * @return The new String.
+     *
      * @throws IOException in case of I/O errors
      */
     public static String copyToString(InputStream in, Charset encoding) throws IOException {
@@ -69,8 +90,9 @@ public class FileUtils {
      * Copy the contents of the given Reader to the given Writer.
      * Closes both when done.
      *
-     * @param in the Reader to copy from
+     * @param in  the Reader to copy from
      * @param out the Writer to copy to
+     *
      * @throws IOException in case of I/O errors
      */
     public static void copy(Reader in, Writer out) throws IOException {
@@ -91,9 +113,11 @@ public class FileUtils {
      * Copy the contents of the given InputStream to the given OutputStream.
      * Closes both streams when done.
      *
-     * @param in the stream to copy from
+     * @param in  the stream to copy from
      * @param out the stream to copy to
+     *
      * @return the number of bytes copied
+     *
      * @throws IOException in case of I/O errors
      */
     public static int copy(InputStream in, OutputStream out) throws IOException {
@@ -120,4 +144,18 @@ public class FileUtils {
             throw new FlywayException("Unable to read " + path.toAbsolutePath() + " from disk", ioe);
         }
     }
-}
\ No newline at end of file
+
+    public static String readResourceAsString(String path) {
+        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(path);
+             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
+
+            String result = "";
+            while (reader.ready()) {
+                result += reader.readLine() + System.lineSeparator();
+            }
+            return result;
+        } catch (IOException ioe) {
+            throw new FlywayException("Unable to read " + path + " from resources", ioe);
+        }
+    }
+}

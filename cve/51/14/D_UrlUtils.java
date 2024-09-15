@@ -17,7 +17,6 @@ package org.flywaydb.core.internal.util;
 
 import lombok.AccessLevel;
 import lombok.NoArgsConstructor;
-import org.flywaydb.core.api.FlywayException;
 
 import java.io.File;
 import java.io.UnsupportedEncodingException;
@@ -36,7 +35,7 @@ public class UrlUtils {
      * @return The file path.
      */
     public static String toFilePath(URL url) {
-        String filePath = new File(decodeURL(url.getPath().replace("+", "%2b"))).getAbsolutePath();
+        String filePath = new File(decodeURLSafe(url.getPath())).getAbsolutePath();
         if (filePath.endsWith("/")) {
             return filePath.substring(0, filePath.length() - 1);
         }
@@ -46,6 +45,8 @@ public class UrlUtils {
     /**
      * Decodes this UTF-8 encoded URL.
      *
+     * Shall be made private, new code shall always call decodeURLSafe() instead.
+     *
      * @param url The url to decode.
      * @return The decoded URL.
      */
@@ -56,4 +57,8 @@ public class UrlUtils {
             throw new IllegalStateException("Can never happen", e);
         }
     }
+
+    public static String decodeURLSafe(String url) {
+       return decodeURL(url.replace("+", "%2b"));
+    }
 }
\ No newline at end of file

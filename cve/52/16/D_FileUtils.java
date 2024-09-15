@@ -15,8 +15,10 @@
  */
 package org.flywaydb.core.internal.util;
 
+import java.util.UUID;
 import lombok.AccessLevel;
 import lombok.NoArgsConstructor;
+import lombok.experimental.ExtensionMethod;
 import org.flywaydb.core.api.FlywayException;
 
 import java.io.*;
@@ -31,6 +33,7 @@ import java.util.Locale;
  * Utility class for copying files and their contents. Inspired by Spring's own.
  */
 @NoArgsConstructor(access = AccessLevel.PRIVATE)
+@ExtensionMethod(StringUtils.class)
 public class FileUtils {
     public static String getFilename(String path) {
         if (StringUtils.hasText(path)) {
@@ -199,4 +202,65 @@ public class FileUtils {
             throw new FlywayException("Unable to write to " + file.getAbsolutePath(), e);
         }
     }
+
+    public static String readUserIdFromFileIfNoneWriteDefault() {
+        String userId = null;
+
+        File redgateAppData;
+        if (isWindows()) {
+            redgateAppData = new File(System.getenv("APPDATA"), "Redgate");
+        } else {
+            redgateAppData = new File(System.getProperty("user.home"), ".config/Redgate");
+        }
+
+        File userIdFile = new File(redgateAppData, "feature_usage_data");
+        if (userIdFile.exists()) {
+            userId = FileUtils.readAsString(userIdFile.toPath());
+        }
+
+        if(!userId.hasText()) {
+            userId = UUID.randomUUID().toString();
+
+            if(!redgateAppData.exists()) {
+                redgateAppData.mkdirs();
+            }
+
+            try(FileWriter fileWriter = new FileWriter(userIdFile)) {
+                fileWriter.write(userId);
+            } catch (IOException ignore) {}
+        }
+
+        return userId;
+    }
+
+    public static void writeUserIdToFile(String userId) {
+        if(!userId.hasText()) {
+            return;
+        }
+
+        File redgateAppData;
+        if (isWindows()) {
+            redgateAppData = new File(System.getenv("APPDATA"), "Redgate");
+        } else {
+            redgateAppData = new File(System.getProperty("user.home"), ".config/Redgate");
+        }
+
+        File userIdFile = new File(redgateAppData, "feature_usage_data");
+
+        if (userIdFile.exists()) {
+            userIdFile.delete();
+        }
+
+        if (!redgateAppData.exists()) {
+            redgateAppData.mkdirs();
+        }
+
+        try (FileWriter fileWriter = new FileWriter(userIdFile)) {
+            fileWriter.write(userId);
+        } catch (IOException ignore) {}
+    }
+
+    private static boolean isWindows() {
+        return System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win");
+    }
 }

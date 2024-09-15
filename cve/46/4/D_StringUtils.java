@@ -411,15 +411,25 @@ public class StringUtils {
         return false;
     }
 
-    public static String getFileExtension(String path) {
-        String[] foldersSplit = path.split("[|/]");
+    public static Pair<String,String> getFileNameAndExtension(String path) {
+        String[] foldersSplit = path.split("[|/\\\\]");
         String fileNameAndExtension = foldersSplit[foldersSplit.length - 1];
 
         String[] nameExtensionSplit = fileNameAndExtension.split("\\.");
         if (nameExtensionSplit.length < 2) {
-            return "";
+            return Pair.of(fileNameAndExtension, "");
         }
 
-        return nameExtensionSplit[nameExtensionSplit.length - 1];
+        return Pair.of(nameExtensionSplit[nameExtensionSplit.length - 2], nameExtensionSplit[nameExtensionSplit.length - 1]);
+    }
+



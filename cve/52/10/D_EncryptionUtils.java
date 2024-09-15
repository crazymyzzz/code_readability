@@ -94,13 +94,13 @@ public class EncryptionUtils {
         return (SealedObject) new ObjectInputStream(new ByteArrayInputStream(byteArray)).readObject();
     }
 
-    public static String hashProjectId(String projectId, String hashInput) {
-        if (projectId == null) {
+    public static String hashString(String strInput, String hashInput) {
+        if (strInput == null) {
             return null;
         }
         try {
             MessageDigest md = MessageDigest.getInstance("SHA-256");
-            md.update(projectId.getBytes(StandardCharsets.UTF_8));
+            md.update(strInput.getBytes(StandardCharsets.UTF_8));
             byte[] hash = md.digest(hashInput.getBytes(StandardCharsets.UTF_8));
             BigInteger number = new BigInteger(1, hash);
             String result = number.toString(16);

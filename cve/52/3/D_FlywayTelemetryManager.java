@@ -50,26 +50,7 @@ public class FlywayTelemetryManager implements AutoCloseable{
 
         String userId = System.getenv("RG_TELEMETRY_ANONYMOUS_USER_ID");
         if(!userId.hasText()) {
-            boolean isWindows = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win");
-            File redgateAppData;
-            if (isWindows) {
-                redgateAppData = new File(System.getenv("APPDATA"), "Redgate");
-            } else {
-                redgateAppData = new File(System.getProperty("user.home"), ".config/Redgate");
-            }
-            File userIdFile = new File(redgateAppData, "feature_usage_data");
-            if (userIdFile.exists()) {
-                userId = FileUtils.readAsString(userIdFile.toPath());
-            }
-            if(!userId.hasText()) {
-                userId = UUID.randomUUID().toString();
-                if(!redgateAppData.exists()) {
-                    redgateAppData.mkdirs();
-                }
-                try(FileWriter fileWriter = new FileWriter(userIdFile)) {
-                    fileWriter.write(userId);
-                } catch (IOException ignore) {}
-            }
+            userId = FileUtils.readUserIdFromFileIfNoneWriteDefault();
         }
 
         rootTelemetryModel.setUserId(userId);

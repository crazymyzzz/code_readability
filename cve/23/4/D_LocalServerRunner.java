@@ -163,7 +163,7 @@ public class LocalServerRunner {
                         + serverFile;
         LOGGER.info("Running command {}", command);
 
-        serverProcess = Runtime.getRuntime().exec(command);
+        serverProcess = Runtime.getRuntime().exec(new String[] {"bash", "-l", "-c", command});
         BufferedReader error =
                 new BufferedReader(new InputStreamReader(serverProcess.getErrorStream()));
         BufferedReader op =

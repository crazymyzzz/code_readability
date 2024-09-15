@@ -497,10 +497,10 @@ public class ExtensionsResource {
                     "                  description: the generated UUID\n" +
                     "                  format: uuid\n" +
                     "              responses:\n" +
-                    "                200:\n" +
+                    "                \"200\":\n" +
                     "                  description: Return this code if the callback was received and processed\n" +
                     "                    successfully\n" +
-                    "                205:\n" +
+                    "                \"205\":\n" +
                     "                  description: Return this code to unsubscribe from future data updates\n" +
                     "                default:\n" +
                     "                  description: All other response codes will disable this callback\n" +

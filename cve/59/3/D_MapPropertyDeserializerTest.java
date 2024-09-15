@@ -205,7 +205,7 @@ public class MapPropertyDeserializerTest {
     public void testIssue1261InlineSchemaExample() throws Exception {
         Operation operation = Yaml.mapper().readValue(
                 "      responses:\n" +
-                        "        200:\n" +
+                        "        \"200\":\n" +
                         "          content:\n" +
                         "            '*/*':\n" +
                         "              description: OK\n" +

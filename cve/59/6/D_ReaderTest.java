@@ -350,15 +350,15 @@ public class ReaderTest {
                 "        object\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/SampleResponseSchema'\n" +
-                "        404:\n" +
+                "        \"404\":\n" +
                 "          description: not found!\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: boo\n" +
                 "          content:\n" +
                 "            '*/*':\n" +
@@ -389,7 +389,7 @@ public class ReaderTest {
                 "        object\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -416,7 +416,7 @@ public class ReaderTest {
                 "          format: int32\n" +
                 "        example: 1\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: bean answer\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -438,7 +438,7 @@ public class ReaderTest {
                 "          format: int32\n" +
                 "        example: 1\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: bean answer\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -460,7 +460,7 @@ public class ReaderTest {
                 "          format: int32\n" +
                 "        example: 1\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: bean answer\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -1161,7 +1161,7 @@ public class ReaderTest {
                 "    get:\n" +
                 "      operationId: getDistances\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          content:\n" +
                 "            application/json:\n" +
                 "              schema:\n" +
@@ -1214,7 +1214,7 @@ public class ReaderTest {
                 "        object\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -1226,7 +1226,7 @@ public class ReaderTest {
                 "            '*/*':\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/GenericError'\n" +
-                "        401:\n" +
+                "        \"401\":\n" +
                 "          $ref: '#/components/responses/invalidJWT'\n" +
                 "      deprecated: true\n" +
                 "components:\n" +
@@ -1269,7 +1269,7 @@ public class ReaderTest {
                 "      operationId: getWithPayloadResponse\n" +
                 "      parameters: []\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -1281,7 +1281,7 @@ public class ReaderTest {
                 "            '*/*':\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/GenericError'\n" +
-                "        401:\n" +
+                "        \"401\":\n" +
                 "          $ref: '#/components/responses/invalidJWT'\n" +
                 "      deprecated: true\n" +
                 "components:\n" +
@@ -1750,7 +1750,7 @@ public class ReaderTest {
                 "      description: Defines a simple get operation with no inputs and a complex output\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          headers:\n" +
                 "            Rate-Limit-Limit:\n" +
@@ -1879,7 +1879,7 @@ public class ReaderTest {
                 "      summary: Simple get operation\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      callbacks:\n" +
                 "        testCallback1:\n" +
@@ -1903,16 +1903,16 @@ public class ReaderTest {
                 "    get:\n" +
                 "      operationId: schemaImpl\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: OK\n" +
                 "          content:\n" +
                 "            '*/*':\n" +
                 "              schema:\n" +
                 "                type: string\n" +
                 "                format: uri\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Bad Request\n" +
-                "        500:\n" +
+                "        \"500\":\n" +
                 "          description: Internal Server Error\n";
         SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
         PrimitiveType.customExcludedClasses().add(URI.class.getName());
@@ -1923,7 +1923,7 @@ public class ReaderTest {
                 "    get:\n" +
                 "      operationId: schemaImpl_1\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: OK\n" +
                 "          content:\n" +
                 "            '*/*':\n" +
@@ -1965,9 +1965,9 @@ public class ReaderTest {
                 "                    type: boolean\n" +
                 "                  opaque:\n" +
                 "                    type: boolean\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Bad Request\n" +
-                "        500:\n" +
+                "        \"500\":\n" +
                 "          description: Internal Server Error\n";
         SerializationMatchers.assertEqualsToYaml(openAPI, yaml);
         PrimitiveType.customExcludedClasses().remove(URI.class.getName());

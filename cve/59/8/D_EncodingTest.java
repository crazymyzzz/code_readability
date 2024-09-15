@@ -42,7 +42,7 @@ public class EncodingTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex\n" +
                 "      operationId: simpleGet\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -112,7 +112,7 @@ public class EncodingTest extends AbstractAnnotationTest {
                 "                style: form\n" +
                 "                allowReserved: true\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!";
 
         String extractedYAML = openApiYAML.substring(start, end);
@@ -188,7 +188,7 @@ public class EncodingTest extends AbstractAnnotationTest {
                 "                style: form\n" +
                 "                allowReserved: true\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!";
 
         String extractedYAML = openApiYAML.substring(start, end);
@@ -265,7 +265,7 @@ public class EncodingTest extends AbstractAnnotationTest {
                 "                explode: true\n" +
                 "        required: true\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!";
         String extractedYAML = openApiYAML.substring(start, end);
 
@@ -312,7 +312,7 @@ public class EncodingTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!";
         String extractedYAML = openApiYAML.substring(start, end);
 

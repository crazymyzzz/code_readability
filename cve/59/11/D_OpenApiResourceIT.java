@@ -368,7 +368,7 @@ public class OpenApiResourceIT extends AbstractAnnotationTest {
             "      description: Return whole car\n" +
             "      operationId: getAll\n" +
             "      responses:\n" +
-            "        200:\n" +
+            "        \"200\":\n" +
             "          content:\n" +
             "            application/json:\n" +
             "              schema:\n" +
@@ -382,7 +382,7 @@ public class OpenApiResourceIT extends AbstractAnnotationTest {
             "      description: Return car detail\n" +
             "      operationId: getDetails\n" +
             "      responses:\n" +
-            "        200:\n" +
+            "        \"200\":\n" +
             "          content:\n" +
             "            application/json:\n" +
             "              schema:\n" +
@@ -410,7 +410,7 @@ public class OpenApiResourceIT extends AbstractAnnotationTest {
             "      description: Return car summaries\n" +
             "      operationId: getSummaries\n" +
             "      responses:\n" +
-            "        200:\n" +
+            "        \"200\":\n" +
             "          content:\n" +
             "            application/json:\n" +
             "              schema:\n" +
@@ -485,7 +485,7 @@ public class OpenApiResourceIT extends AbstractAnnotationTest {
             "        schema:\n" +
             "          type: string\n" +
             "      responses:\n" +
-            "        200:\n" +
+            "        \"200\":\n" +
             "          description: Returns widget with matching id\n" +
             "          content:\n" +
             "            application/json:\n" +

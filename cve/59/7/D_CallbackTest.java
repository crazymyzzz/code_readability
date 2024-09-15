@@ -70,10 +70,10 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "                  format: uuid\n" +
                 "                  readOnly: true\n" +
                 "              responses:\n" +
-                "                200:\n" +
+                "                \"200\":\n" +
                 "                  description: Return this code if the callback was received and processed\n" +
                 "                    successfully\n" +
-                "                205:\n" +
+                "                \"205\":\n" +
                 "                  description: Return this code to unsubscribe from future data updates\n" +
                 "                default:\n" +
                 "                  description: All other response codes will disable this callback\n" +
@@ -159,7 +159,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "      summary: Simple get operation\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      callbacks:\n" +
                 "        testCallback1:\n" +
@@ -197,7 +197,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "      summary: Simple get operation\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      callbacks:\n" +
                 "        testCallback1:\n" +
@@ -206,7 +206,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "              summary: get all the reviews\n" +
                 "              operationId: getAllReviews\n" +
                 "              responses:\n" +
-                "                200:\n" +
+                "                \"200\":\n" +
                 "                  description: successful operation\n" +
                 "                  content:\n" +
                 "                    application/json:\n" +
@@ -260,7 +260,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "      summary: Simple get operation\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      callbacks:\n" +
                 "        testCallback1:\n" +
@@ -269,7 +269,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "              summary: get all the reviews\n" +
                 "              operationId: getAllReviews\n" +
                 "              responses:\n" +
-                "                200:\n" +
+                "                \"200\":\n" +
                 "                  description: successful operation\n" +
                 "                  content:\n" +
                 "                    application/json:\n" +
@@ -326,7 +326,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "      summary: Simple get operation\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      callbacks:\n" +
                 "        testCallback:\n" +
@@ -337,7 +337,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "              summary: get all the reviews\n" +
                 "              operationId: getAllReviews\n" +
                 "              responses:\n" +
-                "                200:\n" +
+                "                \"200\":\n" +
                 "                  description: successful operation\n" +
                 "                  content:\n" +
                 "                    application/json:\n" +
@@ -361,7 +361,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "      summary: Simple get operation\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      callbacks:\n" +
                 "        testCallback:\n" +
@@ -372,7 +372,7 @@ public class CallbackTest extends AbstractAnnotationTest {
                 "              summary: get all the reviews\n" +
                 "              operationId: getAllReviews\n" +
                 "              responses:\n" +
-                "                200:\n" +
+                "                \"200\":\n" +
                 "                  description: successful operation\n" +
                 "                  content:\n" +
                 "                    application/json:\n" +

@@ -37,7 +37,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      deprecated: true";
         String extractedYAML = openApiYAML.substring(start, end);
@@ -108,7 +108,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -187,7 +187,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex output\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -219,7 +219,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "        example:\n" +
                 "          id: 19877734\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -323,7 +323,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex output\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          headers:\n" +
                 "            Rate-Limit-Limit:\n" +
@@ -370,7 +370,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex output\n" +
                 "      operationId: getWithPayloadResponse\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "          headers:\n" +
                 "            X-Rate-Limit-Desc:\n" +
@@ -410,7 +410,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "            application/json:\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/Pet'\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid tag value\n" +
                 "  /pet/findByCategory/{category}:\n" +
                 "    get:\n" +
@@ -440,7 +440,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "            application/json:\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/Pet'\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid category value\n" +
                 "  /pet/{petId}:\n" +
                 "    get:\n" +
@@ -465,9 +465,9 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "            application/xml:\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/Pet'\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid ID supplied\n" +
-                "        404:\n" +
+                "        \"404\":\n" +
                 "          description: Pet not found\n" +
                 "  /pet/bodynoannotation:\n" +
                 "    post:\n" +
@@ -482,7 +482,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "            schema:\n" +
                 "              $ref: '#/components/schemas/Pet'\n" +
                 "      responses:\n" +
-                "        405:\n" +
+                "        \"405\":\n" +
                 "          description: Invalid input\n" +
                 "  /pet/bodyid:\n" +
                 "    post:\n" +
@@ -501,7 +501,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "              format: int32\n" +
                 "        required: true\n" +
                 "      responses:\n" +
-                "        405:\n" +
+                "        \"405\":\n" +
                 "          description: Invalid input\n" +
                 "  /pet/bodyidnoannotation:\n" +
                 "    post:\n" +
@@ -518,7 +518,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "              type: integer\n" +
                 "              format: int32\n" +
                 "      responses:\n" +
-                "        405:\n" +
+                "        \"405\":\n" +
                 "          description: Invalid input\n" +
                 "  /pet:\n" +
                 "    put:\n" +
@@ -532,11 +532,11 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "              $ref: '#/components/schemas/Pet'\n" +
                 "        required: true\n" +
                 "      responses:\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid ID supplied\n" +
-                "        404:\n" +
+                "        \"404\":\n" +
                 "          description: Pet not found\n" +
-                "        405:\n" +
+                "        \"405\":\n" +
                 "          description: Validation exception\n" +
                 "    post:\n" +
                 "      summary: Add a new pet to the store\n" +
@@ -552,7 +552,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "              $ref: '#/components/schemas/Pet'\n" +
                 "        required: true\n" +
                 "      responses:\n" +
-                "        405:\n" +
+                "        \"405\":\n" +
                 "          description: Invalid input\n" +
                 "  /pet/findByStatus:\n" +
                 "    get:\n" +
@@ -582,7 +582,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "            application/json:\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/Pet'\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid status value\n" +
                 "components:\n" +
                 "  schemas:\n" +
@@ -767,7 +767,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "            application/json:\n" +
                 "              schema:\n" +
                 "                $ref: '#/components/schemas/User'\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: User not found\n" +
                 "    put:\n" +
                 "      summary: Updated user\n" +
@@ -799,11 +799,11 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "              $ref: '#/components/schemas/User'\n" +
                 "        required: true\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: user updated\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid user supplied\n" +
-                "        404:\n" +
+                "        \"404\":\n" +
                 "          description: User not found\n" +
                 "    delete:\n" +
                 "      summary: Delete user\n" +
@@ -817,11 +817,11 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "        schema:\n" +
                 "          type: string\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: user deteled\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid username supplied\n" +
-                "        404:\n" +
+                "        \"404\":\n" +
                 "          description: User not found\n" +
                 "  /user/login:\n" +
                 "    get:\n" +
@@ -850,7 +850,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "            application/xml:\n" +
                 "              schema:\n" +
                 "                type: string\n" +
-                "        400:\n" +
+                "        \"400\":\n" +
                 "          description: Invalid username/password supplied\n" +
                 "  /user/logout:\n" +
                 "    get:\n" +
@@ -958,7 +958,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "              $ref: '#/components/schemas/User'\n" +
                 "        required: true\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: aaa\n" +
                 "          content:\n" +
                 "            application/json:\n" +
@@ -983,7 +983,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "              $ref: '#/components/schemas/User'\n" +
                 "        required: true\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: aaa\n" +
                 "components:\n" +
                 "  schemas:\n" +
@@ -1066,7 +1066,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      security:\n" +
                 "      - petstore-auth:\n" +
@@ -1107,7 +1107,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      security:\n" +
                 "      - petstore-auth:\n" +
@@ -1149,7 +1149,7 @@ public class AnnotatedOperationMethodTest extends AbstractAnnotationTest {
                 "      description: Defines a simple get operation with no inputs and a complex\n" +
                 "      operationId: getWithNoParameters\n" +
                 "      responses:\n" +
-                "        200:\n" +
+                "        \"200\":\n" +
                 "          description: voila!\n" +
                 "      security:\n" +
                 "      - review-auth:\n" +

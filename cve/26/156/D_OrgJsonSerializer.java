@@ -23,6 +23,7 @@ import io.jsonwebtoken.lang.Collections;
 import io.jsonwebtoken.lang.DateFormats;
 import io.jsonwebtoken.lang.Objects;
 import io.jsonwebtoken.lang.Strings;
+import io.jsonwebtoken.lang.Supplier;
 import org.json.JSONArray;
 import org.json.JSONObject;
 
@@ -40,9 +41,9 @@ public class OrgJsonSerializer<T> implements Serializer<T> {
 
     // we need reflection for these because of Android - see https://github.com/jwtk/jjwt/issues/388
     private static final String JSON_WRITER_CLASS_NAME = "org.json.JSONWriter";
-    private static final Class[] VALUE_TO_STRING_ARG_TYPES = new Class[]{Object.class};
+    private static final Class<?>[] VALUE_TO_STRING_ARG_TYPES = new Class[]{Object.class};
     private static final String JSON_STRING_CLASS_NAME = "org.json.JSONString";
-    private static final Class JSON_STRING_CLASS;
+    private static final Class<?> JSON_STRING_CLASS;
 
     static { // see see https://github.com/jwtk/jjwt/issues/388
         if (Classes.isAvailable(JSON_STRING_CLASS_NAME)) {
@@ -82,14 +83,18 @@ public class OrgJsonSerializer<T> implements Serializer<T> {
             return JSONObject.NULL;
         }
 
+        if (object instanceof Supplier) {
+            object = ((Supplier<?>)object).get();
+        }
+
         if (object instanceof JSONObject || object instanceof JSONArray
-            || JSONObject.NULL.equals(object) || isJSONString(object)
-            || object instanceof Byte || object instanceof Character
-            || object instanceof Short || object instanceof Integer
-            || object instanceof Long || object instanceof Boolean
-            || object instanceof Float || object instanceof Double
-            || object instanceof String || object instanceof BigInteger
-            || object instanceof BigDecimal || object instanceof Enum) {
+                || JSONObject.NULL.equals(object) || isJSONString(object)
+                || object instanceof Byte || object instanceof Character
+                || object instanceof Short || object instanceof Integer
+                || object instanceof Long || object instanceof Boolean
+                || object instanceof Float || object instanceof Double
+                || object instanceof String || object instanceof BigInteger
+                || object instanceof BigDecimal || object instanceof Enum) {
             return object;
         }
 
@@ -114,14 +119,15 @@ public class OrgJsonSerializer<T> implements Serializer<T> {
             Map<?, ?> map = (Map<?, ?>) object;
             return toJSONObject(map);
         }
+
+        if (Objects.isArray(object)) {
+            object = Collections.arrayToList(object); //sets object to List, will be converted in next if-statement:
+        }
+
         if (object instanceof Collection) {
             Collection<?> coll = (Collection<?>) object;
             return toJSONArray(coll);
         }
-        if (Objects.isArray(object)) {
-            Collection c = Collections.arrayToList(object);
-            return toJSONArray(c);
-        }
 
         //not an immediately JSON-compatible object and probably a JavaBean (or similar).  We can't convert that
         //directly without using a marshaller of some sort:
@@ -145,7 +151,7 @@ public class OrgJsonSerializer<T> implements Serializer<T> {
         return obj;
     }
 
-    private JSONArray toJSONArray(Collection c) {
+    private JSONArray toJSONArray(Collection<?> c) {
 
         JSONArray array = new JSONArray();
 
@@ -167,7 +173,7 @@ public class OrgJsonSerializer<T> implements Serializer<T> {
         //
         // This is sufficient for all JJWT-supported scenarios on Android since Android users shouldn't ever use
         // JJWT's internal Serializer implementation for general JSON serialization.  That is, its intended use
-        // is within the context of JwtBuilder execution and not for application use outside of that.
+        // is within the context of JwtBuilder execution and not for application use beyond that.
         if (o instanceof JSONObject) {
             s = o.toString();
         } else {

@@ -35,7 +35,6 @@ import java.util.Map;
  */
 public class OrgJsonDeserializer implements Deserializer<Object> {
 
-    @SuppressWarnings("unchecked")
     @Override
     public Object deserialize(byte[] bytes) throws DeserializationException {
 
@@ -91,7 +90,7 @@ public class OrgJsonDeserializer implements Deserializer<Object> {
         int length = a.length();
         List<Object> list = new ArrayList<>(length);
         // https://github.com/jwtk/jjwt/issues/380: use a.get(i) and *not* a.toList() for Android compatibility:
-        for( int i = 0; i < length; i++) {
+        for (int i = 0; i < length; i++) {
             Object value = a.get(i);
             value = convertIfNecessary(value);
             list.add(value);

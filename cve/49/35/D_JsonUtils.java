@@ -73,6 +73,7 @@ public class JsonUtils {
         Type existingObjectType = new TypeToken<CompositeResult<T>>() { }.getType();
 
         try (FileReader reader = new FileReader(filename)) {
+
             existingObject = new GsonBuilder()
                     .registerTypeAdapter(existingObjectType, deserializer)
                     .create()

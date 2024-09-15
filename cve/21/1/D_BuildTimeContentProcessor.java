@@ -230,13 +230,14 @@ void createBuildTimeConstJsTemplate(CurateOutcomeBuildItem curateOutcomeBuildIte
 
         InternalImportMapBuildItem internalImportMapBuildItem = new InternalImportMapBuildItem();
 
+        var mapper = DatabindCodec.mapper().writerWithDefaultPrettyPrinter();
         for (BuildTimeConstBuildItem buildTimeConstBuildItem : buildTimeConstBuildItems) {
             Map<String, Object> data = new HashMap<>();
             if (buildTimeConstBuildItem.hasBuildTimeData()) {
                 for (Map.Entry<String, Object> pageData : buildTimeConstBuildItem.getBuildTimeData().entrySet()) {
                     try {
                         String key = pageData.getKey();
-                        String value = DatabindCodec.prettyMapper().writeValueAsString(pageData.getValue());
+                        String value = mapper.writeValueAsString(pageData.getValue());
                         data.put(key, value);
                     } catch (JsonProcessingException ex) {
                         log.error("Could not create Json Data for Dev UI page", ex);


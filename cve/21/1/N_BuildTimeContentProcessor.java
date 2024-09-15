

/**
 * This creates static content that is used in dev UI. For example the index.html and any other data (json) available on build
 * time
 */
public class BuildTimeContentProcessor {
    

    /**
     * Here we find all build time data and make then available via a const
     *
     * js components can import the const with "import {constName} from '{ext}-data';"
     *
     * @param pageBuildItems
     * @param quteTemplateProducer
     * @param internalImportMapProducer
     */
    @BuildStep(onlyIf = IsDevelopment.class)
    void createBuildTimeConstJsTemplate(CurateOutcomeBuildItem curateOutcomeBuildItem,
            NonApplicationRootPathBuildItem nonApplicationRootPathBuildItem,
            List<BuildTimeConstBuildItem> buildTimeConstBuildItems,
            BuildProducer<QuteTemplateBuildItem> quteTemplateProducer,
            BuildProducer<InternalImportMapBuildItem> internalImportMapProducer) {

        String contextRoot = nonApplicationRootPathBuildItem.getNonApplicationRootPath() + DEV_UI + SLASH;

        QuteTemplateBuildItem quteTemplateBuildItem = new QuteTemplateBuildItem(
                QuteTemplateBuildItem.DEV_UI);

        InternalImportMapBuildItem internalImportMapBuildItem = new InternalImportMapBuildItem();

        var mapper = DatabindCodec.mapper().writerWithDefaultPrettyPrinter();
        for (BuildTimeConstBuildItem buildTimeConstBuildItem : buildTimeConstBuildItems) {
            Map<String, Object> data = new HashMap<>();
            if (buildTimeConstBuildItem.hasBuildTimeData()) {
                for (Map.Entry<String, Object> pageData : buildTimeConstBuildItem.getBuildTimeData().entrySet()) {
                    try {
                        String key = pageData.getKey();
                        String value = mapper.writeValueAsString(pageData.getValue());
                        data.put(key, value);
                    } catch (JsonProcessingException ex) {
                        log.error("Could not create Json Data for Dev UI page", ex);
                    }
                }
            }
            if (!data.isEmpty()) {
                Map<String, Object> qutedata = new HashMap<>();
                qutedata.put("buildTimeData", data);

                String ref = buildTimeConstBuildItem.getExtensionPathName(curateOutcomeBuildItem) + "-data";
                String file = ref + ".js";
                quteTemplateBuildItem.add("build-time-data.js", file, qutedata);
                internalImportMapBuildItem.add(ref, contextRoot + file);
            }
        }
        quteTemplateProducer.produce(quteTemplateBuildItem);
        internalImportMapProducer.produce(internalImportMapBuildItem);
    }

 
}

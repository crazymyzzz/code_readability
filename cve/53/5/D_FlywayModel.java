@@ -203,7 +203,7 @@ public class FlywayModel {
         result.failOnMissingLocations = failOnMissingLocations.merge(otherPojo.failOnMissingLocations);
         result.loggers = loggers.merge(otherPojo.loggers);
         result.defaultSchema = defaultSchema.merge(otherPojo.defaultSchema);
-        result.placeholders = placeholders.merge(otherPojo.placeholders);
+        result.placeholders = MergeUtils.merge(placeholders, otherPojo.placeholders, (a,b) -> b != null ? b : a);
         result.reportEnabled = reportEnabled.merge(otherPojo.reportEnabled);
         result.propertyResolvers = MergeUtils.merge(propertyResolvers, otherPojo.propertyResolvers, (a,b) -> b != null ? b : a); // TODO: more granular merge
         result.pluginConfigurations = MergeUtils.merge(pluginConfigurations, otherPojo.pluginConfigurations, (a,b) -> b != null ? b : a);

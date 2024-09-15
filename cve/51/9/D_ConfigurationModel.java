@@ -18,10 +18,12 @@ package org.flywaydb.core.internal.configuration.models;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 import lombok.Setter;
+import lombok.experimental.ExtensionMethod;
 import org.flywaydb.core.internal.util.MergeUtils;
 
 import java.util.HashMap;
 import java.util.Map;
+import java.util.UUID;
 
 @Getter
 @Setter
@@ -29,6 +31,7 @@ import java.util.Map;
 public class ConfigurationModel {
     private Map<String, EnvironmentModel> environments = new HashMap<>();
     private FlywayModel flyway = new FlywayModel();
+    private String id;
 
     public static ConfigurationModel defaults() {
         ConfigurationModel model = new ConfigurationModel();
@@ -39,6 +42,8 @@ public class ConfigurationModel {
 
     public ConfigurationModel merge(ConfigurationModel otherPojo) {
         ConfigurationModel result = new ConfigurationModel();
+        result.id = MergeUtils.merge(id, otherPojo.id);
+
         result.flyway = flyway != null ? flyway.merge(otherPojo.flyway) : otherPojo.flyway;
         result.environments = MergeUtils.merge(environments, otherPojo.environments, EnvironmentModel::merge);
         return result;

@@ -59,6 +59,7 @@ public class FlywayModel {
     private List<String> sqlMigrationSuffixes;
     private Boolean cleanDisabled;
     private Boolean cleanOnValidationError;
+    private Boolean communityDBSupportEnabled;
     private List<String> locations;
     private String table;
     private String tablespace;
@@ -116,6 +117,7 @@ public class FlywayModel {
          model.sqlMigrationSuffixes = Arrays.asList(".sql");
          model.cleanDisabled = true;
          model.cleanOnValidationError = false;
+         model.communityDBSupportEnabled = true;
          model.locations = new ArrayList<>(Collections.singletonList("db/migration"));
          model.target = "latest";
          model.table = "flyway_schema_history";
@@ -171,6 +173,7 @@ public class FlywayModel {
         result.sqlMigrationSuffixes = sqlMigrationSuffixes.merge(otherPojo.sqlMigrationSuffixes);
         result.cleanDisabled = cleanDisabled.merge(otherPojo.cleanDisabled);
         result.cleanOnValidationError = cleanOnValidationError.merge(otherPojo.cleanOnValidationError);
+        result.communityDBSupportEnabled = communityDBSupportEnabled.merge(otherPojo.communityDBSupportEnabled);
         result.locations = locations.merge(otherPojo.locations);
         result.table = table.merge(otherPojo.table);
         result.tablespace = tablespace.merge(otherPojo.tablespace);

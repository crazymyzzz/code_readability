@@ -88,6 +88,7 @@ public class CommandResultFactory {
     public static InfoOutput createInfoOutput(Set<MigrationInfo> undoableMigrations, MigrationInfo migrationInfo) {
         return new InfoOutput(getCategory(migrationInfo),
                               migrationInfo.getVersion() != null ? migrationInfo.getVersion().getVersion() : "",
+                              migrationInfo.getVersion() != null ? migrationInfo.getVersion().getRawVersion() : "",
                               migrationInfo.getDescription(),
                               migrationInfo.getType() != null ? migrationInfo.getType().toString() : "",
                               migrationInfo.getInstalledOn() != null ? migrationInfo.getInstalledOn().toInstant().toString() : "",

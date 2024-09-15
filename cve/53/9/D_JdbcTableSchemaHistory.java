
@@ -271,12 +272,16 @@ class JdbcTableSchemaHistory extends SchemaHistory {
                              .forEach(am -> repairResult.migrationsRemoved.add(CommandResultFactory.createRepairOutput(am)));
 
             for (AppliedMigration appliedMigration : appliedMigrations) {
+                Pair<String, Object> deleteStatement;
                 if (appliedMigration.getVersion() != null) {
-                    jdbcTemplate.execute(database.getDeleteStatement(table, true ), appliedMigration.getVersion().getVersion());
+                    deleteStatement = database.getDeleteStatement(table, true, appliedMigration.getVersion().getVersion());
                 } else {
-                    jdbcTemplate.execute(database.getDeleteStatement(table, false ), appliedMigration.getDescription());
+                    deleteStatement = database.getDeleteStatement(table, false, appliedMigration.getDescription());
                 }
 
+                if (deleteStatement != null) {
+                    jdbcTemplate.execute(deleteStatement.getLeft(), deleteStatement.getRight());
+                }
             }
 
             clearCache();

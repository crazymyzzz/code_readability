@@ -54,7 +54,7 @@ public class ScriptMigrationExecutor implements MigrationExecutor {
         } else if (context.getConnection() == null) {
             executeOnce(context);
         } else {
-            DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection());
+            DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection(), context.getConfiguration());
 
             DatabaseExecutionStrategy strategy = databaseType.createExecutionStrategy(context.getConnection());
             strategy.execute(() -> {

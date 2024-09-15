@@ -50,7 +50,7 @@ public class SqlMigrationExecutor implements MigrationExecutor {
 
     @Override
     public void execute(final Context context) throws SQLException {
-        DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection());
+        DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection(), context.getConfiguration());
 
         DatabaseExecutionStrategy strategy = databaseType.createExecutionStrategy(context.getConnection());
         strategy.execute(() -> {

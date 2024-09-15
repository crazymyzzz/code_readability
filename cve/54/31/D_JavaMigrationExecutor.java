@@ -47,7 +47,7 @@ public class JavaMigrationExecutor implements MigrationExecutor {
         if (statementInterceptor != null) {
             statementInterceptor.javaMigration(javaMigration);
         } else {
-            DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection());
+            DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection(), context.getConfiguration());
 
             DatabaseExecutionStrategy strategy = databaseType.createExecutionStrategy(context.getConnection());
             strategy.execute(() -> {

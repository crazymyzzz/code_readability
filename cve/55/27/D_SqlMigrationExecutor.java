@@ -19,12 +19,17 @@
  */
 package org.flywaydb.core.internal.resolver.sql;
 
+import java.util.ArrayList;
+import java.util.HashMap;
+import java.util.List;
+import java.util.Map;
 import lombok.RequiredArgsConstructor;
 import org.flywaydb.core.api.executor.Context;
 import org.flywaydb.core.api.executor.MigrationExecutor;
 import org.flywaydb.core.internal.database.DatabaseExecutionStrategy;
 import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
+import org.flywaydb.core.internal.jdbc.Results;
 import org.flywaydb.core.internal.sqlscript.SqlScript;
 import org.flywaydb.core.internal.sqlscript.SqlScriptExecutorFactory;
 
@@ -53,20 +58,21 @@ public class SqlMigrationExecutor implements MigrationExecutor {
     private final boolean batch;
 
     @Override
-    public void execute(final Context context) throws SQLException {
+    public List<Results> execute(final Context context) throws SQLException {
         DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(context.getConnection(), context.getConfiguration());
 
         DatabaseExecutionStrategy strategy = databaseType.createExecutionStrategy(context.getConnection());
-        strategy.execute(() -> {
-            executeOnce(context);
-            return true;
+        return strategy.execute(() -> {
+            return executeOnce(context);
         });
     }
 
-    private void executeOnce(Context context) {
+    private List<Results> executeOnce(Context context) {
+
         boolean outputQueryResults = context.getConfiguration().isOutputQueryResults();
 
-        sqlScriptExecutorFactory.createSqlScriptExecutor(context.getConnection(), undo, batch, outputQueryResults).execute(sqlScript, context.getConfiguration());
+        var executorFactory = sqlScriptExecutorFactory.createSqlScriptExecutor(context.getConnection(), undo, batch, outputQueryResults);
+        return executorFactory.execute(sqlScript, context.getConfiguration());
     }
 
     @Override

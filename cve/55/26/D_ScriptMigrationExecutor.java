@@ -28,6 +28,7 @@ import org.flywaydb.core.api.resource.LoadableResource;
 import org.flywaydb.core.internal.database.DatabaseExecutionStrategy;
 import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
+import org.flywaydb.core.internal.jdbc.Results;
 import org.flywaydb.core.internal.jdbc.StatementInterceptor;
 import org.flywaydb.core.internal.parser.ParsingContext;
 import org.flywaydb.core.internal.resource.ResourceName;
@@ -52,7 +53,7 @@ public class ScriptMigrationExecutor implements MigrationExecutor {
     private final StatementInterceptor statementInterceptor;
 
     @Override
-    public void execute(final Context context) throws SQLException {
+    public List<Results> execute(final Context context) throws SQLException {
         if (statementInterceptor != null) {
             statementInterceptor.scriptMigration(resource);
         } else if (context.getConnection() == null) {
@@ -66,6 +67,8 @@ public class ScriptMigrationExecutor implements MigrationExecutor {
                 return true;
             });
         }
+
+        return List.of();
     }
 
     private void executeOnce(final Context context) {

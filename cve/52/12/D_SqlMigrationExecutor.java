@@ -74,4 +74,9 @@ public class SqlMigrationExecutor implements MigrationExecutor {
     public boolean shouldExecute() {
         return sqlScript.shouldExecute();
     }
+
+    @Override
+    public String shouldExecuteExpression() {
+        return sqlScript.shouldExecuteExpression();
+    }
 }
\ No newline at end of file

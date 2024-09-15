@@ -91,6 +91,7 @@ public class CommandResultFactory {
                               migrationInfo.getPhysicalLocation() != null ? migrationInfo.getPhysicalLocation() : "",
                               getUndoablePath(migrationInfo, undoableMigrations),
                               migrationInfo.getInstalledBy() != null ? migrationInfo.getInstalledBy() : "",
+                              migrationInfo.getShouldExecuteExpression(),
                               migrationInfo.getExecutionTime() != null ? migrationInfo.getExecutionTime() : 0);
     }
 

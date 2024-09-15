@@ -110,6 +110,14 @@ public class MigrationInfoImpl implements MigrationInfo {
         return resolvedMigration.getScript();
     }
 
+    @Override
+    public String getShouldExecuteExpression() {
+        if(resolvedMigration != null) {
+            return resolvedMigration.getExecutor().shouldExecuteExpression();
+        }
+        return null;
+    }
+
 
 
 

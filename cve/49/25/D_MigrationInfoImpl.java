@@ -248,7 +248,7 @@ public class MigrationInfoImpl implements MigrationInfo {
             }
         }
 
-        if (!context.isIgnoredIgnored() && MigrationState.IGNORED == state && !resolvedMigration.getType().isBaseline()) {
+        if (!context.isIgnoredIgnored() && MigrationState.IGNORED == state && !resolvedMigration.getType().isBaseline() && !resolvedMigration.getType().isUndo()) {
             if (shouldNotExecuteMigration) {
                 return null;
             }

@@ -74,9 +74,7 @@ public abstract class SchemaHistory {
     public final boolean hasNonSyntheticAppliedMigrations() {
         for (AppliedMigration appliedMigration : allAppliedMigrations()) {
             if (!appliedMigration.getType().isSynthetic()
-
-
-
+                    && !appliedMigration.getType().isUndo()
             ) {
                 return true;
             }

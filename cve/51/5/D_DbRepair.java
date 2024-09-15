@@ -156,9 +156,7 @@ public class DbRepair {
             MigrationInfoImpl migrationInfoImpl = (MigrationInfoImpl) migrationInfo;
 
             if (migrationInfo.getType().isSynthetic()
-
-
-
+                    || migrationInfo.getType().isUndo()
             ) {
                 continue;
             }
@@ -190,9 +188,7 @@ public class DbRepair {
                     && resolved.getVersion() != null
                     && applied != null
                     && !applied.getType().isSynthetic()
-
-
-
+                    && migrationInfoImpl.getState() != MigrationState.UNDONE
                     && migrationInfoImpl.getState() != MigrationState.IGNORED
                     && updateNeeded(resolved, applied)) {
                 schemaHistory.update(applied, resolved);
@@ -205,9 +201,7 @@ public class DbRepair {
                     && resolved.getVersion() == null
                     && applied != null
                     && !applied.getType().isSynthetic()
-
-
-
+                    && migrationInfoImpl.getState() != MigrationState.UNDONE
                     && migrationInfoImpl.getState() != MigrationState.IGNORED
                     && resolved.checksumMatchesWithoutBeingIdentical(applied.getChecksum())) {
                 schemaHistory.update(applied, resolved);

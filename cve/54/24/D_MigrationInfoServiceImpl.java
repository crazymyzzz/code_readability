@@ -359,7 +359,7 @@ public class MigrationInfoServiceImpl implements MigrationInfoService, Operation
             if (!av.getLeft().getType().isSynthetic() && version.equals(av.getLeft().getVersion())) {
                 if (av.getRight().deleted) {
                     throw new FlywayException("Corrupted schema history: multiple delete entries for version " + version,
-                                              ErrorCode.DUPLICATE_DELETED_MIGRATION);
+                                              CoreErrorCode.DUPLICATE_DELETED_MIGRATION);
                 } else {
                     av.getRight().deleted = true;
                     return;

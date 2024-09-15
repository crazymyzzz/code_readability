@@ -229,10 +229,10 @@ public class MigrationInfoImpl implements MigrationInfo {
         if (state.isFailed() && (!context.isFutureIgnored() || MigrationState.FUTURE_FAILED != state)) {
             if (getVersion() == null) {
                 String errorMessage = "Detected failed repeatable migration: " + getDescription() + ".\nPlease remove any half-completed changes then run repair to fix the schema history.";
-                return new ErrorDetails(ErrorCode.FAILED_REPEATABLE_MIGRATION, errorMessage);
+                return new ErrorDetails(CoreErrorCode.FAILED_REPEATABLE_MIGRATION, errorMessage);
             }
             String errorMessage = "Detected failed migration to version " + getVersion() + " (" + getDescription() + ")" + ".\nPlease remove any half-completed changes then run repair to fix the schema history.";
-            return new ErrorDetails(ErrorCode.FAILED_VERSIONED_MIGRATION, errorMessage);
+            return new ErrorDetails(CoreErrorCode.FAILED_VERSIONED_MIGRATION, errorMessage);
         }
 
 
@@ -249,10 +249,10 @@ public class MigrationInfoImpl implements MigrationInfo {
                 && (!context.isFutureIgnored() || (MigrationState.FUTURE_SUCCESS != state && MigrationState.FUTURE_FAILED != state))) {
             if (appliedMigration.getVersion() != null) {
                 String errorMessage = "Detected applied migration not resolved locally: " + getVersion() + ".\nIf you removed this migration intentionally, run repair to mark the migration as deleted.";
-                return new ErrorDetails(ErrorCode.APPLIED_VERSIONED_MIGRATION_NOT_RESOLVED, errorMessage);
+                return new ErrorDetails(CoreErrorCode.APPLIED_VERSIONED_MIGRATION_NOT_RESOLVED, errorMessage);
             } else {
                 String errorMessage = "Detected applied migration not resolved locally: " + getDescription() + ".\nIf you removed this migration intentionally, run repair to mark the migration as deleted.";
-                return new ErrorDetails(ErrorCode.APPLIED_REPEATABLE_MIGRATION_NOT_RESOLVED, errorMessage);
+                return new ErrorDetails(CoreErrorCode.APPLIED_REPEATABLE_MIGRATION_NOT_RESOLVED, errorMessage);
             }
         }
 
@@ -262,24 +262,24 @@ public class MigrationInfoImpl implements MigrationInfo {
             }
             if (getVersion() != null) {
                 String errorMessage = "Detected resolved migration not applied to database: " + getVersion() + ".\nTo ignore this migration, set -ignoreMigrationPatterns='*:ignored'. To allow executing this migration, set -outOfOrder=true.";
-                return new ErrorDetails(ErrorCode.RESOLVED_VERSIONED_MIGRATION_NOT_APPLIED, errorMessage);
+                return new ErrorDetails(CoreErrorCode.RESOLVED_VERSIONED_MIGRATION_NOT_APPLIED, errorMessage);
             }
             String errorMessage = "Detected resolved repeatable migration not applied to database: " + getDescription() + ".\nTo ignore this migration, set -ignoreMigrationPatterns='*:ignored'.";
-            return new ErrorDetails(ErrorCode.RESOLVED_REPEATABLE_MIGRATION_NOT_APPLIED, errorMessage);
+            return new ErrorDetails(CoreErrorCode.RESOLVED_REPEATABLE_MIGRATION_NOT_APPLIED, errorMessage);
         }
 
         if (!context.isPendingIgnored() && MigrationState.PENDING == state) {
             if (getVersion() != null) {
                 String errorMessage = "Detected resolved migration not applied to database: " + getVersion() + ".\nTo fix this error, either run migrate, or set -ignoreMigrationPatterns='*:pending'.";
-                return new ErrorDetails(ErrorCode.RESOLVED_VERSIONED_MIGRATION_NOT_APPLIED, errorMessage);
+                return new ErrorDetails(CoreErrorCode.RESOLVED_VERSIONED_MIGRATION_NOT_APPLIED, errorMessage);
             }
             String errorMessage = "Detected resolved repeatable migration not applied to database: " + getDescription() + ".\nTo fix this error, either run migrate, or set -ignoreMigrationPatterns='*:pending'.";
-            return new ErrorDetails(ErrorCode.RESOLVED_REPEATABLE_MIGRATION_NOT_APPLIED, errorMessage);
+            return new ErrorDetails(CoreErrorCode.RESOLVED_REPEATABLE_MIGRATION_NOT_APPLIED, errorMessage);
         }
 
         if (!context.isPendingIgnored() && MigrationState.OUTDATED == state) {
             String errorMessage = "Detected outdated resolved repeatable migration that should be re-applied to database: " + getDescription() + ".\nRun migrate to execute this migration.";
-            return new ErrorDetails(ErrorCode.OUTDATED_REPEATABLE_MIGRATION, errorMessage);
+            return new ErrorDetails(CoreErrorCode.OUTDATED_REPEATABLE_MIGRATION, errorMessage);
         }
 
         if (resolvedMigration != null &&
@@ -291,17 +291,17 @@ public class MigrationInfoImpl implements MigrationInfo {
             if (getVersion() == null || getVersion().compareTo(context.appliedBaseline) > 0) {
                 if (resolvedMigration.getType() != appliedMigration.getType()) {
                     String mismatchMessage = createMismatchMessage("type", migrationIdentifier, appliedMigration.getType(), resolvedMigration.getType());
-                    return new ErrorDetails(ErrorCode.TYPE_MISMATCH, mismatchMessage);
+                    return new ErrorDetails(CoreErrorCode.TYPE_MISMATCH, mismatchMessage);
                 }
                 if (resolvedMigration.getVersion() != null || (context.isPendingIgnored() && MigrationState.OUTDATED != state && MigrationState.SUPERSEDED != state)) {
                     if (!resolvedMigration.checksumMatches(appliedMigration.getChecksum())) {
                         String mismatchMessage = createMismatchMessage("checksum", migrationIdentifier, appliedMigration.getChecksum(), resolvedMigration.getChecksum());
-                        return new ErrorDetails(ErrorCode.CHECKSUM_MISMATCH, mismatchMessage);
+                        return new ErrorDetails(CoreErrorCode.CHECKSUM_MISMATCH, mismatchMessage);
                     }
                 }
                 if (descriptionMismatch(resolvedMigration, appliedMigration)) {
                     String mismatchMessage = createMismatchMessage("description", migrationIdentifier, appliedMigration.getDescription(), resolvedMigration.getDescription());
-                    return new ErrorDetails(ErrorCode.DESCRIPTION_MISMATCH, mismatchMessage);
+                    return new ErrorDetails(CoreErrorCode.DESCRIPTION_MISMATCH, mismatchMessage);
                 }
             }
         }

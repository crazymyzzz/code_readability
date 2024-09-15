@@ -17,6 +17,7 @@ package org.flywaydb.core.internal.command;
 
 import lombok.CustomLog;
 import lombok.Getter;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.ErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.MigrationInfo;
@@ -429,9 +430,9 @@ public class DbMigrate {
 
         public ErrorCode getMigrationErrorCode() {
             if (migration.getVersion() != null) {
-                return ErrorCode.FAILED_VERSIONED_MIGRATION;
+                return CoreErrorCode.FAILED_VERSIONED_MIGRATION;
             } else {
-                return ErrorCode.FAILED_REPEATABLE_MIGRATION;
+                return CoreErrorCode.FAILED_REPEATABLE_MIGRATION;
             }
         }
 

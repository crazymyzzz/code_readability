@@ -16,7 +16,7 @@
 package org.flywaydb.core.internal.command;
 
 import lombok.CustomLog;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.ErrorDetails;
 import org.flywaydb.core.api.callback.Event;
 import org.flywaydb.core.api.configuration.Configuration;
@@ -85,7 +85,7 @@ public class DbValidate {
         if (!schema.exists()) {
             if (!migrationResolver.resolveMigrations(configuration).isEmpty() && !ValidatePatternUtils.isPendingIgnored(ignorePatterns)) {
                 String validationErrorMessage = "Schema " + schema + " doesn't exist yet";
-                ErrorDetails validationError = new ErrorDetails(ErrorCode.SCHEMA_DOES_NOT_EXIST, validationErrorMessage);
+                ErrorDetails validationError = new ErrorDetails(CoreErrorCode.SCHEMA_DOES_NOT_EXIST, validationErrorMessage);
                 return CommandResultFactory.createValidateResult(database.getCatalog(), validationError, 0, null, new ArrayList<>());
             }
             return CommandResultFactory.createValidateResult(database.getCatalog(), null, 0, null, new ArrayList<>());
@@ -135,7 +135,7 @@ public class DbValidate {
             }
             callbackExecutor.onEvent(Event.AFTER_VALIDATE);
         } else {
-            validationError = new ErrorDetails(ErrorCode.VALIDATE_ERROR, "Migrations have failed validation");
+            validationError = new ErrorDetails(CoreErrorCode.VALIDATE_ERROR, "Migrations have failed validation");
             callbackExecutor.onEvent(Event.AFTER_VALIDATE_ERROR);
         }
 

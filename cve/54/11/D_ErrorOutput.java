@@ -19,6 +19,7 @@ import java.util.Optional;
 import lombok.AccessLevel;
 import lombok.AllArgsConstructor;
 import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.internal.command.DbMigrate;
 import org.flywaydb.core.internal.sqlscript.FlywaySqlScriptException;
@@ -76,7 +77,7 @@ public class ErrorOutput implements OperationResult {
         }
 
         return new ErrorOutput(
-            ErrorCode.FAULT,
+            CoreErrorCode.FAULT,
             message == null ? "Fault occurred" : message,
             getStackTrace(exception),
             null,

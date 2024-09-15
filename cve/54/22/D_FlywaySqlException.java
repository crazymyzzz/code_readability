@@ -15,7 +15,7 @@
  */
 package org.flywaydb.core.internal.exception;
 
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.internal.util.ExceptionUtils;
 import org.flywaydb.core.internal.util.StringUtils;
@@ -28,7 +28,7 @@ import java.sql.SQLException;
 public class FlywaySqlException extends FlywayException {
 
     public FlywaySqlException(String message, SQLException sqlException) {
-        super(message, sqlException, ErrorCode.DB_CONNECTION);
+        super(message, sqlException, CoreErrorCode.DB_CONNECTION);
     }
 
     @Override

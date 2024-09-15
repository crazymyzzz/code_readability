@@ -19,6 +19,8 @@
  */
 package org.flywaydb.core.internal.resolver.java;
 
+import java.util.Collections;
+import java.util.List;
 import lombok.AccessLevel;
 import lombok.RequiredArgsConstructor;
 import org.flywaydb.core.api.FlywayException;
@@ -29,6 +31,7 @@ import org.flywaydb.core.api.migration.JavaMigration;
 import org.flywaydb.core.internal.database.DatabaseExecutionStrategy;
 import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
+import org.flywaydb.core.internal.jdbc.Results;
 import org.flywaydb.core.internal.jdbc.StatementInterceptor;
 
 import java.sql.Connection;
@@ -47,7 +50,7 @@ public class JavaMigrationExecutor implements MigrationExecutor {
     private final StatementInterceptor statementInterceptor;
 
     @Override
-    public void execute(final Context context) throws SQLException {
+    public List<Results> execute(final Context context) throws SQLException {
         if (statementInterceptor != null) {
             statementInterceptor.javaMigration(javaMigration);
         } else {
@@ -59,6 +62,8 @@ public class JavaMigrationExecutor implements MigrationExecutor {
                 return true;
             });
         }
+
+        return List.of();
     }
 
     private void executeOnce(final Context context) throws SQLException {

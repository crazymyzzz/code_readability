@@ -20,6 +20,8 @@
 package org.flywaydb.core.api.executor;
 
 import java.sql.SQLException;
+import java.util.List;
+import org.flywaydb.core.internal.jdbc.Results;
 
 /**
  * Executes a migration.
@@ -31,7 +33,7 @@ public interface MigrationExecutor {
      * @param context The context to use to execute the migration against the DB.
      * @throws SQLException when the execution of a statement failed.
      */
-    void execute(Context context) throws SQLException;
+    List<Results> execute(Context context) throws SQLException;
 
     /**
      * Whether the execution can take place inside a transaction. Almost all implementation should return {@code true}.

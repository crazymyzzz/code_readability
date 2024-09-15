@@ -19,6 +19,7 @@
  */
 package org.flywaydb.database.postgresql;
 
+import lombok.Getter;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.internal.database.base.Connection;
 import org.flywaydb.core.internal.database.base.Schema;
@@ -31,6 +32,8 @@ import java.util.concurrent.Callable;
 
 public class PostgreSQLConnection extends Connection<PostgreSQLDatabase> {
     private final String originalRole;
+    @Getter
+    private final boolean awsRds;
 
     protected PostgreSQLConnection(PostgreSQLDatabase database, java.sql.Connection connection) {
         super(database, connection);
@@ -40,6 +43,8 @@ public class PostgreSQLConnection extends Connection<PostgreSQLDatabase> {
         } catch (SQLException e) {
             throw new FlywaySqlException("Unable to determine current user", e);
         }
+
+        awsRds = rdsAdminExists();
     }
 
     @Override
@@ -99,4 +104,12 @@ public class PostgreSQLConnection extends Connection<PostgreSQLDatabase> {
     public <T> T lock(Table table, Callable<T> callable) {
         return new PostgreSQLAdvisoryLockTemplate(database.getConfiguration(), jdbcTemplate, table.toString().hashCode()).execute(callable);
     }
+
+    private boolean rdsAdminExists() {
+        try {
+            return StringUtils.hasText(jdbcTemplate.queryForString("SELECT rolname FROM pg_roles WHERE rolname ILIKE 'rds_superuser';"));
+        } catch (Exception e) {
+            return false;
+        }
+    }
 }

@@ -27,6 +27,7 @@ import org.flywaydb.core.internal.exception.FlywaySqlException;
 
 import java.sql.SQLException;
 import java.util.concurrent.Callable;
+import org.flywaydb.core.internal.util.StringUtils;
 
 /**
  * SQL Server connection.
@@ -36,6 +37,10 @@ public class SQLServerConnection extends Connection<SQLServerDatabase> {
     private final String originalAnsiNulls;
     @Getter
     private final boolean azure;
+    @Getter
+    private final boolean awsRds;
+
+    @Getter
     private final SQLServerEngineEdition engineEdition;
 
     protected SQLServerConnection(SQLServerDatabase database, java.sql.Connection connection) {
@@ -60,6 +65,8 @@ public class SQLServerConnection extends Connection<SQLServerDatabase> {
             throw new FlywaySqlException("Unable to determine database engine edition.'", e);
         }
 
+        awsRds = rdsAdminExists();
+
         try {
             originalAnsiNulls = azure ? null :
                     jdbcTemplate.queryForString("DECLARE @ANSI_NULLS VARCHAR(3) = 'OFF';\n" +
@@ -99,5 +106,11 @@ public class SQLServerConnection extends Connection<SQLServerDatabase> {
         return new SQLServerApplicationLockTemplate(this, jdbcTemplate, originalDatabaseName, table.toString().hashCode()).execute(callable);
     }
 
-    public SQLServerEngineEdition getEngineEdition() {return engineEdition;}
+    private boolean rdsAdminExists() {
+        try {
+            return StringUtils.hasText(jdbcTemplate.queryForString("SELECT name FROM sys.databases WHERE name = 'RDSAdmin'"));
+        } catch (Exception e) {
+            return false;
+        }
+    }
 }

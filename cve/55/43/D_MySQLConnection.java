@@ -20,6 +20,7 @@
 package org.flywaydb.database.mysql;
 
 import lombok.CustomLog;
+import lombok.Getter;
 import org.flywaydb.core.internal.database.base.Connection;
 import org.flywaydb.core.internal.database.base.Schema;
 import org.flywaydb.core.internal.database.base.Table;
@@ -44,6 +45,9 @@ public class MySQLConnection extends Connection<MySQLDatabase> {
     private final int originalForeignKeyChecks;
     private final int originalSqlSafeUpdates;
 
+    @Getter
+    private final boolean awsRds;
+
     public MySQLConnection(MySQLDatabase database, java.sql.Connection connection) {
         super(database, connection);
 
@@ -54,6 +58,7 @@ public class MySQLConnection extends Connection<MySQLDatabase> {
 
         originalForeignKeyChecks = getIntVariableValue(FOREIGN_KEY_CHECKS);
         originalSqlSafeUpdates = getIntVariableValue(SQL_SAFE_UPDATES);
+        awsRds = rdsAdminExists();
     }
 
     private int getIntVariableValue(String varName) {
@@ -159,4 +164,12 @@ public class MySQLConnection extends Connection<MySQLDatabase> {
     protected boolean canUseNamedLockTemplate() {
         return !database.isPxcStrict() && !database.isWsrepOn();
     }
+
+    private boolean rdsAdminExists() {
+        try {
+            return StringUtils.hasText(jdbcTemplate.queryForString("SHOW DATABASES LIKE 'RDSAdmin';"));
+        } catch (Exception e) {
+            return false;
+        }
+    }
 }

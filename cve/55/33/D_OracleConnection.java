@@ -19,14 +19,21 @@
  */
 package org.flywaydb.database.oracle;
 
+import lombok.Getter;
 import org.flywaydb.core.internal.database.base.Connection;
 import org.flywaydb.core.internal.database.base.Schema;
 
 import java.sql.SQLException;
+import org.flywaydb.core.internal.util.StringUtils;
 
 public class OracleConnection extends Connection<OracleDatabase> {
+    @Getter
+    private final boolean awsRds;
+
     OracleConnection(OracleDatabase database, java.sql.Connection connection) {
         super(database, connection);
+
+        awsRds = rdsAdminExists();
     }
 
     @Override
@@ -43,4 +50,12 @@ public class OracleConnection extends Connection<OracleDatabase> {
     public Schema getSchema(String name) {
         return new OracleSchema(jdbcTemplate, database, name);
     }
+
+    private boolean rdsAdminExists() {
+        try {
+            return StringUtils.hasText(jdbcTemplate.queryForString("SELECT username FROM all_users WHERE UPPER(username) = 'RDSADMIN'"));
+        } catch (Exception e) {
+            return false;
+        }
+    }
 }

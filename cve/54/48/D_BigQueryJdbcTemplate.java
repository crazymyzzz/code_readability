@@ -16,7 +16,9 @@
 package org.flywaydb.database.bigquery;
 
 import org.flywaydb.core.api.FlywayException;
+import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
+import org.flywaydb.core.internal.database.base.Database;
 import org.flywaydb.core.internal.jdbc.JdbcNullTypes;
 import org.flywaydb.core.internal.jdbc.JdbcTemplate;
 
@@ -27,8 +29,8 @@ import java.sql.Types;
 
 public class BigQueryJdbcTemplate extends JdbcTemplate {
 
-    public BigQueryJdbcTemplate(Connection connection) {
-        super(connection, DatabaseTypeRegister.getDatabaseTypeForConnection(connection));
+    public BigQueryJdbcTemplate(Connection connection, DatabaseType databaseType) {
+        super(connection, databaseType);
     }
 
     @Override

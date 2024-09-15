@@ -32,7 +32,7 @@ public class BigQueryConnection extends Connection<BigQueryDatabase> {
 
     BigQueryConnection(BigQueryDatabase database, java.sql.Connection connection) {
         super(database, connection);
-        this.jdbcTemplate = new BigQueryJdbcTemplate(connection);
+        this.jdbcTemplate = new BigQueryJdbcTemplate(connection, database.getDatabaseType());
     }
 
     @Override

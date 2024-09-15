@@ -25,7 +25,7 @@ import java.util.concurrent.Callable;
 public class SpannerConnection extends Connection<SpannerDatabase> {
     protected SpannerConnection(SpannerDatabase database, java.sql.Connection connection) {
         super(database, connection);
-        this.jdbcTemplate = new SpannerJdbcTemplate(connection);
+        this.jdbcTemplate = new SpannerJdbcTemplate(connection, database.getDatabaseType());
     }
 
     @Override

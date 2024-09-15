@@ -25,7 +25,7 @@ import static org.flywaydb.core.internal.logging.PreviewFeatureWarning.logPrevie
 public class MongoDBConnection extends Connection<MongoDBDatabase> {
     protected MongoDBConnection(MongoDBDatabase database, java.sql.Connection connection) {
         super(database, connection);
-        this.jdbcTemplate = new MongoDBJdbcTemplate(connection);
+        this.jdbcTemplate = new MongoDBJdbcTemplate(connection, database.getDatabaseType());
         logPreviewFeature("MongoDB support");
     }
 

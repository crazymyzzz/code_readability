@@ -21,6 +21,7 @@ import com.dbschema.mongo.resultSet.ListResultSet;
 import lombok.NonNull;
 import org.bson.Document;
 import org.flywaydb.core.api.FlywayException;
+import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
 import org.flywaydb.core.internal.jdbc.JdbcNullTypes;
 import org.flywaydb.core.internal.jdbc.JdbcTemplate;
@@ -41,10 +42,12 @@ import java.util.stream.Collectors;
 
 public class MongoDBJdbcTemplate extends JdbcTemplate {
 
-    public MongoDBJdbcTemplate(Connection connection) {
-        super(connection, DatabaseTypeRegister.getDatabaseTypeForConnection(connection));
+    public MongoDBJdbcTemplate(Connection connection, DatabaseType databaseType) {
+        super(connection, databaseType);
     }
 
+
+
     @Override
     protected PreparedStatement prepareStatement(String sql, Object[] params) throws SQLException {
         Object[] params2 = new Object[params.length];

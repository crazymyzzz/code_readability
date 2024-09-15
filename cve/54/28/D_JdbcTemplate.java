@@ -42,10 +42,6 @@ public class JdbcTemplate {
      */
     protected final int nullType;
 
-    public JdbcTemplate(Connection connection) {
-        this(connection, DatabaseTypeRegister.getDatabaseTypeForConnection(connection));
-    }
-
     public JdbcTemplate(Connection connection, DatabaseType databaseType) {
         this.connection = connection;
         this.nullType = databaseType.getNullType();

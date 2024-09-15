@@ -70,7 +70,7 @@ public class JdbcConnectionFactory implements Closeable {
         this.configuration = configuration;
 
         firstConnection = JdbcUtils.openConnection(dataSource, connectRetries, connectRetriesInterval);
-        this.databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(firstConnection);
+        this.databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(firstConnection, configuration);
 
         final DatabaseMetaData databaseMetaData = JdbcUtils.getDatabaseMetaData(firstConnection);
         this.jdbcUrl = getJdbcUrl(databaseMetaData);

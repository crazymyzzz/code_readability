@@ -16,6 +16,7 @@
 package org.flywaydb.database.spanner;
 
 import org.flywaydb.core.api.FlywayException;
+import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.database.DatabaseTypeRegister;
 import org.flywaydb.core.internal.jdbc.JdbcNullTypes;
 import org.flywaydb.core.internal.jdbc.JdbcTemplate;
@@ -24,8 +25,8 @@ import java.sql.*;
 
 public class SpannerJdbcTemplate extends JdbcTemplate {
 
-    public SpannerJdbcTemplate(Connection connection) {
-        super(connection, DatabaseTypeRegister.getDatabaseTypeForConnection(connection));
+    public SpannerJdbcTemplate(Connection connection, DatabaseType databaseType) {
+        super(connection, databaseType);
     }
 
     @Override

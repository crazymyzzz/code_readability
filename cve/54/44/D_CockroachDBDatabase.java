@@ -16,7 +16,9 @@
 package org.flywaydb.database.cockroachdb;
 
 import org.flywaydb.core.api.MigrationVersion;
+import org.flywaydb.core.api.configuration.ClassicConfiguration;
 import org.flywaydb.core.api.configuration.Configuration;
+import org.flywaydb.core.internal.database.DatabaseTypeRegister;
 import org.flywaydb.core.internal.database.base.Database;
 import org.flywaydb.core.internal.database.base.Table;
 import org.flywaydb.core.internal.exception.FlywaySqlException;
@@ -34,7 +36,7 @@ public class CockroachDBDatabase extends Database<CockroachDBConnection> {
 
     public CockroachDBDatabase(Configuration configuration, JdbcConnectionFactory jdbcConnectionFactory, StatementInterceptor statementInterceptor) {
         super(configuration, jdbcConnectionFactory, statementInterceptor);
-        this.determinedVersion = rawDetermineVersion();
+        this.determinedVersion = rawDetermineVersion(configuration);
     }
 
     @Override
@@ -66,11 +68,11 @@ public class CockroachDBDatabase extends Database<CockroachDBConnection> {
                 "CREATE INDEX IF NOT EXISTS \"" + table.getName() + "_s_idx\" ON " + table + " (\"success\");";
     }
 
-    private MigrationVersion rawDetermineVersion() {
+    private MigrationVersion rawDetermineVersion(Configuration configuration) {
         String version;
         try {
             // Use rawMainJdbcConnection to avoid infinite recursion.
-            JdbcTemplate template = new JdbcTemplate(rawMainJdbcConnection);
+            JdbcTemplate template = new JdbcTemplate(rawMainJdbcConnection, DatabaseTypeRegister.getDatabaseTypeForConnection(rawMainJdbcConnection, configuration));
             version = template.queryForString("SELECT value FROM crdb_internal.node_build_info where field='Version'");
             if (version == null) {
                 version = template.queryForString("SELECT value FROM crdb_internal.node_build_info where field='Tag'");

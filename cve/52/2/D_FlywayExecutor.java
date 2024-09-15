@@ -29,6 +29,7 @@ import org.flywaydb.core.internal.callback.*;
 import org.flywaydb.core.internal.clazz.NoopClassProvider;
 import org.flywaydb.core.internal.configuration.ConfigurationValidator;
 import org.flywaydb.core.internal.database.DatabaseType;
+import org.flywaydb.core.internal.database.base.CommunityDatabaseType;
 import org.flywaydb.core.internal.database.base.Database;
 import org.flywaydb.core.internal.database.base.Schema;
 import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
@@ -159,6 +160,10 @@ public class FlywayExecutor {
             if (!dbConnectionInfoPrinted) {
                 dbConnectionInfoPrinted = true;
 
+                if (database.getDatabaseType() instanceof CommunityDatabaseType) {
+                    LOG.info(((CommunityDatabaseType) database.getDatabaseType()).announcementForCommunitySupport());
+                }
+
                 LOG.info("Database: " + redactJdbcUrl(jdbcConnectionFactory.getJdbcUrl()) + " (" + jdbcConnectionFactory.getProductName() + ")");
                 LOG.debug("Driver: " + jdbcConnectionFactory.getDriverInfo());
 

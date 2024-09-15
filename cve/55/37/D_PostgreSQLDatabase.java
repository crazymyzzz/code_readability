@@ -19,6 +19,8 @@
  */
 package org.flywaydb.database.postgresql;
 
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AWS_RDS;
+
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.extensibility.Tier;
 import org.flywaydb.core.internal.database.base.Database;
@@ -134,4 +136,13 @@ public class PostgreSQLDatabase extends Database<PostgreSQLConnection> {
                 + " WHERE " + quote("installed_rank") + " > ?"
                 + " ORDER BY " + quote("installed_rank");
     }
+
+    @Override
+    public String getDatabaseHosting() {
+        if (getMainConnection().isAwsRds()) {
+            return DATABASE_HOSTING_AWS_RDS;
+        } else {
+            return super.getDatabaseHosting();
+        }
+    }
 }

@@ -19,6 +19,8 @@
  */
 package org.flywaydb.database.oracle;
 
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AWS_RDS;
+
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.extensibility.Tier;
 import org.flywaydb.core.internal.database.base.Database;
@@ -323,4 +325,13 @@ public class OracleDatabase extends Database<OracleConnection> {
 
         return result;
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

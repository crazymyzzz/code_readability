@@ -19,6 +19,8 @@
  */
 package org.flywaydb.database.mysql;
 
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AWS_RDS;
+
 import lombok.CustomLog;
 import org.flywaydb.core.api.CoreMigrationType;
 import org.flywaydb.core.api.FlywayException;
@@ -301,4 +303,13 @@ public class MySQLDatabase extends Database<MySQLConnection> {
     public boolean useSingleConnection() {
         return !pxcStrict;
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

@@ -381,6 +381,8 @@ public class SQLServerDatabase extends Database<SQLServerConnection> {
             } else if (code == 8) {
                 return DATABASE_HOSTING_AZURE_SQL_MANAGED_INSTANCE;
             }
+        } else if (getMainConnection().isAwsRds()) {
+            return DATABASE_HOSTING_AWS_RDS;
         } else {
             return super.getDatabaseHosting();
         }

@@ -36,6 +36,8 @@ import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.Arrays;
 
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AWS_RDS;
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_GOOGLE_BIGQUERY;
 import static org.flywaydb.core.internal.util.DataUnits.GIGABYTE;
 
 /**
@@ -179,4 +181,9 @@ public class BigQueryDatabase extends Database<BigQueryConnection> {
     public boolean useSingleConnection() {
         return true;
     }
+
+    @Override
+    public String getDatabaseHosting() {
+        return DATABASE_HOSTING_GOOGLE_BIGQUERY;
+    }
 }

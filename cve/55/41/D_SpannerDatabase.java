@@ -35,6 +35,7 @@ import java.sql.Connection;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_GOOGLE_SPANNER;
 import static org.flywaydb.core.internal.util.DataUnits.GIGABYTE;
 
 @CustomLog
@@ -163,4 +164,9 @@ public class SpannerDatabase extends Database<SpannerConnection> {
                 + ")"
                 + " VALUES (?, ?, ?, ?, ?, ?, ?, PENDING_COMMIT_TIMESTAMP(), ?, ?)";
     }
+
+    @Override
+    public String getDatabaseHosting() {
+        return DATABASE_HOSTING_GOOGLE_SPANNER;
+    }
 }

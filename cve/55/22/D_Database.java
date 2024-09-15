@@ -46,9 +46,13 @@ import java.sql.DatabaseMetaData;
 import java.sql.SQLException;
 import java.util.List;
 
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AWS_VM;
 import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AZURE_URL_IDENTIFIER;
 import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AZURE_VM;
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_GCP_URL_IDENTIFIER;
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_GCP_VM;
 import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_LOCAL;
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_RDS_URL_IDENTIFIER;
 import static org.flywaydb.core.internal.util.FlywayDbWebsiteLinks.COMMUNITY_SUPPORT;
 
 /**
@@ -499,8 +503,14 @@ public abstract class Database<C extends Connection> implements Closeable {
     }
 
     public String getDatabaseHosting() {
-        if (DATABASE_HOSTING_AZURE_URL_IDENTIFIER.matcher(configuration.getUrl()).find()) {
+        String url = configuration.getUrl();
+
+        if (DATABASE_HOSTING_AZURE_URL_IDENTIFIER.matcher(url).find()) {
             return DATABASE_HOSTING_AZURE_VM;
+        } else if (DATABASE_HOSTING_RDS_URL_IDENTIFIER.matcher(url).find()) {
+            return DATABASE_HOSTING_AWS_VM;
+        } else if (url.contains(DATABASE_HOSTING_GCP_URL_IDENTIFIER)) {
+            return DATABASE_HOSTING_GCP_VM;
         }
 
         return DATABASE_HOSTING_LOCAL;

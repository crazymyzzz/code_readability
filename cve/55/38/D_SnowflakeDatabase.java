@@ -19,7 +19,9 @@
  */
 package org.flywaydb.database.snowflake;
 
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AWS_SNOWFLAKE;
 import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_AZURE_SNOWFLAKE;
+import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_GCP_SNOWFLAKE;
 import static org.flywaydb.core.internal.database.base.DatabaseConstants.DATABASE_HOSTING_LOCAL;
 
 import lombok.CustomLog;
@@ -159,10 +161,14 @@ public class SnowflakeDatabase extends Database<SnowflakeConnection> {
 
     @Override
     public String getDatabaseHosting() {
-        if (configuration.getUrl().contains("azure.snowflakecomputing.com")) {
+        String url = configuration.getUrl();
+
+        if (url.contains("azure.snowflakecomputing.com")) {
             return DATABASE_HOSTING_AZURE_SNOWFLAKE;
+        } else if (url.contains("gcp.snowflakecomputing.com")) {
+            return DATABASE_HOSTING_GCP_SNOWFLAKE;
+        } else {
+            return DATABASE_HOSTING_AWS_SNOWFLAKE;
         }
-
-        return DATABASE_HOSTING_LOCAL;
     }
 }

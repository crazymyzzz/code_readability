@@ -15,6 +15,7 @@
  */
 package org.flywaydb.database.bigquery;
 
+import java.util.List;
 import lombok.CustomLog;
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
@@ -45,7 +46,7 @@ public class BigQueryDatabaseType extends BaseDatabaseType {
 
     @Override
     public String getName() {
-        return "BigQuery";
+        return "Google Big Query";
     }
 
     @Override

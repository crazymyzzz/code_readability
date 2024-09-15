@@ -15,6 +15,7 @@
  */
 package org.flywaydb.database.postgresql;
 
+import java.util.List;
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.extensibility.Tier;
@@ -45,6 +46,11 @@ public class PostgreSQLDatabaseType extends BaseDatabaseType {
         return "PostgreSQL";
     }
 
+    @Override
+    public List<String> getSupportedEngines() {
+        return List.of(getName(), "AuroraPostgreSql", "YugabyteDb", "TimescaleDb");
+    }
+
     @Override
     public int getNullType() {
         return Types.NULL;

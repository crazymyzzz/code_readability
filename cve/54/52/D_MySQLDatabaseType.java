@@ -15,6 +15,7 @@
  */
 package org.flywaydb.database.mysql;
 
+import java.util.List;
 import lombok.CustomLog;
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
@@ -50,6 +51,12 @@ public class MySQLDatabaseType extends BaseDatabaseType {
         return "MySQL";
     }
 
+    @Override
+    public List<String> getSupportedEngines() {
+        //TODO - move TiDB to community plugin once this API is public
+        return List.of(getName(), "PerconaXtraDbCluster", "TiDb", "AuroraMySql");
+    }
+
     @Override
     public int getNullType() {
         return Types.VARCHAR;

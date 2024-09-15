@@ -15,6 +15,7 @@
  */
 package org.flywaydb.database.singlestore;
 
+import java.util.List;
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.internal.database.base.BaseDatabaseType;
@@ -35,6 +36,11 @@ public class SingleStoreDatabaseType extends BaseDatabaseType {
         return "SingleStoreDB";
     }
 
+    @Override
+    public List<String> getSupportedEngines() {
+        return List.of("singlestore");
+    }
+
     @Override
     public int getNullType() {
         return Types.VARCHAR;

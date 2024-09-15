@@ -15,6 +15,7 @@
  */
 package org.flywaydb.database.spanner;
 
+import java.util.List;
 import lombok.CustomLog;
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
@@ -41,6 +42,11 @@ public class SpannerDatabaseType extends BaseDatabaseType {
         return "Google Cloud Spanner";
     }
 
+    @Override
+    public List<String> getSupportedEngines() {
+        return List.of("CloudSpanner");
+    }
+
     @Override
     public int getNullType() {
         return Types.NULL;

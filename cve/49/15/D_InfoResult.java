@@ -15,12 +15,16 @@
  */
 package org.flywaydb.core.api.output;
 
+import java.time.LocalDateTime;
 import java.util.List;
 
-public class InfoResult extends OperationResultBase {
+public class InfoResult extends HtmlResult {
+    private static final String COMMAND = "info";
     public String schemaVersion;
     public String schemaName;
     public List<InfoOutput> migrations;
+    public String flywayVersion;
+    public String database;
     public boolean allSchemasEmpty;
 
     public InfoResult(String flywayVersion,
@@ -29,12 +33,12 @@ public class InfoResult extends OperationResultBase {
                       String schemaName,
                       List<InfoOutput> migrations,
                       boolean allSchemasEmpty) {
+        super(LocalDateTime.now(), COMMAND);
         this.flywayVersion = flywayVersion;
         this.database = database;
         this.schemaVersion = schemaVersion;
         this.schemaName = schemaName;
         this.migrations = migrations;
-        this.operation = "info";
         this.allSchemasEmpty = allSchemasEmpty;
     }
 }
\ No newline at end of file

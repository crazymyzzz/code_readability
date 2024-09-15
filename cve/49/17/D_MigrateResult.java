@@ -15,38 +15,51 @@
  */
 package org.flywaydb.core.api.output;
 
+import java.time.LocalDateTime;
 import java.util.ArrayList;
 import java.util.List;
 
-public class MigrateResult extends OperationResultBase {
+public class MigrateResult extends HtmlResult {
+    public static final String COMMAND = "migrate";
     public String initialSchemaVersion;
     public String targetSchemaVersion;
     public String schemaName;
     public List<MigrateOutput> migrations;
     public int migrationsExecuted;
     public boolean success;
+    public String flywayVersion;
+    public String database;
+    public List<String> warnings = new ArrayList<>();
 
     public MigrateResult(String flywayVersion,
                          String database,
                          String schemaName) {
+        super(LocalDateTime.now(), COMMAND);
         this.flywayVersion = flywayVersion;
         this.database = database;
         this.schemaName = schemaName;
         this.migrations = new ArrayList<>();
-        this.operation = "migrate";
         this.success = true;
     }
 
     MigrateResult(MigrateResult migrateResult) {
+        super(migrateResult.getTimestamp(), migrateResult.getOperation());
         this.flywayVersion = migrateResult.flywayVersion;
         this.database = migrateResult.database;
         this.schemaName = migrateResult.schemaName;
         this.migrations = migrateResult.migrations;
-        this.operation = migrateResult.operation;
         this.success = migrateResult.success;
         this.migrationsExecuted = migrateResult.migrationsExecuted;
         this.initialSchemaVersion = migrateResult.initialSchemaVersion;
         this.targetSchemaVersion = migrateResult.targetSchemaVersion;
         this.warnings = migrateResult.warnings;
     }
+
+    public void addWarning(String warning) {
+        if (warnings == null) {
+            warnings = new ArrayList<>();
+        }
+
+        warnings.add(warning);
+    }
 }
\ No newline at end of file

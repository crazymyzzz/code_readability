@@ -19,7 +19,9 @@
  */
 package org.flywaydb.core.internal.sqlscript;
 
+import java.util.List;
 import org.flywaydb.core.api.configuration.Configuration;
+import org.flywaydb.core.internal.jdbc.Results;
 
 /**
  * Executor for SQL scripts.
@@ -30,5 +32,5 @@ public interface SqlScriptExecutor {
      *
      * @param sqlScript The SQL script.
      */
-    void execute(SqlScript sqlScript, Configuration config);
+    List<Results> execute(SqlScript sqlScript, Configuration config);
 }

@@ -15,6 +15,8 @@
  */
 package org.flywaydb.database.oracle;
 
+import static org.flywaydb.core.internal.util.FlywayDbWebsiteLinks.ORACLE_DATABASE;
+
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.api.resource.Resource;
@@ -688,4 +690,9 @@ public class OracleParser extends Parser {
 
 
 
+
+    @Override
+    protected String getAdditionalParsingErrorInfo() {
+        return "For Oracle-specific information about syntax and limitations, see " + ORACLE_DATABASE + ". ";
+    }
 }
\ No newline at end of file

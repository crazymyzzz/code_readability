@@ -83,8 +83,8 @@ public class PostgreSQLDatabase extends Database<PostgreSQLConnection> {
                 "    \"success\" BOOLEAN NOT NULL\n" +
                 ")" + tablespace + ";\n" +
                 (baseline ? getBaselineStatement(table) + ";\n" : "") +
-                "ALTER TABLE " + table + " ADD CONSTRAINT \"" + table.getName() + "_pk\" PRIMARY KEY (\"installed_rank\");\n" +
-                "CREATE INDEX \"" + table.getName() + "_s_idx\" ON " + table + " (\"success\");";
+                "ALTER TABLE " + table + " ADD CONSTRAINT \"" + table.getName() + "_pk\" PRIMARY KEY (\"installed_rank\")" + (configuration.getTablespace() != null ? " USING INDEX" + tablespace : "" ) + ";\n" +
+                "CREATE INDEX \"" + table.getName() + "_s_idx\" ON " + table + " (\"success\")" + tablespace + ";";
     }
 
     @Override

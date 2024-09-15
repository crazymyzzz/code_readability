@@ -295,7 +295,7 @@ public class PostgreSQLSchema extends Schema<PostgreSQLDatabase, PostgreSQLTable
 
         List<String> statements = new ArrayList<>();
         for (String domainName : domainNames) {
-            statements.add("DROP DOMAIN " + database.quote(name, domainName));
+            statements.add("DROP DOMAIN IF EXISTS " + database.quote(name, domainName) + " CASCADE");
         }
 
         return statements;

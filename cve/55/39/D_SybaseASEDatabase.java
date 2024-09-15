@@ -167,11 +167,11 @@ public class SybaseASEDatabase extends Database<SybaseASEConnection> {
             String getDatabaseMetadataQuery = "sp_helpdb " + databaseName + " -- ";
             Results results = getMainConnection().getJdbcTemplate().executeStatement(getDatabaseMetadataQuery);
             for (int resultsIndex = 0; resultsIndex < results.getResults().size(); resultsIndex++) {
-                List<String> columns = results.getResults().get(resultsIndex).getColumns();
+                List<String> columns = results.getResults().get(resultsIndex).columns();
                 if (columns != null) {
                     int statusIndex = getStatusIndex(columns);
                     if (statusIndex > -1) {
-                        String options = results.getResults().get(resultsIndex).getData().get(0).get(statusIndex);
+                        String options = results.getResults().get(resultsIndex).data().get(0).get(statusIndex);
                         return options.contains("ddl in tran");
                     }
                 }

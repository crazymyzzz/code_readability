@@ -128,16 +128,6 @@ public class Main {
                 MavenVersionChecker.checkForVersionUpdates();
             }
 
-            if (configuration.getDataSource() != null) {
-                try (JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(configuration.getDataSource(), configuration, null);
-                     Database database = jdbcConnectionFactory.getDatabaseType().createDatabase(configuration, false, jdbcConnectionFactory, null)) {
-
-//                    [master-only]
-//                    Telemetry to replace
-//                    [/master-only]
-                }
-            }
-
             OperationResult result;
             if (commandLineArguments.getOperations().size() == 1) {
                 String operation = commandLineArguments.getOperations().get(0);

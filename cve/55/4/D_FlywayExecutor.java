@@ -194,8 +194,8 @@ public class FlywayExecutor {
 
             database.ensureSupported(configuration);
 
-            DefaultCallbackExecutor callbackExecutor = new DefaultCallbackExecutor(configuration, database, defaultSchema, prepareCallbacks(
-                    database, resourceProvider, jdbcConnectionFactory, sqlScriptFactory, statementInterceptor, defaultSchema, parsingContext));
+            DefaultCallbackExecutor callbackExecutor = new DefaultCallbackExecutor(configuration, database, defaultSchema, flywayTelemetryManager, prepareCallbacks(
+                    database, resourceProvider, jdbcConnectionFactory, sqlScriptFactory, statementInterceptor, defaultSchema, parsingContext, flywayTelemetryManager));
 
             SqlScriptExecutorFactory sqlScriptExecutorFactory = databaseType.createSqlScriptExecutorFactory(jdbcConnectionFactory, callbackExecutor, statementInterceptor);
 
@@ -274,7 +274,7 @@ public class FlywayExecutor {
     private List<Callback> prepareCallbacks(Database database, ResourceProvider resourceProvider,
                                             JdbcConnectionFactory jdbcConnectionFactory,
                                             SqlScriptFactory sqlScriptFactory, StatementInterceptor statementInterceptor,
-                                            Schema schema, ParsingContext parsingContext) {
+                                            Schema schema, ParsingContext parsingContext, FlywayTelemetryManager flywayTelemetryManager) {
         List<Callback> effectiveCallbacks = new ArrayList<>();
         CallbackExecutor callbackExecutor = NoopCallbackExecutor.INSTANCE;
 

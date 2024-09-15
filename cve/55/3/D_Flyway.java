@@ -245,7 +245,7 @@ public class Flyway {
         if (isExperimentalModeActivated() && canUseExperimentalMode(configuration)) {
             final var verb = configuration.getPluginRegister().getPlugins(VerbExtension.class).stream().filter(verbExtension -> verbExtension.handlesVerb("info")).findFirst();
             if (verb.isPresent()) {
-                return (MigrationInfoService) verb.get().executeVerb();
+                return (MigrationInfoService) verb.get().executeVerb(configuration);
             }
         }
         return flywayExecutor.execute((migrationResolver, schemaHistory, database, defaultSchema, schemas, callbackExecutor, statementInterceptor) -> {

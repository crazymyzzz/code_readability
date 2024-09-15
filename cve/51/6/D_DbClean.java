@@ -63,7 +63,7 @@ public class DbClean {
 
         CleanResult cleanResult;
 
-        if (cleanMode == null || Mode.DEFAULT.name().equals(cleanMode) || !StringUtils.hasText(cleanMode)) {
+        if (cleanMode == null || Mode.DEFAULT.name().equalsIgnoreCase(cleanMode) || !StringUtils.hasText(cleanMode)) {
             cleanResult = CommandResultFactory.createCleanResult(database.getCatalog());
             new CleanExecutor(connection, database, schemaHistory, callbackExecutor).clean(defaultSchema, schemas, cleanResult);
         } else {

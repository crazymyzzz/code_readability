@@ -92,7 +92,8 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
     }
 
     @Override
-    public void execute(SqlScript sqlScript, Configuration config) {
+    public List<Results> execute(SqlScript sqlScript, Configuration config) {
+        final List<Results> results = new ArrayList<>(List.of());
 
 
 
@@ -122,26 +123,27 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
                         logStatementExecution(sqlStatement);
                         batchStatements.add(sqlStatement);
                         if (batchStatements.size() >= MAX_BATCH_SIZE) {
-                            executeBatch(jdbcTemplate, sqlScript, batchStatements, config);
+                            results.add(executeBatch(jdbcTemplate, sqlScript, batchStatements, config));
                             batchStatements = new ArrayList<>();
                         }
                     } else {
                         // Execute the batch up to this point
-                        executeBatch(jdbcTemplate, sqlScript, batchStatements, config);
+                        results.add(executeBatch(jdbcTemplate, sqlScript, batchStatements, config));
                         batchStatements = new ArrayList<>();
                         // Now execute this non-batchable statement. We'll resume batching after this one.
-                        executeStatement(jdbcTemplate, sqlScript, sqlStatement, config);
+                        results.add(executeStatement(jdbcTemplate, sqlScript, sqlStatement, config));
                     }
                 } else {
-                    executeStatement(jdbcTemplate, sqlScript, sqlStatement, config);
+                    results.add(executeStatement(jdbcTemplate, sqlScript, sqlStatement, config));
                 }
             }
         }
 
         if (batch) {
             // Execute any remaining batch statements that haven't yet been sent to the database
-            executeBatch(jdbcTemplate, sqlScript, batchStatements, config);
+            results.add(executeBatch(jdbcTemplate, sqlScript, batchStatements, config));
         }
+        return results;
     }
 
     protected void logStatementExecution(SqlStatement sqlStatement) {
@@ -152,9 +154,9 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
         }
     }
 
-    private void executeBatch(JdbcTemplate jdbcTemplate, SqlScript sqlScript, List<SqlStatement> batchStatements, Configuration config) {
+    private Results executeBatch(JdbcTemplate jdbcTemplate, SqlScript sqlScript, List<SqlStatement> batchStatements, Configuration config) {
         if (batchStatements.isEmpty()) {
-            return;
+            return null;
         }
 
         LOG.debug("Sending batch of " + batchStatements.size() + " statements to database ...");
@@ -176,7 +178,7 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
 
             for (int i = 0; i < results.getResults().size(); i++) {
                 SqlStatement sqlStatement = batchStatements.get(i);
-                long updateCount = results.getResults().get(i).getUpdateCount();
+                long updateCount = results.getResults().get(i).updateCount();
                 if (updateCount == Statement.EXECUTE_FAILED) {
                     handleEachMigrateOrUndoStatementCallback(Event.AFTER_EACH_UNDO_STATEMENT_ERROR, Event.AFTER_EACH_MIGRATE_STATEMENT_ERROR, sqlStatement.getSql() + sqlStatement.getDelimiter(), results.getWarnings(), results.getErrors());
                     handleException(results, sqlScript, batchStatements.get(i), config);
@@ -185,7 +187,7 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
                     handleUpdateCount(updateCount);
                 }
             }
-            return;
+            return results;
         }
 
         for (int i = 0; i < results.getResults().size(); i++) {
@@ -193,9 +195,10 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
             handleEachMigrateOrUndoStatementCallback(Event.AFTER_EACH_UNDO_STATEMENT, Event.AFTER_EACH_MIGRATE_STATEMENT, sqlStatement.getSql() + sqlStatement.getDelimiter(), results.getWarnings(), results.getErrors());
         }
         handleResults(results);
+        return results;
     }
 
-    protected void executeStatement(JdbcTemplate jdbcTemplate, SqlScript sqlScript, SqlStatement sqlStatement, Configuration config) {
+    protected Results executeStatement(JdbcTemplate jdbcTemplate, SqlScript sqlScript, SqlStatement sqlStatement, Configuration config) {
         logStatementExecution(sqlStatement);
         String sql = sqlStatement.getSql() + sqlStatement.getDelimiter();
 
@@ -203,7 +206,7 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
             handleEachMigrateOrUndoStatementCallback(Event.BEFORE_EACH_UNDO_STATEMENT, Event.BEFORE_EACH_MIGRATE_STATEMENT, sql, null, null);
         } catch (FlywayBlockStatementExecutionException e) {
             LOG.debug("Statement on line " + sqlStatement.getLineNumber() + " + skipped due to " + e.getMessage());
-            return;
+            return null;
         }
 
         Results results = sqlStatement.execute(jdbcTemplate, this, config);
@@ -212,17 +215,18 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
             handleEachMigrateOrUndoStatementCallback(Event.AFTER_EACH_UNDO_STATEMENT_ERROR, Event.AFTER_EACH_MIGRATE_STATEMENT_ERROR, sql, results.getWarnings(), results.getErrors());
             printWarnings(results);
             handleException(results, sqlScript, sqlStatement, config);
-            return;
+            return null;
         }
 
         handleEachMigrateOrUndoStatementCallback(Event.AFTER_EACH_UNDO_STATEMENT, Event.AFTER_EACH_MIGRATE_STATEMENT, sql, results.getWarnings(), results.getErrors());
         printWarnings(results);
         handleResults(results);
+        return results;
     }
 
     protected void handleResults(Results results) {
         for (Result result : results.getResults()) {
-            long updateCount = result.getUpdateCount();
+            long updateCount = result.updateCount();
             if (updateCount != -1) {
                 handleUpdateCount(updateCount);
             }
@@ -234,8 +238,8 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
 
     protected void outputQueryResult(Result result) {
         if (outputQueryResults &&
-                result.getColumns() != null && !result.getColumns().isEmpty()) {
-            LOG.info(new AsciiTable(result.getColumns(), result.getData(),
+                result.columns() != null && !result.columns().isEmpty()) {
+            LOG.info(new AsciiTable(result.columns(), result.data(),
                 true, "", "No rows returned").render());
         }
     }

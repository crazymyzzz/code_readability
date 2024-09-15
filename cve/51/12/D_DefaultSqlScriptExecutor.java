@@ -229,18 +229,10 @@ public class DefaultSqlScriptExecutor implements SqlScriptExecutor {
 
 
         printWarnings(results);
-        handleResults(results
-
-
-
-                     );
+        handleResults(results);
     }
 
-    protected void handleResults(Results results
-
-
-
-                                ) {
+    protected void handleResults(Results results) {
         for (Result result : results.getResults()) {
             long updateCount = result.getUpdateCount();
             if (updateCount != -1) {

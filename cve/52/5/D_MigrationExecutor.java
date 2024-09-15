@@ -44,4 +44,13 @@ public interface MigrationExecutor {
      * @return {@code true} if the migration should be executed, or {@code false} if not.
      */
     boolean shouldExecute();
+
+    /**
+     *  Optionally, an executor may provide an expression string describing the conditions under which {@code shouldExecute}
+     *  may return {@code true}. The {@code shouldExecute} function itself is the source of truth for whether a script
+     *  may execute, this function is provided for providing additional information rather than direct evaluation.
+     *
+     * @return A string expression for the shouldExecute function, or {@code null}
+     */
+    default String shouldExecuteExpression() { return null; }
 }
\ No newline at end of file

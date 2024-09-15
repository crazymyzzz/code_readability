@@ -59,6 +59,15 @@ public interface SqlScript extends Comparable<SqlScript> {
      */
     boolean shouldExecute();
 
+    /**
+     * Optionally, a script may provide the expression string describing the conditions under which {@code shouldExecute}
+     * returns true. The {@code shouldExecute} function itself is the source of truth for whether a script may execute.
+     * This function is provided for providing additional information rather than direct evaluation.
+     *
+     * @return A string expression for the shouldExecute function, or {@code null}
+     */
+    default String shouldExecuteExpression() { return null; }
+
     /**
      * Validates this SQL script.
      */

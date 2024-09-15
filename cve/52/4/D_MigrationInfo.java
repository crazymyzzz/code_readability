@@ -87,4 +87,9 @@ public interface MigrationInfo extends Comparable<MigrationInfo> {
      * @return The result between a comparison of these MigrationInfo's versions.
      */
     int compareVersion(MigrationInfo o);
+
+    /**
+     * @return The shouldExecute expression if present and supported by the migration type. Otherwise {@code null}.
+     */
+    default String getShouldExecuteExpression() { return null; }
 }
\ No newline at end of file

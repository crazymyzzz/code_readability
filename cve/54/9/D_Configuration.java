@@ -450,6 +450,15 @@ public interface Configuration {
      */
     boolean isCleanDisabled();
 
+
+    /**
+     * Whether to disable community database support.
+     * This is especially useful for production environments where using community databases is undesirable.
+     *
+     * @return {@code true} to disable community database support. {@code false} to be able to use community database support. (default: {@code false})
+     */
+    boolean isCommunityDBSupportEnabled();
+
     /**
      * Whether to allow mixing transactional and non-transactional statements within the same migration. Enabling this
      * automatically causes the entire affected migration to be run without a transaction.

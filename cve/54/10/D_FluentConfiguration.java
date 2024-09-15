@@ -291,6 +291,17 @@ public class FluentConfiguration implements Configuration {
         return this;
     }
 
+    /**
+     * Whether to disable community database support.
+     * This is especially useful for production environments where using community databases is undesirable.
+     *
+     * @param communityDBSupportEnabled {@code true} to disable community database support. {@code false} to be able to use community database support. (default: {@code false})
+     */
+    public FluentConfiguration communityDBSupportEnabled(boolean communityDBSupportEnabled) {
+        config.setCommunityDBSupportEnabled(communityDBSupportEnabled);
+        return this;
+    }
+
     /**
      * Sets the locations to scan recursively for migrations.
      * The location type is determined by its prefix.

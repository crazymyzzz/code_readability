@@ -182,7 +182,7 @@ public class DbMigrate {
             if (configuration.isOutOfOrder()) {
                 String outOfOrderWarning = "outOfOrder mode is active. Migration of schema " + schema + " may not be reproducible.";
                 LOG.warn(outOfOrderWarning);
-                migrateResult.warnings.add(outOfOrderWarning);
+                migrateResult.addWarning(outOfOrderWarning);
             }
         }
 

@@ -83,7 +83,7 @@ public class SnowflakeDatabase extends Database<SnowflakeConnection> {
 
         ensureDatabaseNotOlderThanOtherwiseRecommendUpgradeToFlywayEdition("3", org.flywaydb.core.internal.license.Edition.ENTERPRISE);
 
-        recommendFlywayUpgradeIfNecessaryForMajorVersion("7.1");
+        recommendFlywayUpgradeIfNecessaryForMajorVersion("7.33");
     }
 
     @Override

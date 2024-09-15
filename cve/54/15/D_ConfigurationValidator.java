@@ -16,7 +16,7 @@
 package org.flywaydb.core.internal.configuration;
 
 import java.io.File;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.configuration.Configuration;
 
@@ -29,7 +29,7 @@ public class ConfigurationValidator {
             throw new FlywayException("flyway.batch configuration option is incompatible with flyway.errorOverrides.\n" +
                                               "It is impossible to intercept the errors in a batch process.\n" +
                                               "Set flyway.batch to false, or remove the error overrides.",
-                                      ErrorCode.CONFIGURATION);
+                                      CoreErrorCode.CONFIGURATION);
         }
 
         if (configuration.getDataSource() == null) {
@@ -38,7 +38,7 @@ public class ConfigurationValidator {
                 errorMessage += " Refer to the flyway.toml.example file in the /conf folder in the installation directory.";
             }
 
-            throw new FlywayException(errorMessage, ErrorCode.CONFIGURATION);
+            throw new FlywayException(errorMessage, CoreErrorCode.CONFIGURATION);
         }
 
         for (String key : configuration.getPlaceholders().keySet()) {

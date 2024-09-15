@@ -18,7 +18,7 @@ package org.flywaydb.core;
 import lombok.CustomLog;
 import lombok.Setter;
 import lombok.SneakyThrows;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.MigrationInfoService;
 import org.flywaydb.core.api.callback.Event;
@@ -191,7 +191,7 @@ public class Flyway {
                                     throw new FlywayException("Found non-empty schema(s) "
                                                                       + StringUtils.collectionToCommaDelimitedString(nonEmptySchemas)
                                                                       + " but no schema history table. Use baseline()"
-                                                                      + " or set baselineOnMigrate to true to initialize the schema history table.", ErrorCode.NON_EMPTY_SCHEMA_WITHOUT_SCHEMA_HISTORY_TABLE);
+                                                                      + " or set baselineOnMigrate to true to initialize the schema history table.", CoreErrorCode.NON_EMPTY_SCHEMA_WITHOUT_SCHEMA_HISTORY_TABLE);
                                 }
                             }
                         }

@@ -16,7 +16,7 @@
 package org.flywaydb.core.internal.resolver;
 
 import org.flywaydb.core.api.ClassProvider;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
@@ -35,8 +35,6 @@ import org.flywaydb.core.internal.sqlscript.SqlScriptFactory;
 
 
 
-
-
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collection;
@@ -94,14 +92,14 @@ public class CompositeMigrationResolver implements MigrationResolver {
                                                             current.getPhysicalLocation(),
                                                             current.getType(),
                                                             next.getPhysicalLocation(),
-                                                            next.getType()), ErrorCode.DUPLICATE_VERSIONED_MIGRATION);
+                                                            next.getType()), CoreErrorCode.DUPLICATE_VERSIONED_MIGRATION);
                 }
                 throw new FlywayException(String.format("Found more than one repeatable migration with description %s\nOffenders:\n-> %s (%s)\n-> %s (%s)",
                                                         current.getDescription(),
                                                         current.getPhysicalLocation(),
                                                         current.getType(),
                                                         next.getPhysicalLocation(),
-                                                        next.getType()), ErrorCode.DUPLICATE_REPEATABLE_MIGRATION);
+                                                        next.getType()), CoreErrorCode.DUPLICATE_REPEATABLE_MIGRATION);
             }
         }
     }

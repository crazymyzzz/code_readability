@@ -15,12 +15,13 @@
  */
 package org.flywaydb.core.internal.database.base;
 
+import java.util.List;
+import java.util.Locale;
 import lombok.CustomLog;
 import org.flywaydb.core.api.ResourceProvider;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.internal.callback.CallbackExecutor;
 import org.flywaydb.core.internal.database.DatabaseExecutionStrategy;
-import org.flywaydb.core.internal.database.DatabaseTypeRegister;
 import org.flywaydb.core.internal.database.DefaultExecutionStrategy;
 import org.flywaydb.core.internal.database.DatabaseType;
 import org.flywaydb.core.internal.jdbc.*;
@@ -36,6 +37,7 @@ import java.sql.*;
 import java.util.Map;
 import java.util.Properties;
 import java.util.regex.Pattern;
+import org.flywaydb.core.internal.util.StringUtils;
 
 import static org.flywaydb.core.internal.database.DatabaseTypeRegister.redactJdbcUrl;
 import static org.flywaydb.core.internal.sqlscript.SqlScriptMetadata.getMetadataResource;
@@ -56,6 +58,15 @@ public abstract class BaseDatabaseType implements DatabaseType {
      */
     public abstract String getName();
 
+    /**
+     * @return The list of engine names and their aliases for this database. This corresponds to the optional database
+     * type property in the root of the flyway toml file.
+     */
+    @Override
+    public List<String> getSupportedEngines() {
+        return List.of(getName().replaceAll("\\s", ""));
+    }
+
     @Override
     public String toString() {
         return getName();

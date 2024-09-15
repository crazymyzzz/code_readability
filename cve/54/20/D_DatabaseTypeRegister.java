@@ -17,13 +17,14 @@ package org.flywaydb.core.internal.database;
 
 import lombok.CustomLog;
 import org.flywaydb.core.api.FlywayException;
+import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.internal.database.base.BaseDatabaseType;
+import org.flywaydb.core.internal.database.base.CommunityDatabaseType;
 import org.flywaydb.core.internal.jdbc.JdbcUtils;
 import org.flywaydb.core.internal.plugin.PluginRegister;
 
 import java.sql.Connection;
 import java.sql.DatabaseMetaData;
-import java.util.ArrayList;
 import java.util.List;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
@@ -34,8 +35,8 @@ public class DatabaseTypeRegister {
 
     private static final List<DatabaseType> SORTED_DATABASE_TYPES = new PluginRegister().getPlugins(DatabaseType.class).stream().sorted().collect(Collectors.toList());
 
-    public static DatabaseType getDatabaseTypeForUrl(String url) {
-        List<DatabaseType> typesAcceptingUrl = getDatabaseTypesForUrl(url);
+    public static DatabaseType getDatabaseTypeForUrl(String url, Configuration configuration) {
+        List<DatabaseType> typesAcceptingUrl = getDatabaseTypesForUrl(url, configuration);
 
         if (typesAcceptingUrl.size() > 0) {
             if (typesAcceptingUrl.size() > 1) {
@@ -55,20 +56,35 @@ public class DatabaseTypeRegister {
         }
     }
 
-    private static List<DatabaseType> getDatabaseTypesForUrl(String url) {
-        List<DatabaseType> typesAcceptingUrl = new ArrayList<>();
-
-        for (DatabaseType type : SORTED_DATABASE_TYPES) {
-            if (type.handlesJDBCUrl(url)) {
-                typesAcceptingUrl.add(type);
-            }
-        }
+    private static List<DatabaseType> getDatabaseTypesForUrl(String url, Configuration configuration) {
+        return SORTED_DATABASE_TYPES.stream()
+            .filter(type -> configuration == null ||
+                configuration.isCommunityDBSupportEnabled() ||
+                !(type instanceof CommunityDatabaseType))
+            .filter(type -> type.handlesJDBCUrl(url))
+            .collect(Collectors.toList());
+    }
 
-        return typesAcceptingUrl;
+    public static DatabaseType getDatabaseTypeForEngineName(String engineName, Configuration configuration) {
+        return SORTED_DATABASE_TYPES.stream()
+            .filter(type -> configuration == null ||
+                configuration.isCommunityDBSupportEnabled() ||
+                !(type instanceof CommunityDatabaseType))
+            .filter(type -> type.getSupportedEngines().stream().anyMatch(engineName::equalsIgnoreCase))
+            .findFirst()
+            .orElseThrow(() -> new FlywayException("No database found to handle " + engineName + " engine"));
     }
 
     public static String redactJdbcUrl(String url) {
-        List<DatabaseType> types = getDatabaseTypesForUrl(url);
+        return redactJdbcUrl(url, (Configuration) null);
+    }
+
+    public static String redactJdbcUrl(String url, final Configuration configuration) {
+        List<DatabaseType> types = getDatabaseTypesForUrl(url, configuration);
+        return redactJdbcUrl(url, types);
+    }
+
+    public static String redactJdbcUrl(String url, final List<DatabaseType> types) {
         if (types.isEmpty()) {
             url = redactJdbcUrl(url, BaseDatabaseType.getDefaultJDBCCredentialsPattern());
         } else {
@@ -91,17 +107,17 @@ public class DatabaseTypeRegister {
         return url;
     }
 
-    public static DatabaseType getDatabaseTypeForConnection(Connection connection) {
+    public static DatabaseType getDatabaseTypeForConnection(Connection connection, Configuration configuration) {
         DatabaseMetaData databaseMetaData = JdbcUtils.getDatabaseMetaData(connection);
         String databaseProductName = JdbcUtils.getDatabaseProductName(databaseMetaData);
         String databaseProductVersion = JdbcUtils.getDatabaseProductVersion(databaseMetaData);
 
-        for (DatabaseType type : SORTED_DATABASE_TYPES) {
-            if (type.handlesDatabaseProductNameAndVersion(databaseProductName, databaseProductVersion, connection)) {
-                return type;
-            }
-        }
-
-        throw new FlywayException("Unsupported Database: " + databaseProductName);
+        return SORTED_DATABASE_TYPES.stream()
+            .filter(type -> configuration == null ||
+                configuration.isCommunityDBSupportEnabled() ||
+                !(type instanceof CommunityDatabaseType))
+            .filter(type -> type.handlesDatabaseProductNameAndVersion(databaseProductName, databaseProductVersion, connection))
+            .findFirst()
+            .orElseThrow(() -> new FlywayException("Unsupported Database: " + databaseProductName));
     }
 }
\ No newline at end of file

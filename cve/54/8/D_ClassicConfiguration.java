@@ -346,6 +346,11 @@ public class ClassicConfiguration implements Configuration {
         return getModernFlyway().getCleanDisabled();
     }
 
+    @Override
+    public boolean isCommunityDBSupportEnabled() {
+        return getModernFlyway().getCommunityDBSupportEnabled();
+    }
+
     @Override
     public boolean isMixed() {
         return getModernFlyway().getMixed();
@@ -384,6 +389,10 @@ public class ClassicConfiguration implements Configuration {
 
 
 
+
+
+
+
         }
 
         return this.dryRunOutput;
@@ -926,7 +935,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setPlaceholderPrefix(String placeholderPrefix) {
         if (!StringUtils.hasLength(placeholderPrefix)) {
-            throw new FlywayException("placeholderPrefix cannot be empty!", ErrorCode.CONFIGURATION);
+            throw new FlywayException("placeholderPrefix cannot be empty!", CoreErrorCode.CONFIGURATION);
         }
         getModernFlyway().setPlaceholderPrefix(placeholderPrefix);
     }
@@ -938,7 +947,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setScriptPlaceholderPrefix(String scriptPlaceholderPrefix) {
         if (!StringUtils.hasLength(scriptPlaceholderPrefix)) {
-            throw new FlywayException("scriptPlaceholderPrefix cannot be empty!", ErrorCode.CONFIGURATION);
+            throw new FlywayException("scriptPlaceholderPrefix cannot be empty!", CoreErrorCode.CONFIGURATION);
         }
         getModernFlyway().setScriptPlaceholderPrefix(scriptPlaceholderPrefix);
     }
@@ -950,7 +959,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setPlaceholderSuffix(String placeholderSuffix) {
         if (!StringUtils.hasLength(placeholderSuffix)) {
-            throw new FlywayException("placeholderSuffix cannot be empty!", ErrorCode.CONFIGURATION);
+            throw new FlywayException("placeholderSuffix cannot be empty!", CoreErrorCode.CONFIGURATION);
         }
         getModernFlyway().setPlaceholderSuffix(placeholderSuffix);
     }
@@ -962,7 +971,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setPlaceholderSeparator(String placeholderSeparator) {
         if (!StringUtils.hasLength(placeholderSeparator)) {
-            throw new FlywayException("placeholderSeparator cannot be empty!", ErrorCode.CONFIGURATION);
+            throw new FlywayException("placeholderSeparator cannot be empty!", CoreErrorCode.CONFIGURATION);
         }
         getModernFlyway().setPlaceholderSeparator(placeholderSeparator);
     }
@@ -974,7 +983,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setScriptPlaceholderSuffix(String scriptPlaceholderSuffix) {
         if (!StringUtils.hasLength(scriptPlaceholderSuffix)) {
-            throw new FlywayException("scriptPlaceholderSuffix cannot be empty!", ErrorCode.CONFIGURATION);
+            throw new FlywayException("scriptPlaceholderSuffix cannot be empty!", CoreErrorCode.CONFIGURATION);
         }
         getModernFlyway().setScriptPlaceholderSuffix(scriptPlaceholderSuffix);
     }
@@ -999,7 +1008,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setJavaMigrations(JavaMigration... javaMigrations) {
         if (javaMigrations == null) {
-            throw new FlywayException("javaMigrations cannot be null", ErrorCode.CONFIGURATION);
+            throw new FlywayException("javaMigrations cannot be null", CoreErrorCode.CONFIGURATION);
         }
         this.javaMigrations = javaMigrations;
     }
@@ -1040,7 +1049,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setSqlMigrationSeparator(String sqlMigrationSeparator) {
         if (!StringUtils.hasLength(sqlMigrationSeparator)) {
-            throw new FlywayException("sqlMigrationSeparator cannot be empty!", ErrorCode.CONFIGURATION);
+            throw new FlywayException("sqlMigrationSeparator cannot be empty!", CoreErrorCode.CONFIGURATION);
         }
 
         getModernFlyway().setSqlMigrationSeparator(sqlMigrationSeparator);
@@ -1094,14 +1103,14 @@ public class ClassicConfiguration implements Configuration {
         DataSourceModel model = dataSources.getOrDefault(getCurrentEnvironmentName(), null);
 
         if (StringUtils.hasText(url)) {
-            return DatabaseTypeRegister.getDatabaseTypeForUrl(url);
+            return DatabaseTypeRegister.getDatabaseTypeForUrl(url, this);
         } else if (model != null) {
             if (model.getDatabaseType() != null) {
                 return model.getDatabaseType();
             }
             if (model.getDataSource() != null) {
                 try (Connection connection = model.getDataSource().getConnection()) {
-                    DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(connection);
+                    DatabaseType databaseType = DatabaseTypeRegister.getDatabaseTypeForConnection(connection, this);
                     model.setDatabaseType(databaseType);
                     return databaseType;
                 } catch (SQLException ignored) {
@@ -1121,7 +1130,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setConnectRetries(int connectRetries) {
         if (connectRetries < 0) {
-            throw new FlywayException("Invalid number of connectRetries (must be 0 or greater): " + connectRetries, ErrorCode.CONFIGURATION);
+            throw new FlywayException("Invalid number of connectRetries (must be 0 or greater): " + connectRetries, CoreErrorCode.CONFIGURATION);
         }
         getCurrentUnresolvedEnvironment().setConnectRetries(connectRetries);
         requestResolvedEnvironmentRefresh(getCurrentEnvironmentName());
@@ -1135,7 +1144,7 @@ public class ClassicConfiguration implements Configuration {
      */
     public void setConnectRetriesInterval(int connectRetriesInterval) {
         if (connectRetriesInterval < 0) {
-            throw new FlywayException("Invalid number for connectRetriesInterval (must be 0 or greater): " + connectRetriesInterval, ErrorCode.CONFIGURATION);
+            throw new FlywayException("Invalid number for connectRetriesInterval (must be 0 or greater): " + connectRetriesInterval, CoreErrorCode.CONFIGURATION);
         }
         getCurrentUnresolvedEnvironment().setConnectRetriesInterval(connectRetriesInterval);
         requestResolvedEnvironmentRefresh(getCurrentEnvironmentName());
@@ -1205,7 +1214,7 @@ public class ClassicConfiguration implements Configuration {
             if (o instanceof Callback) {
                 callbacks.add((Callback) o);
             } else {
-                throw new FlywayException("Invalid callback: " + callbackPath + " (must implement org.flywaydb.core.api.callback.Callback)", ErrorCode.CONFIGURATION);
+                throw new FlywayException("Invalid callback: " + callbackPath + " (must implement org.flywaydb.core.api.callback.Callback)", CoreErrorCode.CONFIGURATION);
             }
         } else {
             // else try to scan this location and load all callbacks found within
@@ -1374,6 +1383,10 @@ public class ClassicConfiguration implements Configuration {
         requestResolvedEnvironmentRefresh(getCurrentEnvironmentName());
     }
 
+    private void setJarDirsAsStrings(String... jarDirs) {
+        getCurrentUnresolvedEnvironment().setJarDirs(Arrays.stream(jarDirs).collect(Collectors.toList()));
+    }
+
     /**
      * Configures Flyway with these properties. This overwrites any existing configuration. Properties are documented
      * here: https://documentation.red-gate.com/fd/parameters-184127474.html
@@ -1479,6 +1492,10 @@ public class ClassicConfiguration implements Configuration {
         if (locationsProp != null) {
             setLocationsAsStrings(StringUtils.tokenizeToStringArray(locationsProp, ","));
         }
+        String jarDirsProp = props.remove(ConfigUtils.JAR_DIRS);
+        if (jarDirsProp != null) {
+            setJarDirsAsStrings(StringUtils.tokenizeToStringArray(jarDirsProp, ","));
+        }
         Boolean placeholderReplacementProp = removeBoolean(props, ConfigUtils.PLACEHOLDER_REPLACEMENT);
         if (placeholderReplacementProp != null) {
             setPlaceholderReplacement(placeholderReplacementProp);
@@ -1555,6 +1572,10 @@ public class ClassicConfiguration implements Configuration {
         if (cleanDisabledProp != null) {
             setCleanDisabled(cleanDisabledProp);
         }
+        Boolean communityDBSupportEnabledProd = removeBoolean(props, ConfigUtils.COMMUNITY_DB_SUPPORT_ENABLED);
+        if (communityDBSupportEnabledProd != null) {
+            setCommunityDBSupportEnabled(communityDBSupportEnabledProd);
+        }
         Boolean reportEnabledProp = removeBoolean(props, ConfigUtils.REPORT_ENABLED);
         if (reportEnabledProp != null) {
             setReportEnabled(reportEnabledProp);
@@ -1829,6 +1850,10 @@ public class ClassicConfiguration implements Configuration {
         getModernFlyway().setCleanDisabled(cleanDisabledProp);
     }
 
+    public void setCommunityDBSupportEnabled(Boolean communityDBSupportEnabled){
+        getModernFlyway().setCommunityDBSupportEnabled(communityDBSupportEnabled);
+    }
+
     public void setReportEnabled(Boolean reportEnabled) {
         getModernFlyway().setReportEnabled(reportEnabled);
     }

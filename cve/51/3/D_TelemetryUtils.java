@@ -17,11 +17,13 @@ package org.flywaydb.commandline.utils;
 
 import lombok.AccessLevel;
 import lombok.NoArgsConstructor;
+import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.configuration.ClassicConfiguration;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.api.output.InfoOutput;
 import org.flywaydb.core.extensibility.RgDomainChecker;
 import org.flywaydb.core.extensibility.RootTelemetryModel;
+import org.flywaydb.core.internal.configuration.models.ConfigurationModel;
 import org.flywaydb.core.internal.database.base.Database;
 import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
 import org.flywaydb.core.internal.license.VersionPrinter;
@@ -32,19 +34,31 @@ import java.util.ArrayList;
 import java.util.Comparator;
 import java.util.List;
 
+import java.math.BigInteger;
+import java.nio.charset.StandardCharsets;
+import java.security.MessageDigest;
+import java.security.NoSuchAlgorithmException;
+
 @NoArgsConstructor(access = AccessLevel.PRIVATE)
 public class TelemetryUtils {
 
     public static RootTelemetryModel populateRootTelemetry(RootTelemetryModel rootTelemetryModel, Configuration configuration, boolean isRedgateEmployee) {
         rootTelemetryModel.setRedgateEmployee(isRedgateEmployee);
 
-        if(configuration != null) {
+        if (configuration != null) {
             ClassicConfiguration classicConfiguration = new ClassicConfiguration(configuration);
             if (classicConfiguration.getDataSource() != null) {
                 try (JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(classicConfiguration.getDataSource(), classicConfiguration, null);
                      Database database = jdbcConnectionFactory.getDatabaseType().createDatabase(configuration, false, jdbcConnectionFactory, null)) {
                     rootTelemetryModel.setDatabaseEngine(database.getDatabaseType().getName());
                     rootTelemetryModel.setDatabaseVersion(database.getVersion().toString());
+                    ConfigurationModel modernConfig = configuration.getModernConfig();
+                    if (modernConfig != null) {
+                        if (StringUtils.hasText(modernConfig.getId())) {
+                            String hashedProjectId = hashProjectId(modernConfig.getId());
+                            rootTelemetryModel.setProjectId(hashedProjectId);
+                        }
+                    }
                     return rootTelemetryModel;
                 }
             }
@@ -55,14 +69,32 @@ public class TelemetryUtils {
         return rootTelemetryModel;
     }
 
-    public static boolean isRedgateEmployee(PluginRegister pluginRegister, Configuration configuration) {
-       RgDomainChecker domainChecker = pluginRegister.getPlugin(RgDomainChecker.class);
-       if (domainChecker == null) {
-           return false;
-       }
-       return domainChecker.isInDomain(configuration);
+    static String hashProjectId(String projectId) {
+        if (projectId == null) {
+            return null;
+        }
+        try {
+            MessageDigest md = MessageDigest.getInstance("SHA-256");
+            md.update(projectId.getBytes(StandardCharsets.UTF_8));
+            byte[] hash = md.digest("fur".getBytes(StandardCharsets.UTF_8));
+            BigInteger number = new BigInteger(1, hash);
+            String result = number.toString(16);
+            while (result.length() < 64) {
+                result = "0" + result;
+            }
+            return result;
+        } catch (Exception e) {
+            throw new FlywayException(e);
+        }
     }
 
+    public static boolean isRedgateEmployee(PluginRegister pluginRegister, Configuration configuration) {
+        RgDomainChecker domainChecker = pluginRegister.getPlugin(RgDomainChecker.class);
+        if (domainChecker == null) {
+            return false;
+        }
+        return domainChecker.isInDomain(configuration);
+    }
 
     /**
      * @param infos a List of InfoOutput
@@ -81,7 +113,6 @@ public class TelemetryUtils {
         infos.stream().filter(output -> StringUtils.hasText(output.installedOnUTC))
              .forEach(output -> migrationDates.add(output.installedOnUTC));
 
-
         if (!migrationDates.isEmpty()) {
             migrationDates.sort(Comparator.naturalOrder());
             return migrationDates.get(0);

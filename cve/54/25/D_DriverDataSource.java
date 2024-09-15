@@ -18,7 +18,7 @@ package org.flywaydb.core.internal.jdbc;
 import lombok.AccessLevel;
 import lombok.Getter;
 import lombok.Setter;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.internal.database.DatabaseType;
@@ -106,7 +106,7 @@ public class DriverDataSource implements DataSource {
                             Map<String, String> additionalProperties) throws FlywayException {
         this.url = detectFallbackUrl(url);
 
-        this.type = DatabaseTypeRegister.getDatabaseTypeForUrl(url);
+        this.type = DatabaseTypeRegister.getDatabaseTypeForUrl(url, configuration);
 
         if (!StringUtils.hasLength(driverClass)) {
             if (type == null) {
@@ -138,7 +138,7 @@ public class DriverDataSource implements DataSource {
                 throw new FlywayException("Unable to instantiate JDBC driver: " + driverClass
                                                   + " => Check whether the jar file is present"
                                                   + extendedError, e,
-                                          ErrorCode.JDBC_DRIVER);
+                                          CoreErrorCode.JDBC_DRIVER);
             }
             try {
                 this.driver = ClassUtils.instantiate(backupDriverClass, classLoader);
@@ -146,7 +146,7 @@ public class DriverDataSource implements DataSource {
                 // Only report original exception about primary driver
                 throw new FlywayException(
                         "Unable to instantiate JDBC driver: " + driverClass + " or backup driver: " + backupDriverClass + " => Check whether the jar file is present", e,
-                        ErrorCode.JDBC_DRIVER);
+                        CoreErrorCode.JDBC_DRIVER);
             }
         }
 

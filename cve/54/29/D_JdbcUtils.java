@@ -15,6 +15,7 @@
  */
 package org.flywaydb.core.internal.jdbc;
 
+import java.util.Properties;
 import lombok.AccessLevel;
 import lombok.CustomLog;
 import lombok.RequiredArgsConstructor;
@@ -42,6 +43,12 @@ public class JdbcUtils {
      */
     public static Connection openConnection(DataSource dataSource, int connectRetries, int connectRetriesInterval) throws FlywayException {
         BackoffStrategy backoffStrategy = new BackoffStrategy(1, 2, connectRetriesInterval);
+
+        Properties systemProperties = System.getProperties();
+        if (!systemProperties.containsKey("polyglot.engine.WarnInterpreterOnly")) {
+            systemProperties.put("polyglot.engine.WarnInterpreterOnly", false);
+        }
+
         int retries = 0;
         while (true) {
             try {

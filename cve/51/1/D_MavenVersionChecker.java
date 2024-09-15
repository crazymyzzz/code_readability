@@ -15,93 +15,59 @@
  */
 package org.flywaydb.commandline;
 
-import com.google.gson.Gson;
+import com.fasterxml.jackson.databind.DeserializationFeature;
+import com.fasterxml.jackson.dataformat.xml.XmlMapper;
 import lombok.AccessLevel;
-import lombok.Cleanup;
 import lombok.CustomLog;
+import lombok.Getter;
 import lombok.NoArgsConstructor;
+import lombok.Setter;
 import org.flywaydb.core.api.MigrationVersion;
 import org.flywaydb.core.internal.license.VersionPrinter;
 import org.flywaydb.core.internal.util.FlywayDbWebsiteLinks;
 
-import javax.net.ssl.HttpsURLConnection;
-import java.io.BufferedReader;
-import java.io.InputStreamReader;
-import java.net.InetAddress;
 import java.net.URL;
 
 @CustomLog
 @NoArgsConstructor(access = AccessLevel.PRIVATE)
 public class MavenVersionChecker {
-    private static class MavenResponse {
-        public MavenDoc[] docs;
-    }
-
-    private static class MavenDoc {
-        public String latestVersion;
 
-        // 'g' is the key for the group id in the Maven REST API
-        public String g;
+    @Setter
+    @Getter
+    @NoArgsConstructor
+    private static class MavenVersioning {
+        private String release;
     }
 
-    private static class MavenObject {
-        public MavenResponse response;
+    @Setter
+    @Getter
+    @NoArgsConstructor
+    private static class MavenMetadata {
+        private MavenVersioning versioning;
     }
 
-    private static final String FLYWAY_URL = "https://search.maven.org/solrsearch/select?q=a:flyway-core";
+    private static final String FLYWAY_URL =
 
-    private static boolean canConnectToMaven() {
-        try {
-            InetAddress address = InetAddress.getByName("maven.org");
-            return address.isReachable(500);
-        } catch (Exception e) {
-            return false;
-        }
-    }
+             "https://repo1.maven.org/maven2/org/flywaydb/flyway-core/maven-metadata.xml";
 
-    public static void checkForVersionUpdates() {
-        if (!canConnectToMaven()) {
-            return;
-        }
 
-        try {
-            URL url = new URL(FLYWAY_URL);
-            @Cleanup(value = "disconnect") HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
-            connection.setRequestMethod("GET");
-            connection.setConnectTimeout(1000);
 
-            StringBuilder response = new StringBuilder();
 
-            try (BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
-                String line;
-                while ((line = rd.readLine()) != null) {
-                    response.append(line).append('\r');
-                }
-            }
-
-            MavenObject obj = new Gson().fromJson(response.toString(), MavenObject.class);
 
+    public static void checkForVersionUpdates() {
+        try {
             MigrationVersion current = MigrationVersion.fromVersion(VersionPrinter.getVersion());
-            MigrationVersion latest = null;
-
-            String groupID = "org.flywaydb";
-
+            MavenMetadata metadata = new XmlMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
+                                                    .readValue(new URL(FLYWAY_URL), MavenMetadata.class);
 
-
-
-            MavenDoc[] mavenDocs = obj.response.docs;
-            for (MavenDoc mavenDoc : mavenDocs) {
-                if (mavenDoc.g.equals(groupID)) {
-                    latest = MigrationVersion.fromVersion(mavenDoc.latestVersion);
-                    break;
-                }
-            }
+            MigrationVersion latest = MigrationVersion.fromVersion(metadata.getVersioning().getRelease());
 
             if (current.compareTo(latest) < 0) {
                 LOG.warn("This version of Flyway is out of date. Upgrade to Flyway " + latest + ": "
                                  + FlywayDbWebsiteLinks.STAYING_UP_TO_DATE + "\n");
             }
-        } catch (Exception ignored) {
+        } catch (Exception e) {
+            LOG.debug("Unable to check for updates: " + e.getMessage());
         }
     }
 }
\ No newline at end of file

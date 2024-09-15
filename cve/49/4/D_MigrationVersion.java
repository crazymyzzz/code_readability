@@ -15,6 +15,8 @@
  */
 package org.flywaydb.core.api;
 
+import lombok.Getter;
+
 import java.math.BigInteger;
 import java.util.ArrayList;
 import java.util.List;
@@ -51,6 +53,12 @@ public final class MigrationVersion implements Comparable<MigrationVersion> {
      */
     private final String displayText;
 
+    /**
+     * The raw, unprocessed text to represent the version.
+     */
+    @Getter
+    private final String rawVersion;
+
     /**
      * Create a MigrationVersion from a version String.
      *
@@ -85,6 +93,7 @@ public final class MigrationVersion implements Comparable<MigrationVersion> {
         String normalizedVersion = version.replace('_', '.');
         this.versionParts = tokenize(normalizedVersion);
         this.displayText = normalizedVersion;
+        this.rawVersion = version;
     }
 
     /**
@@ -96,6 +105,7 @@ public final class MigrationVersion implements Comparable<MigrationVersion> {
         this.versionParts = new ArrayList<>();
         this.versionParts.add(version);
         this.displayText = displayText;
+        this.rawVersion = displayText;
     }
 
     @Override

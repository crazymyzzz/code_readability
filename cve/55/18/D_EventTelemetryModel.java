@@ -19,13 +19,12 @@
  */
 package org.flywaydb.core.extensibility;
 
+import java.time.Duration;
+import java.time.Instant;
 import lombok.Getter;
 import lombok.Setter;
 import org.flywaydb.core.FlywayTelemetryManager;
 
-import java.time.Duration;
-import java.time.Instant;
-
 @Getter
 @Setter
 public class EventTelemetryModel implements AutoCloseable {
@@ -35,16 +34,17 @@ public class EventTelemetryModel implements AutoCloseable {
 
     private final FlywayTelemetryManager flywayTelemetryManager;
     private Instant startTime;
+
     public EventTelemetryModel(String name, FlywayTelemetryManager flywayTelemetryManager) {
         startTime = Instant.now();
         this.flywayTelemetryManager = flywayTelemetryManager;
         this.name = name;
-
     }
+
     @Override
-    public void close() throws Exception {
+    public void close() {
         duration = Duration.between(startTime, Instant.now()).toMillis();
-        if(flywayTelemetryManager != null) {
+        if (flywayTelemetryManager != null) {
             flywayTelemetryManager.logEvent(this);
         }
     }

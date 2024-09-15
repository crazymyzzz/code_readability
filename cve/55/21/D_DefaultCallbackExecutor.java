@@ -19,6 +19,7 @@
  */
 package org.flywaydb.core.internal.callback;
 
+import org.flywaydb.core.FlywayTelemetryManager;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.MigrationInfo;
 import org.flywaydb.core.api.callback.Error;
@@ -26,6 +27,7 @@ import org.flywaydb.core.api.callback.*;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.api.exception.FlywayBlockStatementExecutionException;
 import org.flywaydb.core.api.output.OperationResult;
+import org.flywaydb.core.extensibility.EventTelemetryModel;
 import org.flywaydb.core.internal.database.base.Connection;
 import org.flywaydb.core.internal.database.base.Database;
 import org.flywaydb.core.internal.database.base.Schema;
@@ -45,9 +47,11 @@ public class DefaultCallbackExecutor implements CallbackExecutor {
     private final Configuration configuration;
     private final Database database;
     private final Schema schema;
+    private final FlywayTelemetryManager flywayTelemetryManager;
     private final List<Callback> callbacks;
     private MigrationInfo migrationInfo;
 
+
     /**
      * Creates a new callback executor.
      *
@@ -56,10 +60,11 @@ public class DefaultCallbackExecutor implements CallbackExecutor {
      * @param schema The current schema to use for the connection.
      * @param callbacks The callbacks to execute.
      */
-    public DefaultCallbackExecutor(Configuration configuration, Database database, Schema schema, Collection<Callback> callbacks) {
+    public DefaultCallbackExecutor(Configuration configuration, Database database, Schema schema, FlywayTelemetryManager flywayTelemetryManager, Collection<Callback> callbacks) {
         this.configuration = configuration;
         this.database = database;
         this.schema = schema;
+        this.flywayTelemetryManager = flywayTelemetryManager;
 
         this.callbacks = new ArrayList<>(callbacks);
         this.callbacks.sort(Comparator.comparing(Callback::getCallbackName));
@@ -89,7 +94,7 @@ public class DefaultCallbackExecutor implements CallbackExecutor {
         final Context context = new SimpleContext(configuration, database.getMigrationConnection(), migrationInfo, null);
         for (Callback callback : callbacks) {
             if (callback.supports(event, context)) {
-                callback.handle(event, context);
+                handleEvent(callback, event, context);
             }
         }
     }
@@ -108,7 +113,7 @@ public class DefaultCallbackExecutor implements CallbackExecutor {
         final Context context = new SimpleContext(configuration, database.getMigrationConnection(), migrationInfo, operationResult);
         for (Callback callback : callbacks) {
             if (callback.supports(event, context)) {
-                callback.handle(event, context);
+                handleEvent(callback, event, context);
             }
         }
     }
@@ -137,11 +142,11 @@ public class DefaultCallbackExecutor implements CallbackExecutor {
     }
 
     private void handleEvent(Callback callback, Event event, Context context) {
-        try {
+        try (EventTelemetryModel telemetryModel = new EventTelemetryModel(event.getId(), flywayTelemetryManager)) {
             callback.handle(event, context);
         } catch (FlywayBlockStatementExecutionException e) {
             throw e;
-        } catch (RuntimeException e) {
+        } catch (Exception e) {
             throw new FlywayException("Error while executing " + event.getId() + " callback: " + e.getMessage(), e);
         }
     }

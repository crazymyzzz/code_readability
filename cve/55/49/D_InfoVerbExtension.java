@@ -19,7 +19,14 @@
  */
 package org.flywaydb.verb.info;
 
+import java.sql.SQLException;
+import org.flywaydb.core.api.FlywayException;
+import org.flywaydb.core.api.configuration.Configuration;
+import org.flywaydb.core.experimental.ExperimentalDatabasePluginResolverImpl;
 import org.flywaydb.core.extensibility.VerbExtension;
+import org.flywaydb.core.internal.configuration.models.ResolvedEnvironment;
+import org.flywaydb.core.internal.configuration.resolvers.ProvisionerMode;
+import org.flywaydb.core.internal.util.StringUtils;
 
 public class InfoVerbExtension implements VerbExtension {
     @Override
@@ -28,8 +35,33 @@ public class InfoVerbExtension implements VerbExtension {
     }
 
     @Override
-    public Object executeVerb() {
+    public Object executeVerb(final Configuration configuration) {
         System.out.println("InfoVerbExtension.executeVerb");
-        return null;
+
+        final var experimentalDatabasePluginResolver = new ExperimentalDatabasePluginResolverImpl(configuration.getPluginRegister());
+        final var resolvedExperimentalDatabase = experimentalDatabasePluginResolver.resolve(configuration.getUrl());
+        if (resolvedExperimentalDatabase.isEmpty()) {
+            return null;
+        }
+        final var experimentalDatabase = resolvedExperimentalDatabase.get();
+
+        try {
+            experimentalDatabase.initialize(getResolvedEnvironment(configuration));
+            final var schemaHistoryModel = experimentalDatabase.getSchemaHistoryModel(configuration.getTable());
+            throw new FlywayException("Found " + schemaHistoryModel.getSchemaHistoryItems().size() + " migrations");
+        } catch (final SQLException e) {
+            throw new FlywayException(e);
+        }            
+    }
+    
+    private ResolvedEnvironment getResolvedEnvironment(final Configuration configuration) {
+        final var envName =  configuration.getCurrentEnvironmentName();
+        final var envProvisionMode = configuration.getModernConfig().getFlyway().getEnvironmentProvisionMode();
+        final var provisionerMode = StringUtils.hasText(envProvisionMode) ? ProvisionerMode.fromString(envProvisionMode) : ProvisionerMode.Provision;
+        final var resolved = configuration.getResolvedEnvironment(envName, provisionerMode, null);
+        if (resolved == null) {
+            throw new FlywayException("Environment '" + envName + "' not found. Check that this environment exists in your configuration.");
+        }
+        return resolved;
     }
 }

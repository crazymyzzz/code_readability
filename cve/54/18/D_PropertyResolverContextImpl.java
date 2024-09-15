@@ -16,7 +16,7 @@
 package org.flywaydb.core.internal.configuration.resolvers;
 
 import org.flywaydb.core.ProgressLogger;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.configuration.Configuration;
 
@@ -85,7 +85,7 @@ public class PropertyResolverContextImpl implements PropertyResolverContext {
     public String resolveValueOrThrow(final String input, final ProgressLogger progress, final String propertyName) {
         final var result = resolveValue(input, progress);
         if (result == null) {
-            throw new FlywayException("Configuration value " + propertyName + " not specified for environment " + environmentName, ErrorCode.CONFIGURATION);
+            throw new FlywayException("Configuration value " + propertyName + " not specified for environment " + environmentName, CoreErrorCode.CONFIGURATION);
         }
         return result;
     }
@@ -103,7 +103,7 @@ public class PropertyResolverContextImpl implements PropertyResolverContext {
         final String propertyName) {
         final var result = resolveValues(input, progress);
         if (result == null) {
-            throw new FlywayException("Configuration value " + propertyName + " not specified for environment " + environmentName, ErrorCode.CONFIGURATION);
+            throw new FlywayException("Configuration value " + propertyName + " not specified for environment " + environmentName, CoreErrorCode.CONFIGURATION);
         }
         return result;
     }
@@ -129,7 +129,7 @@ public class PropertyResolverContextImpl implements PropertyResolverContext {
 
         String resolverName = resolverMatch.substring(2, resolverMatch.indexOf(".")).strip();
         if (!resolvers.containsKey(resolverName)) {
-            throw new FlywayException("Unknown resolver '" + resolverName + "' for environment " + environmentName, ErrorCode.CONFIGURATION);
+            throw new FlywayException("Unknown resolver '" + resolverName + "' for environment " + environmentName, CoreErrorCode.CONFIGURATION);
         }
 
         String resolverParam;

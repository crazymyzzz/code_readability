@@ -20,7 +20,7 @@ import java.util.HashMap;
 import java.util.Map;
 import java.util.stream.Collectors;
 import org.flywaydb.core.ProgressLogger;
-import org.flywaydb.core.api.ErrorCode;
+import org.flywaydb.core.api.CoreErrorCode;
 import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.extensibility.ConfigurationExtension;
@@ -105,7 +105,7 @@ public class EnvironmentResolver {
             if (!environmentProvisioners.containsKey(provisionerName)) {
                 throw new FlywayException(
                     "Unknown provisioner '" + provisionerName + "' for environment " + context.getEnvironmentName(),
-                    ErrorCode.CONFIGURATION);
+                    CoreErrorCode.CONFIGURATION);
             }
             return environmentProvisioners.get(provisionerName);
         }
@@ -135,7 +135,7 @@ public class EnvironmentResolver {
                 final var data = environmentModel.getResolvers().get(key);
                 return (ConfigurationExtension) new ObjectMapper().convertValue(data, clazz);
             } catch (final IllegalArgumentException e) {
-                throw new FlywayException("Error reading resolver configuration for resolver " + key, e, ErrorCode.CONFIGURATION);
+                throw new FlywayException("Error reading resolver configuration for resolver " + key, e, CoreErrorCode.CONFIGURATION);
             }
         }
 

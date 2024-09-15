@@ -21,11 +21,15 @@ import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.module.SimpleModule;
 import lombok.CustomLog;
 import org.flywaydb.core.api.FlywayException;
+import org.flywaydb.core.api.configuration.ClassicConfiguration;
 import org.flywaydb.core.internal.configuration.models.ConfigurationModel;
+import org.flywaydb.core.internal.configuration.models.EnvironmentModel;
 import org.flywaydb.core.internal.util.ObjectMapperFactory;
+import org.flywaydb.core.internal.util.Pair;
 
 import java.io.File;
 import java.io.IOException;
+import java.util.Arrays;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
@@ -34,12 +38,40 @@ import java.util.stream.Collectors;
 @CustomLog
 public class TomlUtils {
 
+    public static final String MSG = "Using both new Environment variable %1$s and old Environment variable %2$s. Please remove %2$s.";
+
     public static ConfigurationModel loadConfigurationFromEnvironment() {
         Map<String, String> environmentVariables = System.getenv()
                                                          .entrySet()
                                                          .stream()
-                                                         .filter(e -> e.getKey().startsWith("flyway_") || e.getKey().startsWith("environments_"))
-                                                         .collect(Collectors.toMap(k -> k.getKey().replace("_", "."), Map.Entry::getValue));
+                                                         .filter(e -> e.getKey().startsWith("flyway_")
+                                                                 || e.getKey().startsWith("environments_")
+                                                                 || (e.getKey().startsWith("FLYWAY_") && ConfigUtils.convertKey(e.getKey()) != null))
+                                                         .collect(Collectors.toMap(k -> {
+                                                                                       String prop = k.getKey().startsWith("FLYWAY_") ?
+                                                                                               ConfigUtils.convertKey(k.getKey())
+                                                                                               : k.getKey().replace("_", ".");
+                                                                                       if (prop != null && prop.startsWith("flyway.")) {
+                                                                                           String p = prop.substring("flyway.".length());
+                                                                                           if (Arrays.stream((EnvironmentModel.class).getDeclaredFields()).anyMatch(x -> x.getName().equals(p))) {
+                                                                                               return "environments." + ClassicConfiguration.TEMP_ENVIRONMENT_NAME + "." + prop.substring("flyway.".length());
+                                                                                           }
+                                                                                       }
+                                                                                       return prop;
+                                                                                   },
+                                                                                   v -> Pair.of(v.getKey(), v.getValue()),
+                                                                                   (e1, e2) -> {
+                                                                                       if (e1.getLeft().startsWith("FLYWAY_")) {
+                                                                                           LOG.warn(String.format(MSG, e2.getLeft(), e1.getLeft()));
+                                                                                           return e2;
+                                                                                       } else {
+                                                                                           LOG.warn(String.format(MSG, e1.getLeft(), e2.getLeft()));
+                                                                                           return e1;
+                                                                                       }
+                                                                                   }))
+                                                         .entrySet()
+                                                         .stream()
+                                                         .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().getRight()));
         return toConfiguration(unflattenMap(environmentVariables));
     }
 

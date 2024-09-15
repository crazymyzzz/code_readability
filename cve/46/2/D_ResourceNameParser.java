@@ -19,8 +19,8 @@ import org.flywaydb.core.api.MigrationVersion;
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.extensibility.ResourceType;
 import org.flywaydb.core.extensibility.ResourceTypeProvider;
-import org.flywaydb.core.internal.plugin.PluginRegister;
 import org.flywaydb.core.internal.util.Pair;
+import org.flywaydb.core.internal.util.StringUtils;
 
 import java.util.*;
 
@@ -50,7 +50,7 @@ public class ResourceNameParser {
             // Strip off prefix
             Pair<String, String> prefixResult = stripPrefix(suffixResult.getLeft(), prefix.getLeft());
             String name = prefixResult.getRight();
-            Pair<String, String> splitName = splitAtSeparator(name, configuration.getSqlMigrationSeparator());
+            Pair<String, String> splitName = StringUtils.splitAtFirstSeparator(name, configuration.getSqlMigrationSeparator());
             boolean isValid = true;
             String validationMessage = "";
             String exampleDescription = ("".equals(splitName.getRight())) ? "description" : splitName.getRight();
@@ -118,16 +118,6 @@ public class ResourceNameParser {
         return null;
     }
 
-    private Pair<String, String> splitAtSeparator(String name, String separator) {
-        int separatorIndex = name.indexOf(separator);
-        if (separatorIndex >= 0) {
-            return Pair.of(name.substring(0, separatorIndex),
-                           name.substring(separatorIndex + separator.length()));
-        } else {
-            return Pair.of(name, "");
-        }
-    }
-
     private List<Pair<String, ResourceType>> populatePrefixes(Configuration configuration) {
         List<Pair<String, ResourceType>> prefixes = new ArrayList<>();
 

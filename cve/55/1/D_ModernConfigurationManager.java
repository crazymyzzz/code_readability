@@ -115,6 +115,8 @@ public class ModernConfigurationManager implements ConfigurationManager {
                     if(entry.getKey().startsWith("jdbcProperties.")) {
                         envValueObject.computeIfAbsent("jdbcProperties", s -> new HashMap<String, String>());
                         ((Map<String, String>)envValueObject.get("jdbcProperties")).put(entry.getKey().substring("jdbcProperties.".length()), entry.getValue());
+                    } else if (entry.getKey().equals("schemas") || entry.getKey().equals("jarDirs")) {
+                        envValueObject.put(entry.getKey(), Arrays.stream(entry.getValue().split(",")).map(String::trim).toList());
                     } else {
                         envValueObject.put(entry.getKey(), entry.getValue());
                     }

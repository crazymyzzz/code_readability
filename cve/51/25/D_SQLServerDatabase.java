@@ -281,7 +281,7 @@ public class SQLServerDatabase extends Database<SQLServerConnection> {
         }
 
         String cleanMode = configuration.getPluginRegister().getPlugin(CleanModeConfigurationExtension.class).getClean().getMode();
-        if (Mode.ALL.name().equals(cleanMode)) {
+        if (Mode.ALL.name().equalsIgnoreCase(cleanMode)) {
             CleanModePlugin cleanModePlugin = configuration.getPluginRegister().getPlugins(CleanModePlugin.class).stream()
                                                            .filter(p -> p.handlesMode(Mode.valueOf(cleanMode)))
                                                            .filter(p -> p.handlesDatabase(this))

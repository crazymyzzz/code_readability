@@ -57,7 +57,7 @@ public class VersionCommandExtension implements CommandExtension {
         LOG.debug("Java " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
         LOG.debug(System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + "\n");
 
-        return new VersionResult(VersionPrinter.getVersion(), command);
+        return new VersionResult(VersionPrinter.getVersion(), command, VersionPrinter.EDITION);
     }
 
     @Override

@@ -19,9 +19,11 @@
  */
 package org.flywaydb.core.extensibility;
 
+import org.flywaydb.core.api.configuration.Configuration;
+
 public interface VerbExtension extends Plugin {
 
     boolean handlesVerb(String verb);
 
-    Object executeVerb();
+    Object executeVerb(Configuration configuration);
 }

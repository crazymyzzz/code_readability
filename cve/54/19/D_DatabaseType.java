@@ -42,6 +42,8 @@ public interface DatabaseType extends Plugin {
      */
     String getName();
 
+    List<String> getSupportedEngines();
+
     /**
      * @return The JDBC type used to represent {@code null} in prepared statements.
      */

@@ -42,6 +42,7 @@ public class SqlScriptMetadata {
     private final Boolean executeInTransaction;
     private final String encoding;
     private final boolean placeholderReplacement;
+    private String shouldExecuteExpression;
     private boolean shouldExecute;
 
     private SqlScriptMetadata(Map<String, String> metadata, Configuration config) {
@@ -55,6 +56,8 @@ public class SqlScriptMetadata {
         metadata.remove(PLACEHOLDER_REPLACEMENT);
 
         this.shouldExecute = true;
+        this.shouldExecuteExpression = null;
+
 
 
 
@@ -86,6 +89,10 @@ public class SqlScriptMetadata {
         return shouldExecute;
     }
 
+    public String shouldExecuteExpression() {
+        return shouldExecuteExpression;
+    }
+
     public static boolean isMultilineBooleanExpression(String line) {
         return !line.startsWith(SHOULD_EXECUTE) && (line.contains("==") || line.contains("!="));
     }

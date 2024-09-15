@@ -184,6 +184,11 @@ public class ParserSqlScript implements SqlScript {
         return metadata.shouldExecute();
     }
 
+    @Override
+    public String shouldExecuteExpression() {
+        return metadata.shouldExecuteExpression();
+    }
+
     @Override
     public int compareTo(SqlScript o) {
         return resource.getRelativePath().compareTo(o.getResource().getRelativePath());

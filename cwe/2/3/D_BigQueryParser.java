@@ -145,6 +145,7 @@ public class BigQueryParser extends Parser {
                 && !"WHILE".equalsIgnoreCase(keywordText)
                 && !"LOOP".equalsIgnoreCase(keywordText)
                 && !"AS".equalsIgnoreCase(keywordText)
+                && !"CASE".equalsIgnoreCase(keywordText)
                 && context.getBlockDepth() > 0) {
             context.decreaseBlockDepth();
         }

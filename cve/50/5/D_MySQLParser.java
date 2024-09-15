
@@ -173,11 +153,5 @@ public class MySQLParser extends Parser {
                 context.decreaseBlockDepth();
             }
         }
-
-        if (";".equals(keywordText) || TokenType.DELIMITER.equals(keyword.getType()) || TokenType.EOF.equals(keyword.getType())) {
-            if (context.getBlockDepth() > 0 && doesDelimiterEndFunction(tokens, keyword)) {
-                context.decreaseBlockDepth();
-            }
-        }
     }
 }
\ No newline at end of file

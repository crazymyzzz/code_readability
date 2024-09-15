@@ -296,7 +296,7 @@ public abstract class Parser {
         } catch (Exception e) {
             IOUtils.close(reader);
             throw new FlywayException("Unable to parse statement in " + resource.getAbsolutePath()
-                                              + " at line " + statementLine + " col " + statementCol + ". See " + FlywayDbWebsiteLinks.KNOWN_PARSER_LIMITATIONS + " for more information: " + e.getMessage(), e);
+                                              + " at line " + statementLine + " col " + statementCol + ". See " + FlywayDbWebsiteLinks.KNOWN_PARSER_LIMITATIONS + " for more information. " + getAdditionalParsingErrorInfo() + e.getMessage(), e);
         }
     }
 
@@ -674,6 +674,10 @@ public abstract class Parser {
         return new Token(TokenType.KEYWORD, pos, line, col, keyword.toUpperCase(Locale.ENGLISH), keyword, context.getParensDepth());
     }
 
+    protected String getAdditionalParsingErrorInfo() {
+        return "";
+    }
+
     private static boolean containsAtLeast(String str, char c, int min) {
         if (min > str.length()) {
             return false;

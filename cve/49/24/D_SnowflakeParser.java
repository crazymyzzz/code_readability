@@ -43,6 +43,13 @@ public class SnowflakeParser extends Parser {
         return super.isAlternativeStringLiteral(peek);
     }
 
+    @Override
+    protected Token handleStringLiteral(PeekingReader reader, ParserContext context, int pos, int line, int col) throws IOException {
+        reader.swallow();
+        reader.swallowUntilIncludingWithEscape('\'', true, '\\');
+        return new Token(TokenType.STRING, pos, line, col, null, null, context.getParensDepth());
+    }
+
     @Override
     protected Token handleAlternativeStringLiteral(PeekingReader reader, ParserContext context, int pos, int line, int col) throws IOException {
         String alternativeQuoteOpen = ALTERNATIVE_QUOTE;

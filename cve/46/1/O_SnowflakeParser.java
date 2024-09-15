

public class SnowflakeParser extends Parser {


    @Override
    protected Token handleAlternativeStringLiteral(PeekingReader reader, ParserContext context, int pos, int line, int col) throws IOException {
        String alternativeQuoteOpen = ALTERNATIVE_QUOTE;
        String alternativeQuoteEnd = ALTERNATIVE_QUOTE;

        if (reader.peek(ALTERNATIVE_QUOTE_SCRIPT)) {
            alternativeQuoteOpen = ALTERNATIVE_QUOTE_SCRIPT;
            alternativeQuoteEnd = "END";
        }

        reader.swallow(alternativeQuoteOpen.length());
        String text = reader.readUntilExcluding(alternativeQuoteEnd);
        reader.swallow(alternativeQuoteEnd.length());

        return new Token(TokenType.STRING, pos, line, col, text, text, context.getParensDepth());
    }


}
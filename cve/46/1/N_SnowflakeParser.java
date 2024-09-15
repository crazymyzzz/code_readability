
public class SnowflakeParser extends Parser {


    @Override
    protected Token handleAlternativeStringLiteral(PeekingReader reader, ParserContext context, int pos, int line, int col) throws IOException {
        String alternativeQuoteOpen = ALTERNATIVE_QUOTE;
        String alternativeQuoteEnd = ALTERNATIVE_QUOTE;

        String text;
        if (reader.peek(ALTERNATIVE_QUOTE_SCRIPT)) {
            alternativeQuoteOpen = "BEGIN";
            alternativeQuoteEnd = "END";
            reader.swallowUntilExcluding(alternativeQuoteOpen);
            text = readBetweenRecursive(reader, alternativeQuoteOpen, alternativeQuoteEnd);
        } else {
            reader.swallow(alternativeQuoteOpen.length());
            text = reader.readUntilExcluding(alternativeQuoteOpen, alternativeQuoteEnd);
            reader.swallow(alternativeQuoteEnd.length());
        }

        return new Token(TokenType.STRING, pos, line, col, text, text, context.getParensDepth());
    }


    private String readBetweenRecursive(PeekingReader reader, String prefix, String suffix) throws IOException {
        StringBuilder result = new StringBuilder();
        reader.swallow(prefix.length());
        while (!reader.peek(suffix)) {
            result.append(reader.readUntilExcluding(prefix, suffix));
            if (reader.peek(prefix)) {
                result.append(prefix).append(readBetweenRecursive(reader, prefix, suffix)).append(suffix);
            }
        }
        reader.swallow(suffix.length());
        return result.toString();
    }
}
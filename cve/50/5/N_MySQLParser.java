

public class MySQLParser extends Parser {


    @Override
    protected void adjustBlockDepth(ParserContext context, List<Token> tokens, Token keyword, PeekingReader reader) {
        String keywordText = keyword.getText();

        int parensDepth = keyword.getParensDepth();

        if ("BEGIN".equalsIgnoreCase(keywordText) && context.getStatementType() == STORED_PROGRAM_STATEMENT) {
            context.increaseBlockDepth(Integer.toString(parensDepth));
        }

        if (context.getBlockDepth() > 0
            && lastTokenIs(tokens, parensDepth, "END")
            && !"IF".equalsIgnoreCase(keywordText)
            && !"CASE".equalsIgnoreCase(keywordText)
            && !"LOOP".equalsIgnoreCase(keywordText)
            && !"REPEAT".equalsIgnoreCase(keywordText)
            && !"WHILE".equalsIgnoreCase(keywordText)) {
            String initiator = context.getBlockInitiator();
            if (initiator.equals("") || initiator.equals(keywordText) || "AS".equalsIgnoreCase(keywordText) || initiator.equals(Integer.toString(parensDepth))) {
                context.decreaseBlockDepth();
            }
        }
    }
}
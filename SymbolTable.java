import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    private final List<Lexeme> tableEntries = new ArrayList<>();

    public void generateTable(List<Lexeme> lexemes) {
        tableEntries.clear();
        if (lexemes == null) {
            return;
        }

        for (Lexeme lex : lexemes) {
            if (lex == null || lex.text == null || lex.text.trim().isEmpty()) {
                continue;
            }

            TenorLexer.TokenType type = TenorLexer.getTokenType(lex);

            if (type == TenorLexer.TokenType.NEWLINE_DLM
                || type == TenorLexer.TokenType.INDENT_DLM
                || type == TenorLexer.TokenType.DEDENT_DLM
                || type == TenorLexer.TokenType.COMMENT) {
                continue;
            }

            tableEntries.add(lex);
        }
    }

    public List<Lexeme> getEntries() {
        return tableEntries;
    }
}
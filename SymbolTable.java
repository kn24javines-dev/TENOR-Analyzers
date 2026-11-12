import java.util.*;

public class SymbolTable {

    // table entry model
    public static class SymbolEntry {
        private String lexeme;
        private TenorLexer.TokenType tokenType;
        private int line;
        private int col;

        public SymbolEntry(String lexeme, TenorLexer.TokenType tokenType, int line, int col) {
            this.lexeme = lexeme;
            this.tokenType = tokenType;
            this.line = line;
            this.col = col;
        }

        public String getLexeme() {
            return lexeme;
        }

        public TenorLexer.TokenType getTokenType() {
            return tokenType;
        }

        public String getLocation() {
            return line + ":" + col;
        }
    }

    private List<SymbolEntry> tableEntries;

    public SymbolTable() {
        this.tableEntries = new ArrayList<>();
    }

    // table generation from lexeme list
    public void generateTable(List<Lexeme> lexemes) {
        tableEntries.clear();

        if (lexemes == null) {
            return;
        }

        for (Lexeme lex : lexemes) {
            TenorLexer.TokenType type = TenorLexer.getTokenType(lex);
            tableEntries.add(new SymbolEntry(lex.text, type, lex.line, lex.col));
        }
    }

    public List<SymbolEntry> getTableEntries() {
        return tableEntries;
    }
}
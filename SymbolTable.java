import java.util.*;

public class SymbolTable {

    // table entry model
    public static class SymbolEntry {
        private int number;
        private String lexeme;
        private TenorLexer.TokenType tokenType;
        private int line;
        private int col;

        public SymbolEntry(String lexeme, TenorLexer.TokenType tokenType) {
            this(0, lexeme, tokenType, 1, 1);
        }

        public SymbolEntry(int number, String lexeme, TenorLexer.TokenType tokenType, int line, int col) {
            this.number = number;
            this.lexeme = lexeme;
            this.tokenType = tokenType;
            this.line = line;
            this.col = col;
        }

        public int getNumber() {
            return number;
        }

        public String getLexeme() {
            return lexeme;
        }

        public TenorLexer.TokenType getTokenType() {
            return tokenType;
        }

        public int getLine() {
            return line;
        }

        public int getCol() {
            return col;
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

        int count = 1;
        for (Lexeme lex : lexemes) {
            TenorLexer.TokenType type = TenorLexer.getTokenType(lex);
            tableEntries.add(new SymbolEntry(count++, lex.text, type, lex.line, lex.col));
        }
    }

    // table generation from String array
    public void generateTable(String[] lexemes) {
        tableEntries.clear();

        if (lexemes == null) {
            return;
        }

        int count = 1;
        for (String lexeme : lexemes) {
            TenorLexer.TokenType type = TenorLexer.getTokenType(lexeme);
            tableEntries.add(new SymbolEntry(count++, lexeme, type, 1, 1));
        }
    }

    public List<SymbolEntry> getTableEntries() {
        return tableEntries;
    }
}
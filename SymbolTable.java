import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    // stores the individual mapping of a lexeme to its corresponding token type
    public static class SymbolEntry {
        private String lexeme;
        private TenorLexer.TokenType tokenType;

        public SymbolEntry(String lexeme, TenorLexer.TokenType tokenType) {
            this.lexeme = lexeme;
            this.tokenType = tokenType;
        }

        public String getLexeme() {
            return lexeme;
        }

        public TenorLexer.TokenType getTokenType() {
            return tokenType;
        }
    }

    // internal list to hold all the entries generated from the input array
    private List<SymbolEntry> tableEntries;

    public SymbolTable() {
        this.tableEntries = new ArrayList<>();
    }

    // accepts the String[] of raw lexemes, loops through each lexeme, and classifies it using TenorLexer's classification function
    public void generateTable(String[] lexemes) {
        tableEntries.clear(); // clear previous data if re-run

        if (lexemes == null) {
            return;
        }

        for (String lexeme : lexemes) {
            // calls the method to get the token type
            TenorLexer.TokenType type = TenorLexer.getTokenType(lexeme);
            
            // store the result in the symbol table list
            tableEntries.add(new SymbolEntry(lexeme, type));
        }
    }

    public List<SymbolEntry> getTableEntries() {
    return tableEntries;
    }
}
public class Lexeme {
    public final String text;
    public final String type;
    public final int line;
    public final int column;
    
    public Lexeme(String text, String type, int line, int column) {
        this.text = text;
        this.type = type;
        this.line = line;
        this.column = column;
    }
    
    @Override
    public String toString() {
        return String.format("Lexeme('%s', type=%s, line=%d, col=%d)", text, type, line, column);
    }
}
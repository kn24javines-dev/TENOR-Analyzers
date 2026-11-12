public class Lexeme {
    public String text;
    public String type;
    public int line;
    public int col;

    public Lexeme(String text, String type, int line, int col) {
        this.text = text;
        this.type = type;
        this.line = line;
        this.col = col;
    }
}

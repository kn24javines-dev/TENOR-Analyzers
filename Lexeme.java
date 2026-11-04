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

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return text + " [" + type + "] (Line " + line + ", Col " + col + ")";
    }
}

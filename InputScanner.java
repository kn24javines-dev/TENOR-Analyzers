import java.io.*;
import java.util.*;

public class InputScanner {

    // scanner state variables
    private String code;
    private int len;
    private int i;
    private int line;
    private int col;
    private boolean newLine;
    private int openBrackets;
    private ArrayList<Integer> indentList;
    private ArrayList<Lexeme> lexemeList;

    // constructor and file reader
    public InputScanner(String inputCode) {
        this.code = (inputCode != null) ? inputCode : "";
        this.len = this.code.length();
        this.i = 0;
        this.line = 1;
        this.col = 1;
        this.newLine = true;
        this.openBrackets = 0;
        this.indentList = new ArrayList<>();
        this.indentList.add(0);
        this.lexemeList = new ArrayList<>();
    }

    public static String loadFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        FileReader fr = new FileReader(filePath);
        BufferedReader br = new BufferedReader(fr);
        int ch;
        while ((ch = br.read()) != -1) {
            sb.append((char) ch);
        }
        br.close();
        fr.close();
        return sb.toString();
    }

    // character checkers
    private boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    private boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    private boolean isValidWordChar(char c) {
        return isLetter(c) || isNumber(c) || c == '_';
    }

    private boolean isDurationLetter(char c) {
        return c == 'y' || c == 'm' || c == 'w' || c == 'd'
            || c == 'Y' || c == 'M' || c == 'W' || c == 'D';
    }

    // cursor and line/column trackers
    private boolean isEnd() {
        return i >= len;
    }

    private char getChar() {
        if (i < len) {
            return code.charAt(i);
        }
        return '\0';
    }

    private char peekNext(int steps) {
        if (i + steps < len) {
            return code.charAt(i + steps);
        }
        return '\0';
    }

    private char nextChar() {
        if (i >= len) {
            return '\0';
        }
        char c = code.charAt(i);
        i++;

        if (c == '\n') {
            line++;
            col = 1;
            newLine = true;
        } else if (c != '\r') {
            col++;
        }
        return c;
    }

    // 4-space indentation check
    private void checkIndent() {
        int startLine = line;
        int startCol = col;
        int spaces = 0;
        boolean usedTab = false;

        while (!isEnd()) {
            char c = getChar();
            if (c == ' ') {
                spaces++;
                nextChar();
            } else if (c == '\t') {
                usedTab = true;
                spaces += 4;
                nextChar();
            } else if (c == '\r') {
                nextChar();
            } else {
                break;
            }
        }

        // skip blank or comment lines
        char next = getChar();
        if (next == '\n' || next == '#' || next == '\0') {
            return;
        }

        if (openBrackets > 0) {
            newLine = false;
            return;
        }

        newLine = false;

        if (usedTab) {
            lexemeList.add(new Lexeme("\\t", "ERROR", startLine, startCol));
        }

        if (spaces % 4 != 0) {
            lexemeList.add(new Lexeme("<BAD_INDENT:" + spaces + ">", "ERROR", startLine, 1));
            return;
        }

        int currentIndent = indentList.get(indentList.size() - 1);

        if (spaces > currentIndent) {
            while (spaces > indentList.get(indentList.size() - 1)) {
                int nextLevel = indentList.get(indentList.size() - 1) + 4;
                indentList.add(nextLevel);
                lexemeList.add(new Lexeme("<INDENT:4>", "INDENT", startLine, 1));
            }
        } else if (spaces < currentIndent) {
            while (indentList.size() > 1 && spaces < indentList.get(indentList.size() - 1)) {
                indentList.remove(indentList.size() - 1);
                lexemeList.add(new Lexeme("<DEDENT:4>", "DEDENT", startLine, 1));
            }
        }
    }

    // comment and string readers
    private Lexeme skipComment() {
        int startLine = line;
        int startCol = col;
        StringBuilder sb = new StringBuilder();
        while (!isEnd() && getChar() != '\n' && getChar() != '\r') {
            sb.append(nextChar());
        }
        return new Lexeme(sb.toString(), "COMMENT", startLine, startCol);
    }

    private Lexeme readString(String prefix) {
        int startLine = line;
        int startCol = col - prefix.length();
        char quote = nextChar();
        StringBuilder word = new StringBuilder(prefix);
        word.append(quote);
        boolean closed = false;

        while (!isEnd()) {
            char c = getChar();
            if (c == '\\') {
                word.append(nextChar());
                if (!isEnd()) {
                    word.append(nextChar());
                }
                continue;
            }
            if (c == quote) {
                word.append(nextChar());
                closed = true;
                break;
            }
            if (c == '\n' || c == '\r') {
                break;
            }
            word.append(nextChar());
        }

        if (!closed) {
            return new Lexeme(word.toString(), "ERROR", startLine, startCol);
        }
        return new Lexeme(word.toString(), "STRING", startLine, startCol);
    }

    // word and identifier reader
    private Lexeme readWord() {
        int startLine = line;
        int startCol = col;

        // f-string prefix check
        if ((getChar() == 'f' || getChar() == 'F') && (peekNext(1) == '"' || peekNext(1) == '\'')) {
            String prefix = String.valueOf(nextChar());
            return readString(prefix);
        }

        StringBuilder word = new StringBuilder();
        int state = 1;

        while (!isEnd()) {
            char c = getChar();
            if (!isValidWordChar(c)) {
                break;
            }

            switch (state) {
                case 1:
                    if (c == '_') {
                        word.append(nextChar());
                    } else if (isLetter(c)) {
                        word.append(nextChar());
                        state = 3;
                    } else {
                        word.append(nextChar());
                        state = -1;
                    }
                    break;

                case 3:
                    word.append(nextChar());
                    break;

                case -1:
                    word.append(nextChar());
                    break;
            }
        }

        String text = word.toString();

        if (text.equals("_")) {
            return new Lexeme(text, "WILDCARD", startLine, startCol);
        }

        if (state != 3) {
            return new Lexeme(text, "ERROR", startLine, startCol);
        }
        return new Lexeme(text, "WORD", startLine, startCol);
    }

    // number and duration reader
    private Lexeme readNumber() {
        int startLine = line;
        int startCol = col;
        StringBuilder num = new StringBuilder();
        int state = 1;

        while (!isEnd()) {
            char c = getChar();

            switch (state) {
                case 1: // integer part
                    if (isNumber(c)) {
                        num.append(nextChar());
                    } else if (c == '_') {
                        num.append(nextChar());
                        state = 2;
                    } else if (c == '.' && isNumber(peekNext(1))) {
                        num.append(nextChar());
                        state = 3;
                    } else if (isDurationLetter(c) && !isValidWordChar(peekNext(1))) {
                        num.append(nextChar());
                        state = 6;
                    } else if (isLetter(c)) {
                        num.append(nextChar());
                        state = -1;
                    } else {
                        return new Lexeme(num.toString(), "INT", startLine, startCol);
                    }
                    break;

                case 2: // underscore in integer
                    if (isNumber(c)) {
                        num.append(nextChar());
                        state = 1;
                    } else {
                        num.append(nextChar());
                        state = -1;
                    }
                    break;

                case 3: // decimal point
                    if (isNumber(c)) {
                        num.append(nextChar());
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;

                case 4: // float part
                    if (isNumber(c)) {
                        num.append(nextChar());
                    } else if (c == '_') {
                        num.append(nextChar());
                        state = 5;
                    } else if (isDurationLetter(c) && !isValidWordChar(peekNext(1))) {
                        num.append(nextChar());
                        state = 6;
                    } else if (isLetter(c) || c == '.') {
                        num.append(nextChar());
                        state = -1;
                    } else {
                        return new Lexeme(num.toString(), "FLOAT", startLine, startCol);
                    }
                    break;

                case 5: // underscore in float
                    if (isNumber(c)) {
                        num.append(nextChar());
                        state = 4;
                    } else {
                        num.append(nextChar());
                        state = -1;
                    }
                    break;

                case 6: // duration suffix
                    return new Lexeme(num.toString(), "DURATION", startLine, startCol);

                case -1:
                    if (isValidWordChar(c) || c == '.') {
                        num.append(nextChar());
                    } else {
                        return new Lexeme(num.toString(), "ERROR", startLine, startCol);
                    }
                    break;
            }
        }

        if (state == 1) return new Lexeme(num.toString(), "INT", startLine, startCol);
        if (state == 4) return new Lexeme(num.toString(), "FLOAT", startLine, startCol);
        if (state == 6) return new Lexeme(num.toString(), "DURATION", startLine, startCol);
        return new Lexeme(num.toString(), "ERROR", startLine, startCol);
    }

    // operators and delimiters reader
    private Lexeme readSymbol() {
        int startLine = line;
        int startCol = col;
        char c = nextChar();
        char next = getChar();

        switch (c) {
            case '*':
                if (next == '*') {
                    nextChar();
                    if (getChar() == '=') {
                        nextChar();
                        return new Lexeme("**=", "OPERATOR", startLine, startCol);
                    }
                    return new Lexeme("**", "OPERATOR", startLine, startCol);
                } else if (next == '=') {
                    nextChar();
                    return new Lexeme("*=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("*", "OPERATOR", startLine, startCol);

            case '/':
                if (next == '/') {
                    nextChar();
                    if (getChar() == '=') {
                        nextChar();
                        return new Lexeme("//=", "OPERATOR", startLine, startCol);
                    }
                    return new Lexeme("//", "OPERATOR", startLine, startCol);
                } else if (next == '=') {
                    nextChar();
                    return new Lexeme("/=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("/", "OPERATOR", startLine, startCol);

            case '<':
                if (next == '<') {
                    nextChar();
                    if (getChar() == '=') {
                        nextChar();
                        return new Lexeme("<<=", "OPERATOR", startLine, startCol);
                    }
                    return new Lexeme("<<", "OPERATOR", startLine, startCol);
                } else if (next == '=') {
                    nextChar();
                    return new Lexeme("<=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("<", "OPERATOR", startLine, startCol);

            case '>':
                if (next == '>') {
                    nextChar();
                    if (getChar() == '=') {
                        nextChar();
                        return new Lexeme(">>=", "OPERATOR", startLine, startCol);
                    }
                    return new Lexeme(">>", "OPERATOR", startLine, startCol);
                } else if (next == '=') {
                    nextChar();
                    return new Lexeme(">=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme(">", "OPERATOR", startLine, startCol);

            case '^':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("^=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("^", "OPERATOR", startLine, startCol);

            case '~':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("~=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("~", "OPERATOR", startLine, startCol);

            case '&':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("&=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("&", "OPERATOR", startLine, startCol);

            case '|':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("|=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("|", "OPERATOR", startLine, startCol);

            case '+':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("+=", "OPERATOR", startLine, startCol);
                } else if (next == '+') {
                    nextChar();
                    return new Lexeme("++", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("+", "OPERATOR", startLine, startCol);

            case '-':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("-=", "OPERATOR", startLine, startCol);
                } else if (next == '-') {
                    nextChar();
                    return new Lexeme("--", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("-", "OPERATOR", startLine, startCol);

            case '%':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("%=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("%", "OPERATOR", startLine, startCol);

            case '=':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("==", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("=", "OPERATOR", startLine, startCol);

            case '!':
                if (next == '=') {
                    nextChar();
                    return new Lexeme("!=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme("!", "ERROR", startLine, startCol);

            case '@':
                return new Lexeme("@", "OPERATOR", startLine, startCol);

            case ':':
                if (next == '=') {
                    nextChar();
                    return new Lexeme(":=", "OPERATOR", startLine, startCol);
                }
                return new Lexeme(":", "DELIMITER", startLine, startCol);

            case '(':
                openBrackets++;
                return new Lexeme("(", "DELIMITER", startLine, startCol);
            case ')':
                if (openBrackets > 0) openBrackets--;
                return new Lexeme(")", "DELIMITER", startLine, startCol);
            case '[':
                openBrackets++;
                return new Lexeme("[", "DELIMITER", startLine, startCol);
            case ']':
                if (openBrackets > 0) openBrackets--;
                return new Lexeme("]", "DELIMITER", startLine, startCol);
            case '{':
                openBrackets++;
                return new Lexeme("{", "DELIMITER", startLine, startCol);
            case '}':
                if (openBrackets > 0) openBrackets--;
                return new Lexeme("}", "DELIMITER", startLine, startCol);
            case ',':
            case '.':
            case ';':
                return new Lexeme(String.valueOf(c), "DELIMITER", startLine, startCol);

            default:
                return new Lexeme(String.valueOf(c), "ERROR", startLine, startCol);
        }
    }

    // main scanner loop
    public ArrayList<Lexeme> scanCode() {
        lexemeList.clear();

        while (!isEnd()) {
            if (newLine && openBrackets == 0) {
                checkIndent();
                if (isEnd()) break;
            }

            char c = getChar();

            switch (c) {
                case '\r':
                case '\n':
                    int nlLine = line;
                    int nlCol = col;
                    nextChar();
                    if (openBrackets == 0) {
                        lexemeList.add(new Lexeme("\\n", "NEWLINE", nlLine, nlCol));
                    }
                    break;

                case ' ':
                case '\t':
                    nextChar();
                    break;

                case '#':
                    lexemeList.add(skipComment());
                    break;

                case '"':
                case '\'':
                    lexemeList.add(readString(""));
                    break;

                default:
                    if (isLetter(c) || c == '_') {
                        lexemeList.add(readWord());
                    } else if (isNumber(c)) {
                        lexemeList.add(readNumber());
                    } else {
                        lexemeList.add(readSymbol());
                    }
                    break;
            }
        }

        // close remaining indented blocks
        while (indentList.size() > 1) {
            indentList.remove(indentList.size() - 1);
            lexemeList.add(new Lexeme("<DEDENT:4>", "DEDENT", line, col));
        }

        return lexemeList;
    }

}
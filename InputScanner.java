import java.util.ArrayList;
import java.util.List;

public class InputScanner {
    public static List<Lexeme> scan(String input) {
        List<Lexeme> lexemes = new ArrayList<>();
        if (input == null || input.isEmpty()) return lexemes;

        int index = 0;
        int length = input.length();
        int line = 1;
        int column = 1;

        List<Integer> indentStack = new ArrayList<>();
        indentStack.add(0);
        boolean atLineStart = true;

        while (index < length) {
            char c = input.charAt(index);

            if (atLineStart) {
                if (c == '\n' || c == '\r') {
                    if (c == '\r' && index + 1 < length && input.charAt(index + 1) == '\n') {
                        index++; }
                    index++;
                    line++;
                    column = 1;
                    continue;
                }

                if (c == ' ' || c == '\t') {
                    int currentIndent = 0;
                    int startCol = column;
                    
                    while (index < length && (input.charAt(index) == ' ' || input.charAt(index) == '\t')) {
                        currentIndent += (input.charAt(index) == '\t') ? 4 : 1;
                        index++;
                        column++;
                    }

                    if (index < length && input.charAt(index) != '\n' && input.charAt(index) != '\r' && input.charAt(index) != '#') {
                        int prevIndent = indentStack.get(indentStack.size() - 1);
                        if (currentIndent > prevIndent) {
                            indentStack.add(currentIndent);
                            lexemes.add(new Lexeme("INDENT", "INDENT", line, startCol));
                        } else if (currentIndent < prevIndent) {
                            while (indentStack.size() > 1 && indentStack.get(indentStack.size() - 1) > currentIndent) {
                                indentStack.remove(indentStack.size() - 1); lexemes.add(new Lexeme("DEDENT", "DEDENT", line, startCol));
                            }
                        }
                    }
                    atLineStart = false;
                    continue;
                } else {
                    int prevIndent = indentStack.get(indentStack.size() - 1);
                    if (prevIndent > 0) {
                        while (indentStack.size() > 1 && indentStack.get(indentStack.size() - 1) > 0) {
                            indentStack.remove(indentStack.size() - 1);
                            lexemes.add(new Lexeme("DEDENT", "DEDENT", line, column));
                        }
                    }
                    atLineStart = false;
                }
            }

            if (c == ' ' || c == '\t') {
                index++;
                column++;
                continue;
            }
            
            if (c == '\n' || c == '\r') {
                lexemes.add(new Lexeme("\\n", "NEWLINE", line, column));
                if (c == '\r' && index + 1 < length && input.charAt(index + 1) == '\n') {
                    index++;
                }
                index++;
                line++;
                column = 1;
                atLineStart = true;
                continue;
            }

            if (c == '#') {
                int startCol = column;
                StringBuilder sb = new StringBuilder();
                while (index < length && input.charAt(index) != '\n' && input.charAt(index) != '\r') {
                    sb.append(input.charAt(index));
                    index++;
                    column++;
                }
                lexemes.add(new Lexeme(sb.toString(), "COMMENT", line, startCol));
                continue;
            }
            
            boolean isFString = (c == 'f' || c == 'F')
                && index + 1 < length
                && (input.charAt(index + 1) == '\"' || input.charAt(index + 1) == '\'');

            if (c == '\"' || c == '\'' || isFString) {
                if (isFString) {
                    lexemes.add(new Lexeme(Character.toString(c), "FSTRING_PREFIX", line, column));
                    index++;
                    column++;
                    c = input.charAt(index);
                }

                char quoteChar = c;

                lexemes.add(new Lexeme(Character.toString(quoteChar), "DLM", line, column));
                index++;
                column++;

                int textStartCol = column;
                StringBuilder innerText = new StringBuilder();
                boolean hasClosingQuote = false;

                while (index < length) {
                    char ch = input.charAt(index);
                    if (ch == quoteChar) {
                        hasClosingQuote = true;
                        break;
                    }
                    if (ch == '\n' || ch == '\r') {
                        break;
                    }
                    innerText.append(ch);
                    index++;
                    column++;
                }

                if (innerText.length() > 0) {
                    lexemes.add(new Lexeme(innerText.toString(), "STRING", line, textStartCol));
                }

                if (hasClosingQuote) {
                    lexemes.add(new Lexeme(Character.toString(quoteChar), "DLM", line, column));
                    index++;
                    column++;
                }

                continue;
            }

            if (Character.isDigit(c)) {
                int startCol = column;
                StringBuilder sb = new StringBuilder();
                boolean isFloat = false;
                
                while (index < length) {
                    char ch = input.charAt(index);
                    if (Character.isDigit(ch) || ch == '_') {
                        sb.append(ch);
                        index++;
                        column++;
                    } else if (ch == '.' && !isFloat && index + 1 < length && Character.isDigit(input.charAt(index + 1))) {
                        isFloat = true;
                        sb.append(ch);
                        index++; column++;
                    } else {
                        break;
                    }
                }
                
                lexemes.add(new Lexeme(sb.toString(), isFloat ? "FLOAT" : "INT", line, startCol));
                continue;
            }

            if (Character.isLetter(c) || c == '_') {
                int startCol = column;
                StringBuilder sb = new StringBuilder();
                
                while (index < length && (Character.isLetterOrDigit(input.charAt(index)) || input.charAt(index) == '_')) {
                    sb.append(input.charAt(index));
                    index++;
                    column++;
                } 
                
                lexemes.add(new Lexeme(sb.toString(), "WORD", line, startCol));
                continue;
            }

            int startCol = column;
            String opMatch = matchOperator(input, index);
            if (opMatch != null) {
                lexemes.add(new Lexeme(opMatch, "OPERATOR", line, startCol));
                index += opMatch.length();
                column += opMatch.length();
                continue;
            }

            lexemes.add(new Lexeme(Character.toString(c), "WILDCARD", line, column));
            index++;
            column++;
        }

        while (indentStack.size() > 1) {
            indentStack.remove(indentStack.size() - 1);
            lexemes.add(new Lexeme("DEDENT", "DEDENT", line, column));
        }
        return lexemes;
    }

    private static String matchOperator(String input, int index) {
        String[] operators = { "//=", "\\=", "<<=", ">>=", "+=", "-=", "*=", "/=", "%=", "^=", "~=", "&=", "|=", "++", "--", "<<", ">>", "==", "!=", "<=", ">=", "\\",
                            "//", "=", "+", "-", "*", "/", "%", "^", "~", "&", "|", "@", "(", ")", "[", "]", "{", "}", ",", ":", ".", ";" };
        
        for (String op : operators) {
            if (input.startsWith(op, index)) {
                return op;
            }
        }
        return null;
    }
}
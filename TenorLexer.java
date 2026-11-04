public class TenorLexer {

    public enum TokenType {
        KEYWORD,
        RESERVED_WORD,
        DATA_TYPE,
        OPERATOR_ASSIGNMENT,
        OPERATOR_ARITHMETIC,
        OPERATOR_TEMPORAL,
        OPERATOR_LOGICAL,
        OPERATOR_RELATIONAL,
        OPERATOR_BINDING,
        OPERATOR_UNARY,
        DELIMITER,
        IDENTIFIER,
        LITERAL_INTEGER,
        LITERAL_FLOAT,
        LITERAL_DURATION,
        LITERAL_STRING,
        INDENT_BLOCK,
        DEDENT_BLOCK,
        WILDCARD,
        COMMENT,
        LEXICAL_ERROR,
        UNKNOWN
    }

    // Predefined words
    private static final String[] KEYWORDS = {
        "match", "case", "from", "to",
        "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN",
        "BAM", "BBD", "BDT", "BGN", "BHD", "BIF", "BMD", "BND", "BOB", "BRL",
        "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY",
        "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP",
        "ERN", "ETB", "EUR", "FJD", "FKP", "GBP", "GEL", "GHS", "GIP", "GMD",
        "GNF", "GTQ", "GYD", "HKD", "HNL", "HTG", "HUF", "IDR", "ILS", "INR",
        "IQD", "IRR", "ISK", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF",
        "KPW", "KRW", "KWD", "KYD", "KZT", "LAK", "LBP", "LKR", "LRD", "LSL",
        "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR",
        "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR",
        "NZD", "OMR", "PAB", "PEN", "PGK", "PHP", "PKR", "PLN", "PYG", "QAR",
        "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD",
        "SHP", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS",
        "TMT", "TND", "TOP", "TRY", "TTD", "TWD", "TZS", "UAH", "UGX", "USD",
        "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XOF", "XPF",
        "YER", "ZAR", "ZMW", "ZWL"
    };

    private static final String[] RESERVED_WORDS = {
        "entity", "link", "def", "let", "if", "else", "elif", "for", "in", "while",
        "return", "policy", "manifest", "source", "trace", "raise", "break",
        "continue", "pass", "try", "except", "finally", "with", "as", "yield",
        "lambda", "import", "del", "assert", "global", "nonlocal",
        "validate", "outgoing_links", "incoming_links", "print", "input", "range",
        "True", "False", "true", "false"
    };

    private static final String[] DATA_TYPES = {
        "int", "float", "bool", "string", "list", "dict", "money", "duration"
    };

    // Operators and delimiters
    private static final String[] ASSIGNMENT_OPERATORS = {
        "=", "+=", "-=", "*=", "/=", "//=", "%=", "**=", "^=", "~=", "&=", "|=", "<<=", ">>=", ":="
    };

    private static final String[] ARITHMETIC_OPERATORS = {
        "+", "-", "*", "/", "//", "%", "**", "&", "^", "~", "|"
    };

    private static final String[] UNARY_OPERATORS = {
        "++", "--"
    };

    private static final String[] TEMPORAL_OPERATORS = {
        "<<", ">>"
    };

    private static final String[] LOGICAL_OPERATORS = {
        "and", "or", "not"
    };

    private static final String[] RELATIONAL_OPERATORS = {
        "==", "!=", "<", ">", "<=", ">="
    };

    private static final String[] BINDING_OPERATOR = {
        "@"
    };

    private static final String[] DELIMITERS = {
        "(", ")", "[", "]", "{", "}", ",", ":", ".", ";", "'", "\""
    };

    private static boolean contains(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    // Token classification
    public static TokenType getTokenType(Lexeme lexeme) {
        if (lexeme == null) {
            return TokenType.UNKNOWN;
        }
        if ("ERROR".equals(lexeme.type)) {
            return TokenType.LEXICAL_ERROR;
        }
        if ("INDENT".equals(lexeme.type)) {
            return TokenType.INDENT_BLOCK;
        }
        if ("DEDENT".equals(lexeme.type)) {
            return TokenType.DEDENT_BLOCK;
        }
        if ("WILDCARD".equals(lexeme.type)) {
            return TokenType.WILDCARD;
        }
        if ("DURATION".equals(lexeme.type)) {
            return TokenType.LITERAL_DURATION;
        }
        return getTokenType(lexeme.text);
    }

    public static TokenType getTokenType(String lexeme) {
        if (lexeme == null || lexeme.length() == 0) {
            return TokenType.UNKNOWN;
        }

        // Indentation and scanner tokens
        if (lexeme.equals("<INDENT:4>")) {
            return TokenType.INDENT_BLOCK;
        }
        if (lexeme.equals("<DEDENT:4>")) {
            return TokenType.DEDENT_BLOCK;
        }
        if (lexeme.startsWith("<BAD_INDENT:") || lexeme.equals("\\t")) {
            return TokenType.LEXICAL_ERROR;
        }
        if (lexeme.equals("_")) {
            return TokenType.WILDCARD;
        }

        // Comments
        if (lexeme.charAt(0) == '#') {
            return TokenType.COMMENT;
        }

        // Predefined words
        if (contains(LOGICAL_OPERATORS, lexeme)) return TokenType.OPERATOR_LOGICAL;
        if (contains(DATA_TYPES, lexeme)) return TokenType.DATA_TYPE;
        if (contains(RESERVED_WORDS, lexeme)) return TokenType.RESERVED_WORD;
        if (contains(KEYWORDS, lexeme)) return TokenType.KEYWORD;

        // Predefined symbols
        if (contains(ASSIGNMENT_OPERATORS, lexeme)) return TokenType.OPERATOR_ASSIGNMENT;
        if (contains(UNARY_OPERATORS, lexeme)) return TokenType.OPERATOR_UNARY;
        if (contains(ARITHMETIC_OPERATORS, lexeme)) return TokenType.OPERATOR_ARITHMETIC;
        if (contains(TEMPORAL_OPERATORS, lexeme)) return TokenType.OPERATOR_TEMPORAL;
        if (contains(RELATIONAL_OPERATORS, lexeme)) return TokenType.OPERATOR_RELATIONAL;
        if (contains(BINDING_OPERATOR, lexeme)) return TokenType.OPERATOR_BINDING;
        if (contains(DELIMITERS, lexeme)) return TokenType.DELIMITER;

        // User-defined values and literals
        if (isStringLiteral(lexeme)) return TokenType.LITERAL_STRING;
        if (isIdentifier(lexeme)) return TokenType.IDENTIFIER;
        if (isInteger(lexeme)) return TokenType.LITERAL_INTEGER;
        if (isFloat(lexeme)) return TokenType.LITERAL_FLOAT;
        if (isDuration(lexeme)) return TokenType.LITERAL_DURATION;

        return TokenType.UNKNOWN;
    }

    // Literal and identifier validators
    private static boolean isIdentifier(String str) {
        int state = 1;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (state == 1) {
                if (c == '_') {
                    state = 1;
                } else if (isLetter(c)) {
                    state = 3;
                } else {
                    return false;
                }
            } else if (state == 3) {
                if (!isLetter(c) && !isDigit(c) && c != '_') {
                    return false;
                }
            }
        }
        return state == 3;
    }

    private static boolean isInteger(String str) {
        if (str.length() == 0 || !isDigit(str.charAt(0))) {
            return false;
        }
        boolean prevUnderscore = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (isDigit(c)) {
                prevUnderscore = false;
            } else if (c == '_' && !prevUnderscore) {
                prevUnderscore = true;
            } else {
                return false;
            }
        }
        return !prevUnderscore;
    }

    private static boolean isFloat(String str) {
        if (str.length() < 3 || !isDigit(str.charAt(0))) {
            return false;
        }
        boolean hasDecimal = false;
        boolean prevUnderscore = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.') {
                if (hasDecimal || prevUnderscore || i == str.length() - 1) return false;
                hasDecimal = true;
            } else if (isDigit(c)) {
                prevUnderscore = false;
            } else if (c == '_' && !prevUnderscore) {
                prevUnderscore = true;
            } else {
                return false;
            }
        }
        return hasDecimal && !prevUnderscore;
    }

    private static boolean isDuration(String str) {
        if (str.length() < 2) {
            return false;
        }
        char last = str.charAt(str.length() - 1);
        if (last != 'y' && last != 'm' && last != 'w' && last != 'd'
                && last != 'Y' && last != 'M' && last != 'W' && last != 'D') {
            return false;
        }
        String numberPart = str.substring(0, str.length() - 1);
        return isInteger(numberPart) || isFloat(numberPart);
    }

    private static boolean isStringLiteral(String str) {
        if (str.length() >= 3 && (str.charAt(0) == 'f' || str.charAt(0) == 'F')) {
            str = str.substring(1);
        }
        if (str.length() < 2) {
            return false;
        }
        char first = str.charAt(0);
        char last = str.charAt(str.length() - 1);
        return (first == '"' && last == '"') || (first == '\'' && last == '\'');
    }

    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static void main(String[] args) {
        String sampleCode =
            "# TENOR Live Presentation Showcase (COSC 303)\n" +
            "entity Bank\n" +
            "    capital: money\n" +
            "    term: duration\n" +
            "\n" +
            "let bdo_capital = 450_000_000_000 PHP @source(\"BSP_Registry\")\n" +
            "let loan_rate = 0.045\n" +
            "loan_rate ^= 0.005\n" +
            "settle_date <<= 2\n";

        InputScanner scanner = new InputScanner(sampleCode);
        java.util.ArrayList<Lexeme> lexemes = scanner.scanCode();

        System.out.println(String.format("%-4s | %-18s | %-22s | %s", "No.", "Lexeme", "Token Type", "Line:Col"));
        System.out.println("-------------------------------------------------------------------");
        int count = 1;
        for (Lexeme lex : lexemes) {
            TokenType type = getTokenType(lex);
            System.out.println(String.format("%-4d | %-18s | %-22s | %d:%d",
                count++, lex.text, type, lex.line, lex.col));
        }
    }
}

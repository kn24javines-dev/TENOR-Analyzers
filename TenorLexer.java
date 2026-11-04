public class TenorLexer{

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
        DELIMITER,
        IDENTIFIER,
        LITERAL_INTEGER,
        LITERAL_FLOAT,
        LITERAL_STRING,
        COMMENT,
        UNKNOWN
    }

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
        "entity", "link", "def", "let", "if", "else", "return", 
        "policy", "manifest", "source", "trace", "raise", "break", "continue", "pass",
        "with", "yield", "lambda", "import", "del", "assert", "link", "validate", "outgoing_links", "incoming_links", "let"
    };

    private static final String[] DATA_TYPES = {
        "int", "float", "bool", "string", "list", "dict", "money", "duration"
    };

    // --- OPERATORS ---

    private static final String[] ASSIGNMENT_OPERATORS = {
        "=", "+=", "-=", "*=", "/=", "//=", "%=", "**=", "^=", "~=", "&=", "<<=", ">>="
    };

    private static final String[] ARITHMETIC_OPERATORS = {
        "+", "-", "*", "/", "//", "%", "**", "&", "^", "~"
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

    // ---------------------------------------

    private static final String[] DELIMITERS = {
        "(", ")", "[", "]", "{", "}", ",", ":", ".", "'", "\""
    };

    private static boolean contains(String[] array, String target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(target)) {
                return true;
            }
        }
        return false;
    }

    public static TokenType getTokenType(String lexeme) {
        if (lexeme == null || lexeme.length() == 0) {
            return TokenType.UNKNOWN;
        }

        //Checks for Comments
        if (lexeme.charAt(0) == '#') {
            return TokenType.COMMENT;
        }

        //Checks for predefined words
        if (contains(DATA_TYPES, lexeme)) return TokenType.DATA_TYPE;
        if (contains(RESERVED_WORDS, lexeme)) return TokenType.RESERVED_WORD;
        if (contains(KEYWORDS, lexeme)) return TokenType.KEYWORD;
        
        //Checks for predefined symbols
        if (contains(ASSIGNMENT_OPERATORS, lexeme)) return TokenType.OPERATOR_ASSIGNMENT;
        if (contains(ARITHMETIC_OPERATORS, lexeme)) return TokenType.OPERATOR_ARITHMETIC;
        if (contains(TEMPORAL_OPERATORS, lexeme)) return TokenType.OPERATOR_TEMPORAL;
        if (contains(LOGICAL_OPERATORS, lexeme)) return TokenType.OPERATOR_LOGICAL;
        if (contains(RELATIONAL_OPERATORS, lexeme)) return TokenType.OPERATOR_RELATIONAL;
        if (contains(BINDING_OPERATOR, lexeme)) return TokenType.OPERATOR_BINDING;
        if (contains(DELIMITERS, lexeme)) return TokenType.DELIMITER;

        //Checks for user defined values
        if (isStringLiteral(lexeme)) return TokenType.LITERAL_STRING;
        if (isIdentifier(lexeme)) return TokenType.IDENTIFIER;
        if (isInteger(lexeme)) return TokenType.LITERAL_INTEGER;
        if (isFloat(lexeme)) return TokenType.LITERAL_FLOAT;

        //If not identified return unknown
        return TokenType.UNKNOWN;
    }

    private static boolean isIdentifier(String str) {
        char first = str.charAt(0);
        if (!isLetter(first) && first != '_') return false;
        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!isLetter(c) && !isDigit(c) && c != '_') return false;
        }
        return true;
    }

    private static boolean isInteger(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!isDigit(str.charAt(i))) return false;
        }
        return true;
    }

    private static boolean isFloat(String str) {
        boolean hasDecimal = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.') {
                if (hasDecimal) return false; 
                hasDecimal = true;
            } else if (!isDigit(c)) {
                return false; 
            }
        }
        return hasDecimal && str.length() > 1; 
    }

    private static boolean isStringLiteral(String str) {
        if (str.length() < 2) {
            return false;
        }
        char first = str.charAt(0);
        char last = str.charAt(str.length() - 1);
        
        // Checks if it starts and ends with " or '
        return (first == '"' && last == '"') || (first == '\'' && last == '\'');
    }

    private static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static void main(String[] args) {
        String[] testLexemes = {
            "USD",          // KEYWORD
            "+=",           // OPERATOR_ASSIGNMENT
            "**",           // OPERATOR_ARITHMETIC
            "<<",           // OPERATOR_TEMPORAL
            "not",          // OPERATOR_LOGICAL
            "!=",           // OPERATOR_RELATIONAL
            "@",             // OPERATOR_BINDING
            "@source"
        };

        System.out.println("Lexeme          | Token Type");
        System.out.println("--------------------------------------------------");
        for (int i = 0; i < testLexemes.length; i++) {
            String lexeme = testLexemes[i];
            TokenType type = getTokenType(lexeme);
            System.out.println(String.format("%-15s | %s", lexeme, type));
        }
    }
}

public class TenorLexer {

    // all token types
    public enum TokenType {
        INT_DT, FLOAT_DT, BOOL_DT, STRING_DT, LIST_DT, DICT_DT, MONEY_DT, DURATION_DT, DATE_DT, // data types

        ASSIGN, ADD_ASSIGN, SUB_ASSIGN, MUL_ASSIGN, DIV_ASSIGN, FLRDIV_ASSIGN, MOD_ASSIGN, EXP_ASSIGN, UNION_ASSIGN,
        STEPUP_ASSIGN, STEPDOWN_ASSIGN, BPS_ASSIGN, DSHIFTLEFT_ASSIGN, DSHIFTRIGHT_ASSIGN, // assignment operators

        PLUS_OP, MINUS_OP, MULTIPLY_OP, DIVIDE_OP, FLRDIV_OP, MODULO_OP, POWER_OP, BPS_OP, STEPUP_OP, STEPDOWN_OP, // arithmetic operators
        
        DATE_SHIFT_LEFT_OP, DATE_SHIFT_RIGHT_OP, // temporal operators

        UNARY_PLUS, UNARY_MINUS, INCREMENT_OP, DECREMENT_OP, // unary operators
        
        AND, OR, NOT, // logical operators

        EQUALS, NOT_EQUALS, LESS_THAN, GREATER_THAN, LESSTHAN_OR_EQUAL, GREATERTHAN_OR_EQUAL, // relational operators

        BIND_OP, // binding operator

        MATCH_KW, CASE_KW, FROM_KW, TO_KW, CURRENCY_LITERAL, // keywords
        
        ENTITY_RW, LINK_RW, DEF_RW, LET_RW, IF_RW, ELSE_RW, FOR_RW, WHILE_RW, RETURN_RW, POLICY_RW, MANIFEST_RW, SOURCE_RW, TRACE_RW,
        TRY_RW, EXCEPT_RW, FINALLY_RW, RAISE_RW, BREAK_RW, CONTINUE_RW, PASS_RW, WITH_RW, YIELD_RW, LAMBDA_RW, IMPORT_RW, AS_RW, 
        DEL_RW, ASSERT_RW, VALIDATE_RW, OUTGOING_LINKS_RW, INCOMING_LINKS_RW, PRINT_RW, // reserved words
        
        COMMENT, // comments

        LPARENTHESIS, RPARENTHESIS, LBRACKET, RBRACKET, LBRACE, RBRACE, COMMA, COLON, SEMICOLON, DOT, STRING_LITERAL,
        NEWLINE, INDENT, DEDENT, // blanks and delimeters

        IDENTIFIER, INT_LITERAL, FLOAT_LITERAL, BOOL_LITERAL, MONEY_LITERAL, DURATION_LITERAL, DATE_LITERAL, // literals

        WILDCARD, LEXICAL_ERROR, INVALID_TOKEN
    }

    // predefined words and structural terms
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
        "entity", "link", "def", "let", "if", "else", "for", "while",
        "return", "policy", "manifest", "source", "trace", "raise", "break",
        "continue", "pass", "try", "except", "finally", "with", "as", "yield",
        "lambda", "import", "del", "assert",
        "validate", "outgoing_links", "incoming_links",
        "True", "False", "true", "false", "print"
    };

    private static final String[] DATA_TYPES = {
        "int", "float", "bool", "string", "list", "dict", "money", "duration", "date"
    };

    // operators and delimiters
    private static final String[] ASSIGNMENT_OPERATORS = {
        "=", "+=", "-=", "*=", "/=", "//=", "%=", "**=", "^=", "~=", "&=", "|=", "<<=", ">>="
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
    
    // token classification
    public static TokenType getTokenType(Lexeme lexeme) {
        if (lexeme == null) {
            return TokenType.INVALID_TOKEN;
        }
        if ("ERROR".equals(lexeme.type)) {
            return TokenType.LEXICAL_ERROR;
        }
        if ("INDENT".equals(lexeme.type)) {
            return TokenType.INDENT;
        }
        if ("DEDENT".equals(lexeme.type)) {
            return TokenType.DEDENT;
        }
        if ("NEWLINE".equals(lexeme.type)) {
            return TokenType.NEWLINE;
        }
        if ("WILDCARD".equals(lexeme.type)) {
            return TokenType.WILDCARD;
        }
        if ("DURATION".equals(lexeme.type)) {
            return TokenType.DURATION_LITERAL;
        }
        return getTokenType(lexeme.text);
    }

    public static TokenType getTokenType(String lexeme) {
        if (lexeme == null || lexeme.length() == 0) {
            return TokenType.INVALID_TOKEN;
        }

        // indentation and scanner tokens
        if (lexeme.equals("<INDENT:4>")) {
            return TokenType.INDENT;
        }
        if (lexeme.equals("<DEDENT:4>")) {
            return TokenType.DEDENT;
        }
        if (lexeme.equals("\\n")) {
            return TokenType.NEWLINE;
        }
        if (lexeme.startsWith("<BAD_INDENT:") || lexeme.equals("\\t")) {
            return TokenType.LEXICAL_ERROR;
        }
        if (lexeme.equals("_")) {
            return TokenType.WILDCARD;
        }

        // comments
        if (lexeme.charAt(0) == '#') {
            return TokenType.COMMENT;
        }

        // predefined words and types
        if (contains(LOGICAL_OPERATORS, lexeme)) {
            if (lexeme.equals("and")) return TokenType.AND;
            if (lexeme.equals("or")) return TokenType.OR;
            if (lexeme.equals("not")) return TokenType.NOT;
        }
        if (contains(DATA_TYPES, lexeme)) {
            switch (lexeme) {
                case "int": return TokenType.INT_DT;
                case "float": return TokenType.FLOAT_DT;
                case "bool": return TokenType.BOOL_DT;
                case "string": return TokenType.STRING_DT;
                case "list": return TokenType.LIST_DT;
                case "dict": return TokenType.DICT_DT;
                case "money": return TokenType.MONEY_DT;
                case "duration": return TokenType.DURATION_DT;
                case "date": return TokenType.DATE_DT;
            }
        }
        if (contains(RESERVED_WORDS, lexeme)) {
            switch (lexeme) {
                case "entity": return TokenType.ENTITY_RW;
                case "link": return TokenType.LINK_RW;
                case "def": return TokenType.DEF_RW;
                case "let": return TokenType.LET_RW;
                case "if": return TokenType.IF_RW;
                case "else": return TokenType.ELSE_RW;
                case "for": return TokenType.FOR_RW;
                case "while": return TokenType.WHILE_RW;
                case "return": return TokenType.RETURN_RW;
                case "policy": return TokenType.POLICY_RW;
                case "manifest": return TokenType.MANIFEST_RW;
                case "source": return TokenType.SOURCE_RW;
                case "trace": return TokenType.TRACE_RW;
                case "try": return TokenType.TRY_RW;
                case "except": return TokenType.EXCEPT_RW;
                case "finally": return TokenType.FINALLY_RW;
                case "raise": return TokenType.RAISE_RW;
                case "break": return TokenType.BREAK_RW;
                case "continue": return TokenType.CONTINUE_RW;
                case "pass": return TokenType.PASS_RW;
                case "with": return TokenType.WITH_RW;
                case "yield": return TokenType.YIELD_RW;
                case "lambda": return TokenType.LAMBDA_RW;
                case "import": return TokenType.IMPORT_RW;
                case "as": return TokenType.AS_RW;
                case "del": return TokenType.DEL_RW;
                case "assert": return TokenType.ASSERT_RW;
                case "validate": return TokenType.VALIDATE_RW;
                case "outgoing_links": return TokenType.OUTGOING_LINKS_RW;
                case "incoming_links": return TokenType.INCOMING_LINKS_RW;
                case "print": return TokenType.PRINT_RW;
                case "True": case "true": case "False": case "false": return TokenType.BOOL_LITERAL;
            }
        }
        if (contains(KEYWORDS, lexeme)) {
            switch (lexeme) {
                case "match": return TokenType.MATCH_KW;
                case "case": return TokenType.CASE_KW;
                case "from": return TokenType.FROM_KW;
                case "to": return TokenType.TO_KW;
                default: return TokenType.CURRENCY_LITERAL; // ISO currency codes
            }
        }

        // predefined symbols and operators
        if (contains(ASSIGNMENT_OPERATORS, lexeme)) {
            switch (lexeme) {
                case "=": return TokenType.ASSIGN;
                case "+=": return TokenType.ADD_ASSIGN;
                case "-=": return TokenType.SUB_ASSIGN;
                case "*=": return TokenType.MUL_ASSIGN;
                case "/=": return TokenType.DIV_ASSIGN;
                case "//=": return TokenType.FLRDIV_ASSIGN;
                case "%=": return TokenType.MOD_ASSIGN;
                case "**=": return TokenType.EXP_ASSIGN;
                case "^=": return TokenType.STEPUP_ASSIGN;
                case "~=": return TokenType.STEPDOWN_ASSIGN;
                case "&=": return TokenType.BPS_ASSIGN;
                case "|=": return TokenType.UNION_ASSIGN;
                case "<<=": return TokenType.DSHIFTLEFT_ASSIGN;
                case ">>=": return TokenType.DSHIFTRIGHT_ASSIGN;
            }
        }
        if (contains(UNARY_OPERATORS, lexeme)) {
            return lexeme.equals("++") ? TokenType.INCREMENT_OP : TokenType.DECREMENT_OP;
        }
        if (contains(ARITHMETIC_OPERATORS, lexeme)) {
            switch (lexeme) {
                case "+": return TokenType.PLUS_OP;
                case "-": return TokenType.MINUS_OP;
                case "*": return TokenType.MULTIPLY_OP;
                case "/": return TokenType.DIVIDE_OP;
                case "//": return TokenType.FLRDIV_OP;
                case "%": return TokenType.MODULO_OP;
                case "**": return TokenType.POWER_OP;
                case "&": return TokenType.BPS_OP;
                case "^": return TokenType.STEPUP_OP;
                case "~": return TokenType.STEPDOWN_OP;
            }
        }
        if (contains(TEMPORAL_OPERATORS, lexeme)) {
            return lexeme.equals("<<") ? TokenType.DATE_SHIFT_LEFT_OP : TokenType.DATE_SHIFT_RIGHT_OP;
        }
        if (contains(RELATIONAL_OPERATORS, lexeme)) {
            switch (lexeme) {
                case "==": return TokenType.EQUALS;
                case "!=": return TokenType.NOT_EQUALS;
                case "<": return TokenType.LESS_THAN;
                case ">": return TokenType.GREATER_THAN;
                case "<=": return TokenType.LESSTHAN_OR_EQUAL;
                case ">=": return TokenType.GREATERTHAN_OR_EQUAL;
            }
        }
        if (contains(BINDING_OPERATOR, lexeme)) {
            return TokenType.BIND_OP;
        }
        if (contains(DELIMITERS, lexeme)) {
            switch (lexeme) {
                case "(": return TokenType.LPARENTHESIS;
                case ")": return TokenType.RPARENTHESIS;
                case "[": return TokenType.LBRACKET;
                case "]": return TokenType.RBRACKET;
                case "{": return TokenType.LBRACE;
                case "}": return TokenType.RBRACE;
                case ",": return TokenType.COMMA;
                case ":": return TokenType.COLON;
                case ".": return TokenType.DOT;
                case ";": return TokenType.SEMICOLON;
                case "'": case "\"": return TokenType.STRING_LITERAL;
            }
        }

        // user-defined values and literals
        if (isStringLiteral(lexeme)) return TokenType.STRING_LITERAL;
        if (isIdentifier(lexeme)) return TokenType.IDENTIFIER;
        if (isInteger(lexeme)) return TokenType.INT_LITERAL;
        if (isFloat(lexeme)) return TokenType.FLOAT_LITERAL;
        if (isDuration(lexeme)) return TokenType.DURATION_LITERAL;

        return TokenType.INVALID_TOKEN;
    }

    // literal and identifier validators
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
}
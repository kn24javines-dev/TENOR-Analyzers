import java.util.HashMap;
import java.util.Map;

public class TenorLexer {
    public enum TokenType {
        // Data Types
        INT_DT, FLOAT_DT, BOOL_DT, STRING_DT, LIST_DT, DICT_DT, MONEY_DT, DURATION_DT, DATE_DT,
        // Assignment Operators
        ASSIGN_OP, ADD_ASSIGN_OP, SUB_ASSIGN_OP, MUL_ASSIGN_OP, DIV_ASSIGN_OP, FLRDIV_ASSIGN_OP, MOD_ASSIGN_OP, EXP_ASSIGN_OP, UNION_ASSIGN_OP, STEPUP_ASSIGN_OP,
        STEPDOWN_ASSIGN_OP, BPS_ASSIGN_OP, DSHIFTLEFT_ASSIGN_OP, DSHIFTRIGHT_ASSIGN_OP,
        // Arithmetic Operators
        PLUS_OP, MINUS_OP, MULTIPLY_OP, DIVIDE_OP, FLRDIV_OP, MODULO_OP, POWER_OP, BPS_OP, STEPUP_OP, STEPDOWN_OP,
        // Temporal Operators
        DSHIFTLEFT_OP, DSHIFTRIGHT_OP,
        // Unary Operators
        INCREMENT_OP, DECREMENT_OP,
        // Logical Operators
        AND_OP, OR_OP, NOT_OP,
        // Relational Operators
        EQUALS_OP, NOT_EQUALS_OP, LESS_THAN_OP, GREATER_THAN_OP, LESSTHAN_OR_EQUAL_OP, GREATERTHAN_OR_EQUAL_OP,
        // Binding Operator
        BIND_OP,
        // Keywords and Currency
        MATCH_KW, CASE_KW, FROM_KW, TO_KW, F_KW, CURRENCY_LITERAL,
        // Reserved Words
        ENTITY_RW, LINK_RW, DEF_RW, LET_RW, IF_RW, ELSE_RW, FOR_RW, WHILE_RW, RETURN_RW, POLICY_RW, MANIFEST_RW, SOURCE_RW, TRACE_RW, TRY_RW, EXCEPT_RW, FINALLY_RW,
        RAISE_RW, BREAK_RW, CONTINUE_RW, PASS_RW, WITH_RW, YIELD_RW, LAMBDA_RW, IMPORT_RW, AS_RW, DEL_RW, ASSERT_RW, VALIDATE_RW, OUTGOING_LINKS_RW, INCOMING_LINKS_RW,
        PRINT_RW, INPUT_RW,
        // Delimiters
        LPARENTHESIS_DLM, RPARENTHESIS_DLM, LBRACKET_DLM, RBRACKET_DLM, LBRACE_DLM, RBRACE_DLM, COMMA_DLM, COLON_DLM, SEMICOLON_DLM, DOT_DLM, NEWLINE_DLM,
        INDENT_DLM, DEDENT_DLM, QUOTE_DLM,
        // Literals and Identifiers
        IDENTIFIER, INT_LITERAL, FLOAT_LITERAL, BOOL_LITERAL, DURATION_LITERAL, STRING_LITERAL,
        // Special Tokens
        COMMENT, WILDCARD, INVALID_TOKEN
    }

    private static final Map<String, TokenType> TOKEN_MAP = new HashMap<>();

    static {
        // Data Types
        TOKEN_MAP.put("int", TokenType.INT_DT);
        TOKEN_MAP.put("float", TokenType.FLOAT_DT);
        TOKEN_MAP.put("bool", TokenType.BOOL_DT);
        TOKEN_MAP.put("string", TokenType.STRING_DT);
        TOKEN_MAP.put("list", TokenType.LIST_DT);
        TOKEN_MAP.put("dict", TokenType.DICT_DT);
        TOKEN_MAP.put("money", TokenType.MONEY_DT);
        TOKEN_MAP.put("duration", TokenType.DURATION_DT);
        TOKEN_MAP.put("date", TokenType.DATE_DT);
        // Logical Operators
        TOKEN_MAP.put("and", TokenType.AND_OP);
        TOKEN_MAP.put("or", TokenType.OR_OP);
        TOKEN_MAP.put("not", TokenType.NOT_OP);
        // Reserved Words
        TOKEN_MAP.put("entity", TokenType.ENTITY_RW);
        TOKEN_MAP.put("link", TokenType.LINK_RW);
        TOKEN_MAP.put("def", TokenType.DEF_RW) ;
        TOKEN_MAP.put("let", TokenType.LET_RW);
        TOKEN_MAP.put("if", TokenType.IF_RW);
        TOKEN_MAP.put("else", TokenType.ELSE_RW);
        TOKEN_MAP.put("for", TokenType.FOR_RW);
        TOKEN_MAP.put("while", TokenType.WHILE_RW);
        TOKEN_MAP.put("return", TokenType.RETURN_RW) ;
        TOKEN_MAP.put("policy", TokenType.POLICY_RW);
        TOKEN_MAP.put("manifest", TokenType.MANIFEST_RW);
        TOKEN_MAP.put("source", TokenType.SOURCE_RW) ;
        TOKEN_MAP.put("trace", TokenType.TRACE_RW);
        TOKEN_MAP.put("try", TokenType.TRY_RW);
        TOKEN_MAP.put("except", TokenType.EXCEPT_RW) ;
        TOKEN_MAP.put("finally", TokenType.FINALLY_RW);
        TOKEN_MAP.put("raise", TokenType.RAISE_RW);
        TOKEN_MAP.put("break", TokenType.BREAK_RW);
        TOKEN_MAP.put ("continue", TokenType.CONTINUE_RW);
        TOKEN_MAP .put("pass", TokenType.PASS_RW);
        TOKEN_MAP.put("with", TokenType.WITH_RW);
        TOKEN_MAP.put("yield", TokenType.YIELD_RW);
        TOKEN_MAP .put ("lambda", TokenType.LAMBDA_RW) ;
        TOKEN_MAP.put("import", TokenType.IMPORT_RW);
        TOKEN_MAP.put("as", TokenType.AS_RW);
        TOKEN_MAP.put("del", TokenType.DEL_RW);
        TOKEN_MAP.put("assert", TokenType.ASSERT_RW);
        TOKEN_MAP.put("validate", TokenType.VALIDATE_RW) ;
        TOKEN_MAP.put("outgoing_links", TokenType.OUTGOING_LINKS_RW);
        TOKEN_MAP.put ("incoming_links", TokenType.INCOMING_LINKS_RW);
        TOKEN_MAP.put("print", TokenType.PRINT_RW);
        TOKEN_MAP.put("input", TokenType.INPUT_RW);
        // Boolean Literals
        TOKEN_MAP.put("True", TokenType.BOOL_LITERAL);
        TOKEN_MAP.put("False", TokenType.BOOL_LITERAL);
        TOKEN_MAP.put("true", TokenType.BOOL_LITERAL);
        TOKEN_MAP.put("false", TokenType.BOOL_LITERAL);
        // Keywords
        TOKEN_MAP.put("match", TokenType.MATCH_KW);
        TOKEN_MAP.put("case", TokenType.CASE_KW) ;
        TOKEN_MAP.put("from", TokenType.FROM_KW) ;
        TOKEN_MAP.put("to", TokenType.TO_KW);
        TOKEN_MAP.put("f", TokenType.F_KW);
        TOKEN_MAP.put("F", TokenType.F_KW);
        // Operators & Assignment
        TOKEN_MAP.put("=", TokenType.ASSIGN_OP);
        TOKEN_MAP.put("+=", TokenType.ADD_ASSIGN_OP);
        TOKEN_MAP.put("-=", TokenType.SUB_ASSIGN_OP);
        TOKEN_MAP.put("*=", TokenType.MUL_ASSIGN_OP);
        TOKEN_MAP.put("/=", TokenType.DIV_ASSIGN_OP);
        TOKEN_MAP.put("//=", TokenType.FLRDIV_ASSIGN_OP);
        TOKEN_MAP.put("%=", TokenType.MOD_ASSIGN_OP);
        TOKEN_MAP.put("**=", TokenType.EXP_ASSIGN_OP);
        TOKEN_MAP.put("^=", TokenType.STEPUP_ASSIGN_OP);
        TOKEN_MAP.put("~=", TokenType.STEPDOWN_ASSIGN_OP);
        TOKEN_MAP.put("&=", TokenType.BPS_ASSIGN_OP);
        TOKEN_MAP.put("|=", TokenType.UNION_ASSIGN_OP);
        TOKEN_MAP.put("<<=", TokenType.DSHIFTLEFT_ASSIGN_OP);
        TOKEN_MAP.put(">>=", TokenType.DSHIFTRIGHT_ASSIGN_OP);
        TOKEN_MAP.put("+", TokenType.PLUS_OP);
        TOKEN_MAP.put("-", TokenType.MINUS_OP);
        TOKEN_MAP.put("*", TokenType.MULTIPLY_OP);
        TOKEN_MAP.put("/", TokenType.DIVIDE_OP);
        TOKEN_MAP.put("//", TokenType.FLRDIV_OP);
        TOKEN_MAP.put("%", TokenType.MODULO_OP);
        TOKEN_MAP.put("**", TokenType.POWER_OP);
        TOKEN_MAP.put("&", TokenType.BPS_OP);
        TOKEN_MAP.put("^", TokenType.STEPUP_OP);
        TOKEN_MAP.put("~", TokenType.STEPDOWN_OP);
        TOKEN_MAP.put("++", TokenType.INCREMENT_OP) ;
        TOKEN_MAP.put("--", TokenType.DECREMENT_OP);
        TOKEN_MAP.put("<<", TokenType.DSHIFTLEFT_OP);
        TOKEN_MAP.put(">>", TokenType.DSHIFTRIGHT_OP);
        TOKEN_MAP .put("==", TokenType.EQUALS_OP) ;
        TOKEN_MAP.put("!=", TokenType.NOT_EQUALS_OP);
        TOKEN_MAP.put("<", TokenType.LESS_THAN_OP);
        TOKEN_MAP .put(">", TokenType.GREATER_THAN_OP);
        TOKEN_MAP.put("<=", TokenType.LESSTHAN_OR_EQUAL_OP);
        TOKEN_MAP .put(">=", TokenType.GREATERTHAN_OR_EQUAL_OP);
        TOKEN_MAP.put("@", TokenType.BIND_OP);
        // Delimiters
        TOKEN_MAP.put("(", TokenType.LPARENTHESIS_DLM);
        TOKEN_MAP.put(")", TokenType.RPARENTHESIS_DLM);
        TOKEN_MAP.put("[", TokenType.LBRACKET_DLM);
        TOKEN_MAP.put("]", TokenType.RBRACKET_DLM);
        TOKEN_MAP.put("{", TokenType.LBRACE_DLM);
        TOKEN_MAP.put("}", TokenType.RBRACE_DLM);
        TOKEN_MAP.put(",", TokenType.COMMA_DLM);
        TOKEN_MAP.put(":", TokenType.COLON_DLM);
        TOKEN_MAP.put(".", TokenType.DOT_DLM);
        TOKEN_MAP.put(";", TokenType.SEMICOLON_DLM);
        TOKEN_MAP.put("\"", TokenType.QUOTE_DLM);
        TOKEN_MAP.put("'", TokenType.QUOTE_DLM);

        // ISO Currency Codes
        String[] currencies = { "AED", "AFN", "ALL", "AMD", "ANG", "AOA", "ARS", "AUD", "AWG", "AZN", "BAM", "BBD", "BDT", "BGN", "BHD",
                                "BIF", "BMD", "BND", "BOB", "BRL", "BSD", "BTN", "BWP", "BYN", "BZD", "CAD", "CDF", "CHF", "CLP", "CNY",
                                "COP", "CRC", "CUP", "CVE", "CZK", "DJF", "DKK", "DOP", "DZD", "EGP", "ERN", "ETB", "EUR", "FJD", "FKP",
                                "GBP", "GEL", "GHS", "GIP", "GMD", "GNF", "GTQ", "GYD", "HKD", "HNL", "HTG", "HUF", "IDR", "ILS", "INR",
                                "IQD", "IRR", "ISK", "JMD", "JOD", "JPY", "KES", "KGS", "KHR", "KMF", "KPW", "KRW", "KWD", "KYD", "KZT",
                                "LAK", "LBP", "LKR", "LRD", "LSL", "LYD", "MAD", "MDL", "MGA", "MKD", "MMK", "MNT", "MOP", "MRU", "MUR",
                                "MVR", "MWK", "MXN", "MYR", "MZN", "NAD", "NGN", "NIO", "NOK", "NPR", "NZD", "OMR", "PAB", "PEN", "PGK",
                                "PHP", "PKR", "PLN", "PYG", "QAR", "RON", "RSD", "RUB", "RWF", "SAR", "SBD", "SCR", "SDG", "SEK", "SGD",
                                "SHP", "SLL", "SOS", "SRD", "SSP", "STN", "SYP", "SZL", "THB", "TJS", "TMT", "TND", "TOP", "TRY", "TTD",
                                "TWD", "TZS", "UAH", "UGX", "USD", "UYU", "UZS", "VES", "VND", "VUV", "WST", "XAF", "XCD", "XOF", "XPF",
                                "YER", "ZAR", "ZMW", "ZWL" };
        
        for (String c : currencies) {
            TOKEN_MAP.put(c, TokenType.CURRENCY_LITERAL);
        }
    }

    public static TokenType getTokenType(Lexeme lexeme) {
        if (lexeme == null) return TokenType. INVALID_TOKEN;
        if ("ERROR".equals(lexeme.type)) return TokenType.INVALID_TOKEN;
        if ("INDENT".equals(lexeme.type)) return TokenType.INDENT_DLM;
        if ("DEDENT".equals(lexeme.type)) return TokenType.DEDENT_DLM;
        if ("NEWLINE".equals(lexeme.type)) return TokenType.NEWLINE_DLM;
        if ("WILDCARD".equals(lexeme.type)) return TokenType.WILDCARD;
        if ("DURATION".equals(lexeme.type)) return TokenType.DURATION_LITERAL;
        if ("INT".equals(lexeme.type)) return TokenType.INT_LITERAL;
        if ("FLOAT".equals(lexeme.type)) return TokenType.FLOAT_LITERAL;
        if ("STRING".equals(lexeme.type)) return TokenType.STRING_LITERAL;
        if ("FSTRING_PREFIX".equals(lexeme.type)) return TokenType.F_KW;
        if ("DLM".equals(lexeme.type)) return TokenType.QUOTE_DLM;

        return getTokenType(lexeme.text);
    }

    public static TokenType getTokenType(String lexeme) {
        if (lexeme == null || lexeme.isEmpty()) return TokenType.INVALID_TOKEN;

        if (lexeme.charAt(0) == '#') return TokenType.COMMENT;
        
        TokenType match = TOKEN_MAP.get(lexeme);
        if (match != null) return match;

        if (isStringLiteral(lexeme)) return TokenType.STRING_LITERAL;
        if (isIdentifier(lexeme)) return TokenType.IDENTIFIER;
        if (isInteger(lexeme)) return TokenType.INT_LITERAL;
        if (isFloat(lexeme)) return TokenType.FLOAT_LITERAL;

        return TokenType. INVALID_TOKEN;
    }

    private static boolean isIdentifier(String str) {
        if (str == null || str.isEmpty()) return false;
        
        char first = str.charAt(0);
        if (first != '_' && !Character.isLetter(first)) return false;

        for (int i = 1; i < str.length(); i++) {
            char c = str.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') return false;
        }
        return true;
    }

    private static boolean isInteger(String str) {
        if (str == null || str.isEmpty() || !Character.isDigit(str.charAt(0))) return false;
        
        boolean prevUnderscore = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isDigit(c)) {
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
        if (str == null || str.length() < 3 || !Character.isDigit(str.charAt(0))) return false;
        
        boolean hasDecimal = false;
        boolean prevUnderscore = false;
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '.') {
                if (hasDecimal || prevUnderscore || i == str.length() - 1) return false;
                hasDecimal = true;
            } else if (Character.isDigit(c)) {
                prevUnderscore = false;
            } else if (c == '_' && !prevUnderscore) {
                prevUnderscore = true;
            } else {
                return false;
            }
        } 
        return hasDecimal && !prevUnderscore;
    }

    private static boolean isStringLiteral(String str) {
        if (str == null) return false;
        if (str.length() >= 3 && (str.charAt(0) == 'f' || str.charAt(0) == 'F')) {
            str = str.substring(1);
        }
        if (str.length() < 2) return false;
        
        char first = str.charAt(0);
        char last = str.charAt(str.length() - 1);
        return (first == '\"' && last == '\"') || (first == '\'' && last == '\'');
    }
}
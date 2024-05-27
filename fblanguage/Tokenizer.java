package hellyeah;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class Tokenizer {
    private static final String IDENTIFIER_PATTERN = "[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖş!ŞüÜ_]*";
    private static final String NUMBER_PATTERN = "\\d+";
    private static final String KEYWORD_PATTERN = "eğer|dön|değil|emre";

    private static final String SYMBOL_PATTERN = "[=<>]+|!=|;|\\+|\\-|\\*|\\/|[(),{}]";
    private static final String WHITESPACE_PATTERN = "\\s*";

    public static ArrayList<Token> tokenize(String input) {
        ArrayList<Token> tokens = new ArrayList<>();
        String[] lines = input.split("\\s*\\n\\s*");

        for (String line : lines) {
            Pattern pattern = Pattern.compile(WHITESPACE_PATTERN + "(" + IDENTIFIER_PATTERN + "|" + NUMBER_PATTERN + "|"
                    + KEYWORD_PATTERN + "|" + SYMBOL_PATTERN + ")" + WHITESPACE_PATTERN);
            Matcher matcher = pattern.matcher(line);

            while (matcher.find()) {
                String token = matcher.group(1).trim().toLowerCase();
                Token.TokenType tokenType = getTokenType(token);
                tokens.add(new Token(tokenType, token));
            }
        }
        tokens.add(new Token(Token.TokenType.EOF, null));
        return tokens;
    }

    private static Token.TokenType getTokenType(String token) {
        switch (token) {
            case "=":
                return Token.TokenType.ASSIGN;
            case ";":
                return Token.TokenType.SEMICOLON;
            case "+":
                return Token.TokenType.ADD;
            case "-":
                return Token.TokenType.SUB;
            case "*":
                return Token.TokenType.MUL;
            case "/":
                return Token.TokenType.DIV;
            case "(":
                return Token.TokenType.LPAREN;
            case ")":
                return Token.TokenType.RPAREN;
            case "{":
                return Token.TokenType.LBRACE;
            case "}":
                return Token.TokenType.RBRACE;
            case "eğer":
                return Token.TokenType.IF;
            case "dön":
                return Token.TokenType.WHILE;
            case "==":
                return Token.TokenType.EQ;
            case "!=":
                return Token.TokenType.NEQ;
            case "<":
                return Token.TokenType.LT;
            case ">":
                return Token.TokenType.GT;
            case "<=":
                return Token.TokenType.LTE;
            case ">=":
                return Token.TokenType.GTE;
            case "emre":
                return Token.TokenType.NEQ;
            case "!":
                return Token.TokenType.NEQ;
            case "değil":

                return Token.TokenType.NEQ;

            default:
                if (token.matches(NUMBER_PATTERN)) {
                    return Token.TokenType.NUMBER;
                } else if (token.matches(IDENTIFIER_PATTERN)) {
                    return Token.TokenType.IDENTIFIER;
                } else {
                    throw new RuntimeException("Invalid token: " + token);
                }
        }
    }
}
/*
 * PROGRAM: STATEMENT_LIST EOF;
 * 
 * STATEMENT_LIST: STATEMENT | STATEMENT_LIST SEMICOLON STATEMENT;
 * 
 * STATEMENT: ASSIGN_STMT | IF_STMT | WHILE_STMT | EXPRESSION_STMT | BLOCK_STMT;
 * 
 * ASSIGN_STMT: VARIABLE ASSIGN EXPRESSION;
 * 
 * VARIABLE: IDENTIFIER;
 * 
 * EXPRESSION_STMT: EXPRESSION;
 * 
 * EXPRESSION: TERM ((ADD | SUB) TERM)*;
 * 
 * TERM: FACTOR ((MUL | DIV) FACTOR)*;
 * 
 * FACTOR: VARIABLE | NUMBER | LPAREN EXPRESSION RPAREN;
 * 
 * COMPARISON: EXPRESSION COMPARISON_OP EXPRESSION;
 * 
 * EQ: '==';
 * 
 * NEQ: '!=';
 * 
 * LT: '<';
 * 
 * GT: '>';
 * 
 * LTE: '<=';
 * 
 * GTE: '>=';
 * 
 * IF_STMT: IF_CLAUSE THEN_STMT END_STMT;
 * 
 * IF_CLAUSE: 'eğer' LPAREN COMPARISON RPAREN;
 * 
 * THEN_STMT: STATEMENT_LIST;
 * 
 * END_STMT: 'son';
 * 
 * WHILE_STMT: WHILE_CLAUSE DO_STMT END_STMT;
 * 
 * WHILE_CLAUSE: 'döngü' LPAREN COMPARISON RPAREN;
 * 
 * DO_STMT: STATEMENT_LIST;
 * 
 * BLOCK_STMT: LBRACE STATEMENT_LIST RBRACE;
 * 
 * SEMICOLON: ';';
 * 
 * COMMA: ',';
 * 
 * LPAREN: '(';
 * 
 * RPAREN: ')';
 * 
 * LBRACE: '{';
 * 
 * RBRACE: '}';
 * 
 * ADD: '+';
 * 
 * SUB: '-';
 * 
 * MUL: '*';
 * 
 * DIV: '/';
 * 
 * COMMENT: '--' ~[\r\n]* '\r'? '\n';
 * 
 * WS: [ \t\r\n]+ -> skip;
 * 
 * IDENTIFIER: [a-zA-Z] [a-zA-Z0-9]*;
 * 
 * NUMBER: [0-9]+ ('.' [0-9]+)?;
 */

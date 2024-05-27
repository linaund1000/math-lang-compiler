package copluk.copluk2.toyguns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Token {
    String type;
    String value;

    Token(String type, String value) {
        this.type = type;
        this.value = value;
    }

    static final String ASSIGN = "ASSIGN";
    static final String SEMICOLON = "SEMICOLON";
    static final String ADD = "ADD";
    static final String SUB = "SUB";
    static final String MUL = "MUL";
    static final String DIV = "DIV";
    static final String NUMBER = "NUMBER";
    static final String IDENTIFIER = "IDENTIFIER";
    static final String LPAREN = "LPAREN";
    static final String RPAREN = "RPAREN";
    static final String LBRACE = "LBRACE";
    static final String RBRACE = "RBRACE";
    static final String EOF = "EOF";
    static final String IF = "IF";
    static final String ELSE = "ELSE";
    static final String END = "END";
    static final String WHILE = "WHILE";
    static final String COMPARISON_OP = "COMPARISON_OP";
}

public class RecursiveDescentParser {

    private List<Token> tokens;
    private int currentTokenIndex;

    public RecursiveDescentParser(List<Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    private Token currentToken() {
        return tokens.get(currentTokenIndex);
    }

    private void consume(String expectedType) {
        if (currentToken().type.equals(expectedType)) {
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Expected token of type " + expectedType + " but found " + currentToken().type);
        }
    }

    public void parseProgram() {
        System.out.println("Entering Program");
        parseStatementList();
        consume(Token.EOF);
        System.out.println("Exiting Program");
    }

    private void parseStatementList() {
        while (!currentToken().type.equals(Token.EOF) && !currentToken().type.equals(Token.RBRACE) && !currentToken().type.equals(Token.END)) {
            parseStatement();
            if (currentToken().type.equals(Token.SEMICOLON)) {
                consume(Token.SEMICOLON);
            }
        }
    }

    private void parseStatement() {
        switch (currentToken().type) {
            case Token.IDENTIFIER:
                parseAssignStmt();
                break;
            case Token.IF:
                parseIfStmt();
                break;
            case Token.WHILE:
                parseWhileStmt();
                break;
            case Token.LBRACE:
                parseBlockStmt();
                break;
            default:
                parseExpressionStmt();
                break;
        }
    }

    private void parseAssignStmt() {
        System.out.println("Entering Assignment Statement");
        parseVariable();
        consume(Token.ASSIGN);
        parseExpression();
        System.out.println("Exiting Assignment Statement");
    }

    private void parseVariable() {
        System.out.println("Entering Variable");
        consume(Token.IDENTIFIER);
        System.out.println("Exiting Variable");
    }

    private void parseExpressionStmt() {
        System.out.println("Entering Expression Statement");
        parseExpression();
        System.out.println("Exiting Expression Statement");
    }

    private void parseExpression() {
        System.out.println("Entering Expression");
        parseTerm();
        while (currentToken().type.equals(Token.ADD) || currentToken().type.equals(Token.SUB)) {
            if (currentToken().type.equals(Token.ADD)) {
                consume(Token.ADD);
            } else {
                consume(Token.SUB);
            }
            parseTerm();
        }
        System.out.println("Exiting Expression");
    }

    private void parseTerm() {
        System.out.println("Entering Term");
        parseFactor();
        while (currentToken().type.equals(Token.MUL) || currentToken().type.equals(Token.DIV)) {
            if (currentToken().type.equals(Token.MUL)) {
                consume(Token.MUL);
            } else {
                consume(Token.DIV);
            }
            parseFactor();
        }
        System.out.println("Exiting Term");
    }

    private void parseFactor() {
        System.out.println("Entering Factor");
        if (currentToken().type.equals(Token.NUMBER)) {
            consume(Token.NUMBER);
        } else if (currentToken().type.equals(Token.IDENTIFIER)) {
            consume(Token.IDENTIFIER);
        } else if (currentToken().type.equals(Token.LPAREN)) {
            consume(Token.LPAREN);
            parseExpression();
            consume(Token.RPAREN);
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken().type);
        }
        System.out.println("Exiting Factor");
    }

    private void parseIfStmt() {
        System.out.println("Entering If Statement");
        consume(Token.IF);
        consume(Token.LPAREN);
        parseComparison();
        consume(Token.RPAREN);
        parseStatementList();
        consume(Token.ELSE);
        parseStatementList();
        consume(Token.END);
        System.out.println("Exiting If Statement");
    }

    private void parseComparison() {
        System.out.println("Entering Comparison");
        parseExpression();
        consume(Token.COMPARISON_OP);
        parseExpression();
        System.out.println("Exiting Comparison");
    }

    private void parseWhileStmt() {
        System.out.println("Entering While Statement");
        consume(Token.WHILE);
        consume(Token.LPAREN);
        parseComparison();
        consume(Token.RPAREN);
        parseStatementList();
        consume(Token.END);
        System.out.println("Exiting While Statement");
    }

    private void parseBlockStmt() {
        System.out.println("Entering Block Statement");
        consume(Token.LBRACE);
        parseStatementList();
        consume(Token.RBRACE);
        System.out.println("Exiting Block Statement");
    }

    public static void main(String[] args) {
        String code = "a = 5; b = 10; eğer (a < b) { c = a + b; } aksi { c = 0; } son döngü (a > 0) { a = a - 1; } son";
        List<Token> tokens = tokenize(code);
        RecursiveDescentParser parser = new RecursiveDescentParser(tokens);
        parser.parseProgram();
    }

    public static List<Token> tokenize(String code) {
        List<Token> tokens = new ArrayList<>();
        String[] lines = code.toLowerCase().split("\n");
        for (String line : lines) {
            Pattern pattern = Pattern.compile("[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*|[=;()+\\-*/{}<>=!]|\\d+");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                String token = matcher.group();
                switch (token) {
                    case "=":
                        tokens.add(new Token(Token.ASSIGN, "="));
                        break;
                    case ";":
                        tokens.add(new Token(Token.SEMICOLON, ";"));
                        break;
                    case "+":
                        tokens.add(new Token(Token.ADD, "+"));
                        break;
                    case "-":
                        tokens.add(new Token(Token.SUB, "-"));
                        break;
                    case "*":
                        tokens.add(new Token(Token.MUL, "*"));
                        break;
                    case "/":
                        tokens.add(new Token(Token.DIV, "/"));
                        break;
                    case "(":
                        tokens.add(new Token(Token.LPAREN, "("));
                        break;
                    case ")":
                        tokens.add(new Token(Token.RPAREN, ")"));
                        break;
                    case "{":
                        tokens.add(new Token(Token.LBRACE, "{"));
                        break;
                    case "}":
                        tokens.add(new Token(Token.RBRACE, "}"));
                        break;
                    case "==":
                    case "!=":
                    case "<":
                    case ">":
                    case "<=":
                    case ">=":
                        tokens.add(new Token(Token.COMPARISON_OP, token));
                        break;
                    case "eğer":
                        tokens.add(new Token(Token.IF, "eğer"));
                        break;
                    case "aksi":
                        tokens.add(new Token(Token.ELSE, "aksi"));
                        break;
                    case "son":
                        tokens.add(new Token(Token.END, "son"));
                        break;
                    case "döngü":
                        tokens.add(new Token(Token.WHILE, "döngü"));
                        break;
                    default:
                        if (token.matches("\\d+")) {
                            tokens.add(new Token(Token.NUMBER, token));
                        } else if (token.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*")) {
                            tokens.add(new Token(Token.IDENTIFIER, token));
                        } else {
                            throw new RuntimeException("Invalid token: " + token);
                        }
                }
            }
        }
        tokens.add(new Token(Token.EOF, null));
        return tokens;
    }
}

package clean;

import ensonlevel.Token;
import ensonlevel.Token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    private Token currentToken() {
        if (pos < tokens.size()) {
            return tokens.get(pos);
        } else {
            return new Token(TokenType.EOF, "EOF");
        }
    }

    private void eat(TokenType type) {
        if (currentToken().getType() == type) {
            pos++;
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken());
        }
    }

    public Program parse() {
        StmtList stmtList = parseStmtList();
        if (currentToken().getType() == TokenType.EOF) {
            return new Program(stmtList);
        } else {
            throw new RuntimeException("Unexpected token: " + currentToken());
        }
    }

    private StmtList parseStmtList() {
        Stmt stmt = parseStmt();
        if (currentToken().getType() == TokenType.EOF) {
            return new StmtList(stmt);
        } else {
            return new StmtList(stmt, parseStmtList());
        }
    }

    private Stmt parseStmt() {
        if (currentToken().getType() == TokenType.IDENTIFIER) {
            return parseAssignStmt();
        } else if (currentToken().getType() == TokenType.IF) {
            return parseIfStmt();
        } else if (currentToken().getType() == TokenType.WHILE) {
            return parseWhileLoop();
        } else {
            return parseExprStmt();
        }
    }

    private AssignStmt parseAssignStmt() {
        Identifier identifier = parseIdentifier();
        eat(TokenType.ASSIGN);
        Expr expr = parseExpr();
        eat(TokenType.SEMICOLON);
        return new AssignStmt(identifier, expr);
    }

    private Stmt parseIfStmt() {
        eat(TokenType.IF);
        eat(TokenType.LPAREN);
        Condition condition = parseCondition();
        eat(TokenType.RPAREN);
        eat(TokenType.COLON);
        StmtList stmtList = parseStmtList();
        IfElseStmt elseStmt = parseElseStmt();
        eat(TokenType.END);
        return new IfStmt(condition, stmtList, elseStmt); //type mismatch for all comment places i gave you i think we need parent types
    }

    private IfElseStmt parseElseStmt() {
        if (currentToken().getType() == TokenType.ELSE) {
            eat(TokenType.ELSE);
            eat(TokenType.COLON);
            StmtList stmtList = parseStmtList();
            return new IfElseStmt(stmtList); // type mismatch again The constructor IfElseStmt(StmtList) is undefinedJava(134217858) ensonlevel.IfElseStmt  ::= eğer () ise: [] değilse: [] son!
        } else {
            return null;
        }
    }

    private WhileLoop parseWhileLoop() {
        eat(TokenType.WHILE);
        eat(TokenType.LPAREN);
        Condition condition = parseCondition();
        eat(TokenType.RPAREN);
        eat(TokenType.COLON);
        StmtList stmtList = parseStmtList();
        eat(TokenType.END);
        return new WhileLoop(condition, stmtList);
    }

    private ExprStmt parseExprStmt() {
        Expr expr = parseExpr();
        eat(TokenType.SEMICOLON);
        return new ExprStmt(expr);
    }

    private Expr parseExpr() {
        Term term = parseTerm();
        List<String> ops = new ArrayList<>();
        List<Term> terms = new ArrayList<>();
        while (currentToken().getType() == TokenType.PLUS|| currentToken().getType() == TokenType.MINUS) {
            ops.add(currentToken().getValue());
            eat(currentToken().getType());
            terms.add(parseTerm());
        }
        if (ops.isEmpty()) {
            return new Expr(term);
        } else {
            return new Expr(term, ops, terms);
        }
    }

    private Term parseTerm() {
        Factor factor = parseFactor(); // Type mismatch: cannot convert from Expr to FactorJava(16777233)
        List<String> ops = new ArrayList<>();
        List<Factor> factors = new ArrayList<>();
        while (currentToken().getType() == TokenType.MULTIPLY || currentToken().getType() == TokenType.DIVIDE) {
            ops.add(currentToken().getValue());
            eat(currentToken().getType());
            factors.add(parseFactor());
        }
        if (ops.isEmpty()) {
            return new Term(factor);
        } else {
            return new Term(factor, ops, factors);
        }
    }

    private Expr parseFactor() {
        if (currentToken().getType() == TokenType.NUMBER) {
            int value = Integer.parseInt(currentToken().getValue());
            eat(TokenType.NUMBER);
            return new Number(value); //Type mismatch: cannot convert from Number to ExprJava(16777235) ensonlevel.Number.Number(int value)
        } else if (currentToken().getType() == TokenType.IDENTIFIER) {
            return parseIdentifier(); // Identifier ensonlevel.Parser.parseIdentifier()
        } else if (currentToken().getType() == TokenType.LPAREN) {
            eat(TokenType.LPAREN);
            Expr expr = parseExpr();
            eat(TokenType.RPAREN);
            return expr; //Expr expr - ensonlevel.Parser.parseFactor()
        } else {
            throw new RuntimeException("Invalid factor");
        }
    }

    private Identifier parseIdentifier() {
        String name = currentToken().getValue();
        eat(TokenType.IDENTIFIER);
        return new Identifier(name);
    }

    private Condition parseCondition() {
        Expr leftExpr = parseExpr();
        String operator = currentToken().getValue();
        eat(currentToken().getType());
        Expr rightExpr = parseExpr();
        return new Condition(leftExpr, operator, rightExpr);
    }
}
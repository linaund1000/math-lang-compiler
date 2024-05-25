package hellyeah;
// PROGRAM: STATEMENT_LIST EOF;

// A program consists of a list of statements followed by the end of file.

// STATEMENT_LIST: STATEMENT | STATEMENT_LIST SEMICOLON STATEMENT;
// A statement list is either a single statement or a list of statements separated by semicolons.

// STATEMENT: ASSIGN_STMT | IF_STMT | WHILE_STMT | EXPRESSION_STMT | BLOCK_STMT;
// A statement can be an assignment, if statement, while statement, expression statement, or block statement.

// ASSIGN_STMT: VARIABLE ASSIGN EXPRESSION;
// An assignment statement consists of a variable, an assignment operator, and an expression.

// VARIABLE: IDENTIFIER;
// A variable is an identifier.

// EXPRESSION_STMT: EXPRESSION;
// An expression statement is simply an expression. expression == 1 true expression = 0 false

// EXPRESSION: TERM ((ADD | SUB) TERM)*;
// An expression is a term followed by zero or more additions or subtractions of terms.

// TERM: FACTOR ((MUL | DIV) FACTOR)*;
// A term is a factor followed by zero or more multiplications or divisions of factors.

// FACTOR: VARIABLE | NUMBER | LPAREN EXPRESSION RPAREN;
// A factor is either a variable, a number, or an expression enclosed in parentheses.

// COMPARISON: EXPRESSION COMPARISON_OP EXPRESSION;
// A comparison is an expression followed by a comparison operator and another expression.

// EQ: '==';
// NEQ: '!=';
// LT: '<';
// GT: '>';
// LTE: '<=';
// GTE: '>=';
// These are the comparison operators.

// IF_STMT: IF_CLAUSE THEN_STMT END_STMT;
// An if statement consists of an if clause, a then statement, and an end statement.

// IF_CLAUSE: 'eğer' LPAREN COMPARISON RPAREN;
// An if clause is the keyword 'eğer' followed by a comparison enclosed in parentheses.

// THEN_STMT: STATEMENT_LIST;
// A then statement is a list of statements.

// END_STMT: 'son';
// An end statement is the keyword 'son'.

// WHILE_STMT: WHILE_CLAUSE DO_STMT END_STMT;
// A while statement consists of a while clause, a do statement, and an end statement.

// WHILE_CLAUSE: 'döngü' LPAREN COMPARISON RPAREN;
// A while clause is the keyword 'döngü' followed by a comparison enclosed in parentheses.

// DO_STMT: STATEMENT_LIST;
// A do statement is a list of statements.

// BLOCK_STMT: LBRACE STATEMENT_LIST RBRACE;
// A block statement is a list of statements enclosed in curly braces.

// SEMICOLON: ';';
// COMMA: ',';
// LPAREN: '(';
// RPAREN: ')';
// LBRACE: '{';
// RBRACE: '}';
// ADD: '+';
// SUB: '-';
// MUL: '*';
// DIV: '/';
// These are the terminal symbols.

// COMMENT: '--' ~[\r\n]* '\r'? '\n';
// A comment is the keyword '--' followed by any characters except newline and carriage return, and ends with a newline.

// WS: [ \t\r\n]+ -> skip;
// Whitespace characters are skipped.

// IDENTIFIER: [a-zA-Z] [a-zA-Z0-9]*;
// An identifier is a letter followed by zero or more letters or digits.

// NUMBER: [0-9]+ ('.' [0-9]+)?;
// A number is one or more digits optionally followed by a decimal point and more digits.

import java.util.ArrayList;

import java.util.List;

import hellyeah.Token.TokenType;
import java.util.logging.Logger;
// AST Node classes
class ProgramNode extends ASTNode {
    // A program node has a list of statements
    List<StatementNode> statements;
}

abstract class ASTNode {
    // Abstract base class for all AST nodes
}

abstract class StatementNode extends ASTNode {
    // Abstract base class for all statement nodes
}

class AssignmentNode extends StatementNode {
    // An assignment node has a variable, an expression, and a boolean flag
    boolean isOlsun;
    String variable;
    ExpressionNode expression;
}

abstract class ExpressionNode extends ASTNode {
    // Abstract base class for all expression nodes
    public abstract int evaluate();
}

class NumberNode extends ExpressionNode {
    // A number node has a value
    int value;

    @Override
    public int evaluate() {
        return value;
    }
}

class VariableNode extends ExpressionNode {
    // A variable node has a name and a value
    String name;
    int value;

    @Override
    public int evaluate() {
        return value;
    }
}

class IfNode extends StatementNode {
    // An if node has an expression and a statement list
    ExpressionNode expression;
    StatementListNode statementList;

    public IfNode(ExpressionNode expression, StatementListNode statementList) {
        this.expression = expression;
        this.statementList = statementList;
    }
}

class WhileNode extends StatementNode {
    // A while node has an expression and a statement list
    ExpressionNode expression;
    StatementListNode statementList;

    public WhileNode(ExpressionNode expression, StatementListNode statementList) {
        this.expression = expression;
        this.statementList = statementList;
    }
}

class StatementListNode extends StatementNode {
    // A statement list node has a list of statements
    List<StatementNode> statements;

    public StatementListNode() {
        this.statements = new ArrayList<>();
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<StatementNode> statements) {
        this.statements = statements;
    }

    public void addStatement(StatementNode statement) {
        this.statements.add(statement);
    }
}

class ComparisonNode extends ExpressionNode {
    // A comparison node has a left expression, an operator, and a right expression
    ExpressionNode left;
    ComparisonOp op;
    ExpressionNode right;

    public ComparisonNode(ExpressionNode left, ComparisonOp op, ExpressionNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public int evaluate() {
        int leftValue = left.evaluate();
        int rightValue = right.evaluate();

        switch (op.type) {
            case EQ:
                return leftValue == rightValue ? 1 : 0;
            case NEQ:
                return leftValue != rightValue ? 1 : 0;
            case LT:
                return leftValue < rightValue ? 1 : 0;
            case GT:
                return leftValue > rightValue ? 1 : 0;
            case LTE:
                return leftValue <= rightValue ? 1 : 0;
            case GTE:
                return leftValue >= rightValue ? 1 : 0;
            default:
                throw new RuntimeException("Invalid comparison operator");
        }
    }
}

class ComparisonOp {
    // A comparison operator has a type
    TokenType type;

    public ComparisonOp(TokenType type) {
        this.type = type;
    }
}

class BlockNode extends StatementNode {
    StatementListNode statements;

    public BlockNode(StatementListNode statements) {
        this.statements = statements;
    }
}

class BinaryOperationNode extends ExpressionNode {
    private ExpressionNode left;
    private Token operator;
    private ExpressionNode right;

    public BinaryOperationNode(ExpressionNode left, Token operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ExpressionNode getLeft() {
        return left;
    }

    public Token getOperator() {
        return operator;
    }

    public ExpressionNode getRight() {
        return right;
    }

    @Override
    public int evaluate() {
        int leftValue = left.evaluate();
        int rightValue = right.evaluate();

        switch (operator.type) {
            case ADD:
                return leftValue + rightValue;
            case SUB:
                return leftValue - rightValue;
            case MUL:
                return leftValue * rightValue;
            case DIV:
                return leftValue / rightValue;
            default:
                throw new RuntimeException("Invalid operator");
        }
    }
}

class ParenthesisNode extends ExpressionNode {
    ExpressionNode expression;

    public ParenthesisNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate() {
        return expression.evaluate();
    }
}

class Parser {
    private List<Token> tokens;
    private int currentIndex = 0;
    private static final Logger logger = LoggerSetup.getLogger();

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    ProgramNode parseProgram() {
        logger.info("[parseProgram()] -- start of program");
        ProgramNode program = new ProgramNode();
        program.statements = parseStatementList();
        eat(TokenType.EOF);
        return program;
    }

    ArrayList<StatementNode> parseStatementList() {
        logger.info("[parseStatementList()] -- start of statement list");
        ArrayList<StatementNode> statements = new ArrayList<>();
        while (currentToken().type != TokenType.EOF) {
            statements.add(parseStatement());
        }
        return statements;
    }

    ArrayList<StatementNode> parseBlockStatementList() {
        logger.info("[parseBlockStatementList()] -- start of block statement list");
        ArrayList<StatementNode> statements = new ArrayList<>();
        eat(TokenType.LBRACE);
        while (currentToken().type != TokenType.RBRACE) {
            statements.add(parseStatement());
        }
        eat(TokenType.RBRACE);
        return statements;
    }

    StatementNode parseStatement() {
        logger.info("[parseStatement()] -- token: " + currentToken().value);
        Token currentToken = currentToken();
        if (currentToken.type == TokenType.IDENTIFIER) {
            return parseAssignmentStatement();
        } else if (currentToken.type == TokenType.IF) {
            return parseIfStatement();
        } else if (currentToken.type == TokenType.WHILE) {
            return parseWhileStatement();
        } else {
            throw new RuntimeException("Invalid statement");
        }
    }

    ExpressionNode parseExpression() {
        logger.info("[parseExpression()] -- token: " + currentToken().value);
        ExpressionNode node = parseTerm();
        while (currentToken().type == TokenType.ADD || currentToken().type == TokenType.SUB) {
            Token token = currentToken();
            eat(token.type);
            ExpressionNode right = parseTerm();
            node = new BinaryOperationNode(node, token, right);
        }
        return node;
    }

    ExpressionNode parseTerm() {
        logger.info("[parseTerm()] -- token: " + currentToken().value);
        ExpressionNode node = parseFactor();
        while (currentToken().type == TokenType.MUL || currentToken().type == TokenType.DIV) {
            Token token = currentToken();
            eat(token.type);
            ExpressionNode right = parseFactor();
            node = new BinaryOperationNode(node, token, right);
        }
        return node;
    }

    ExpressionNode parseFactor() {
        logger.info("[parseFactor()] -- token: " + currentToken().value);
        Token currentToken = currentToken();
        if (currentToken.type == TokenType.NUMBER) {
            NumberNode node = new NumberNode();
            node.value = Integer.parseInt(currentToken.value);
            eat(TokenType.NUMBER);
            return node;
        } else if (currentToken.type == TokenType.IDENTIFIER) {
            VariableNode node = new VariableNode();
            node.name = currentToken.value;
            eat(TokenType.IDENTIFIER);
            return node;
        } else {
            throw new RuntimeException("Invalid factor");
        }
    }

    ExpressionNode parseComparison() {
        logger.info("[parseComparison()] -- token: " + currentToken().value);
        ExpressionNode left = parseTerm();
        while (currentToken().type == TokenType.LT || currentToken().type == TokenType.GT
                || currentToken().type == TokenType.EQ || currentToken().type == TokenType.NEQ
                || currentToken().type == TokenType.LTE || currentToken().type == TokenType.GTE) {
            ComparisonOp op = parseComparisonOp();
            ExpressionNode right = parseTerm();
            left = new ComparisonNode(left, op, right);
        }
        return left;
    }

    ComparisonOp parseComparisonOp() {
        logger.info("[parseComparisonOp()] -- token: " + currentToken().value);
        Token token = currentToken();
        if (token.type == TokenType.LT) {
            eat(TokenType.LT);
            return new ComparisonOp(TokenType.LT);
        } else if (token.type == TokenType.GT) {
            eat(TokenType.GT);
            return new ComparisonOp(TokenType.GT);
        } else if (token.type == TokenType.EQ) {
            eat(TokenType.EQ);
            return new ComparisonOp(TokenType.EQ);
        } else if (token.type == TokenType.NEQ) {
            eat(TokenType.NEQ);
            return new ComparisonOp(TokenType.NEQ);
        } else if (token.type == TokenType.LTE) {
            eat(TokenType.LTE);
            return new ComparisonOp(TokenType.LTE);
        } else if (token.type == TokenType.GTE) {
            eat(TokenType.GTE);
            return new ComparisonOp(TokenType.GTE);
        } else {
            throw new RuntimeException("Invalid comparison operator");
        }
    }

    IfNode parseIfStatement() {
        logger.info("[parseIfStatement()] -- token: " + currentToken().value);
        eat(TokenType.IF);
        eat(TokenType.LPAREN);
        ExpressionNode expression = parseComparison();
        eat(TokenType.RPAREN);
        StatementListNode statementList = new StatementListNode();
        ArrayList<StatementNode> parsedStmtList = parseBlockStatementList();
        statementList.setStatements(parsedStmtList);
        return new IfNode(expression, statementList);
    }

    WhileNode parseWhileStatement() {
        logger.info("[parseWhileStatement()] -- token: " + currentToken().value);
        eat(TokenType.WHILE);
        eat(TokenType.LPAREN);
        ExpressionNode expression = parseExpression();
        eat(TokenType.RPAREN);
        StatementListNode statementList = new StatementListNode();
        ArrayList<StatementNode> parsedStmtList = parseBlockStatementList();
        statementList.setStatements(parsedStmtList);
        return new WhileNode(expression, statementList);
    }

    AssignmentNode parseAssignmentStatement() {
        logger.info("[parseAssignmentStatement()] -- token: " + currentToken().value);
        AssignmentNode node = new AssignmentNode();
        node.variable = eat(TokenType.IDENTIFIER).value;
        eat(TokenType.ASSIGN);
        node.expression = parseExpression();
        eat(TokenType.SEMICOLON);
        return node;
    }

    BlockNode parseBlockStatement() {
        logger.info("[parseBlockStatement()] -- token: " + currentToken().value);
        eat(TokenType.LBRACE);
        StatementListNode statementList = new StatementListNode();
        statementList.getStatements().addAll(parseStatementList());
        eat(TokenType.RBRACE);
        return new BlockNode(statementList);
    }

    Token currentToken() {
        if (currentIndex >= tokens.size()) {
            return new Token(TokenType.EOF, "");
        }
        return tokens.get(currentIndex);
    }

    Token eat(TokenType type) {
        logger.info("[eat(" + type + ")] -- current token: " + currentToken());
        Token token = currentToken();
        if (token.type != type) {
            throw new RuntimeException("Expected token " + type + " but found " + token);
        }
        currentIndex++;
        return token;
    }
}
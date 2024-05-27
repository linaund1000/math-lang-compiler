
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
// An end senior emre blacktas statement is the keyword 'son'.

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
package hellyeah;

import java.util.ArrayList;

import hellyeah.Token.TokenType;

// Base class for all AST nodes
abstract class ASTNode {
    public abstract String toString();
}

// Abstract base class for all statement nodes
abstract class StatementNode extends ASTNode {
}

// Abstract base class for all expression nodes
abstract class ExpressionNode extends ASTNode {
    public abstract int evaluate();
}

// Class representing a program node with a list of statements
class ProgramNode extends ASTNode {
    ArrayList<StatementNode> statements;

    public ProgramNode(ArrayList<StatementNode> statements) {
        this.statements = statements;
    }

    public ProgramNode() {
        this.statements = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ProgramNode{\n");
        for (StatementNode statement : statements) {
            sb.append(statement.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}

// Class representing an assignment node
class AssignmentNode extends StatementNode {
    String variable;
    ExpressionNode expression;
    Context context;

    public AssignmentNode(String variable, ExpressionNode expression, Context context) {
        this.variable = variable;
        this.expression = expression;
        this.context = context;
    }

    public void evaluate() {
        int value = expression.evaluate();
        context.setVariableValue(variable, value);
    }

    @Override
    public String toString() {
        return "\nAssignmentNode{" +
                "variable='" + variable + '\'' +
                ", expression=" + expression +
                '}';
    }
}

class ComparisonNode extends ExpressionNode {
    ExpressionNode left;
    Token operator;
    ExpressionNode right;

    public ComparisonNode(ExpressionNode left, Token operator, ExpressionNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "\nComparisonNode{" +
                "left=" + left +
                ", operator=" + operator +
                ", right=" + right +
                '}';
    }

    @Override
    public int evaluate() {
        int leftValue = left.evaluate();
        int rightValue = right.evaluate();

        switch (operator.getType()) {
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
                throw new RuntimeException("Unknown operator: " + operator);
        }
    }
}

// Class representing a number node
class NumberNode extends ExpressionNode {
    int value;

    public NumberNode(int value) {
        this.value = value;
    }

    @Override
    public int evaluate() {
        return value;
    }

    @Override
    public String toString() {
        return "NumberNode{" + "value=" + value + '}';
    }
}


// Class representing a variable node
class VariableNode extends ExpressionNode {
    String name;
    Context context;

    public VariableNode(String name, Context context) {
        this.name = name;
        this.context = context;
    }

    @Override
    public int evaluate() {
        return context.getVariableValue(name);
    }

    @Override
    public String toString() {
        return "VariableNode{" + "name='" + name + '\'' + '}';
    }
}

// Class representing an if node
class IfNode extends StatementNode {
    ExpressionNode expression;
    StatementListNode statementList;

    public IfNode(ExpressionNode expression, StatementListNode statementList) {
        this.expression = expression;
        this.statementList = statementList;
    }

    @Override
    public String toString() {
        return "IfNode{" +
                "expression=" + expression +
                ", statementList=" + statementList +
                '}';
    }
}

// Class representing a while node
class WhileNode extends StatementNode {
    ExpressionNode expression;
    StatementListNode statementList;

    public WhileNode(ExpressionNode expression, StatementListNode statementList) {
        this.expression = expression;
        this.statementList = statementList;
    }

    @Override
    public String toString() {
        return "WhileNode{" +
                "expression=" + expression +
                ", statementList=" + statementList +
                '}';
    }
}

// Class representing a statement list node
class StatementListNode extends StatementNode {
    ArrayList<StatementNode> statements;

    public StatementListNode() {
        this.statements = new ArrayList<>();
    }

    public StatementListNode(ArrayList<StatementNode> stmts) {
        this.statements = stmts;
    }

    public ArrayList<StatementNode> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<StatementNode> statements) {
        this.statements = statements;
    }

    public void addStatement(StatementNode statement) {
        this.statements.add(statement);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("StatementListNode{\n");
        for (StatementNode statement : statements) {
            sb.append(statement.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
}

// Class representing a comparison operator
class ComparisonOp {
    TokenType type;

    public ComparisonOp(TokenType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ComparisonOp{" + "type=" + type + '}';
    }
}

// Class representing a block node
class BlockNode extends StatementNode {
    StatementListNode statements;

    public BlockNode(StatementListNode statements) {
        this.statements = statements;
    }

    @Override
    public String toString() {
        return "BlockNode{" + "statements=" + statements + '}';
    }
}

// Class representing a binary operation node
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

    @Override
    public String toString() {
        return "BinaryOperationNode{" +
                "left=" + left +
                ", operator=" + operator +
                ", right=" + right +
                '}';
    }
}

// Class representing a parenthesis node
class ParenthesisNode extends ExpressionNode {
    ExpressionNode expression;

    public ParenthesisNode(ExpressionNode expression) {
        this.expression = expression;
    }

    @Override
    public int evaluate() {
        return expression.evaluate();
    }

    @Override
    public String toString() {
        return "ParenthesisNode{" + "expression=" + expression + '}';
    }
}

// Parser class
class Parser {
    private ArrayList<Token> tokens;
    private int currentIndex = 0;
    private Context context;
    // private static final Logger logger = LoggerSetup.getLogger();

    Parser(ArrayList<Token> tokens, Context context) {
        this.tokens = tokens;
        this.context = context;
    }

    ProgramNode parseProgram() {
        ProgramNode program = new ProgramNode();
        program.statements = parseStatementList();
        eat(TokenType.EOF);
        // logger.info("[parseProgram()] -- start of program" + program.statements);
        return program;
    }

    ArrayList<StatementNode> parseStatementList() {
        // logger.info("[parseStatementList()] -- start of statement list");
        ArrayList<StatementNode> statementList = new ArrayList<>();

        while (currentIndex < tokens.size() && tokens.get(currentIndex).type != TokenType.RBRACE
                && tokens.get(currentIndex).type != TokenType.EOF) {
            statementList.add(parseStatement());
        }

        for (StatementNode stmt : statementList) {

            System.out.println("\n");
            System.out.println("[   ] " + stmt);
            System.out.println("\n");
        }

        // logger.info("[parseStatementList()] -- start of statement list");
        return statementList;
    }

    StatementNode parseStatement() {
        // logger.info("[parseStatement()] -- start of statement");
        Token token = tokens.get(currentIndex);
        switch (token.type) {
            case IF:
                return parseIfStatement();
            case WHILE:
                return parseWhileStatement();
            case IDENTIFIER:
                return parseAssignmentStatement();
            case LBRACE:
                return parseBlock();
            default:
                throw new RuntimeException("Unexpected token: " + token.type);
        }

    }

    IfNode parseIfStatement() {
        eat(TokenType.IF);
        eat(TokenType.LPAREN);
        ExpressionNode expression = parseComparison();
        eat(TokenType.RPAREN);

        System.out.println(expression.evaluate());
        if (expression.evaluate() == 1) {
            System.out.println(expression.evaluate() == 1);
            StatementListNode statementList = parseBlock().statements;
            return new IfNode(expression, statementList);
        } else {
            while (currentIndex < tokens.size() && tokens.get(currentIndex).type != TokenType.RBRACE) {
                // jump (false){}
                eat(peek().getType());
            }
            eat(TokenType.RBRACE);
            return null;
        }

    }

    WhileNode parseWhileStatement() {

        int cr = currentIndex;

        eat(TokenType.WHILE);

        eat(TokenType.LPAREN);
        ExpressionNode expression = parseComparison();
        System.out.println(expression);
        eat(TokenType.RPAREN);
        if (expression.evaluate() == 1) {
            System.out.println(expression.evaluate() == 1);
            StatementListNode statementList = parseBlock().statements;

            currentIndex = cr;
            parseWhileStatement();//recursive loop burda
            return new WhileNode(expression, statementList);
        } else {
            while (currentIndex < tokens.size() && tokens.get(currentIndex).type != TokenType.RBRACE) {
                eat(peek().getType());
                // jump (false){}
            }
            eat(TokenType.RBRACE);
            return null;
        }
    }

    AssignmentNode parseAssignmentStatement() {
        String variable = tokens.get(currentIndex).value;

        eat(TokenType.IDENTIFIER);
        eat(TokenType.ASSIGN);
        ExpressionNode expression = parseExpression();
        eat(TokenType.SEMICOLON);

        int value = expression.evaluate();
        context.setVariableValue(variable, value);

        return new AssignmentNode(variable, expression, context);
    }

    BlockNode parseBlock() {
        // logger.info("[parseBlock()] -- start of block");
        eat(TokenType.LBRACE);
        StatementListNode statementList = new StatementListNode(parseStatementList());
        eat(TokenType.RBRACE);
        return new BlockNode(statementList);
    }

    ExpressionNode parseExpression() {
        // logger.info("[parseExpression()] -- start of expression");
        return parseComparison();
    }

    ExpressionNode parseComparison() {
        // logger.info("[parseComparison()] -- start of comparison");
        ExpressionNode left = parseTerm();
        while (isComparisonOperator(peek())) {
            Token operator = tokens.get(currentIndex);
            eat(operator.type);
            ExpressionNode right = parseTerm();
            left = new ComparisonNode(left, operator, right);
        }
        return left;
    }

    ExpressionNode parseTerm() {
        // logger.info("[parseTerm()] -- start of term");
        ExpressionNode node = parseFactor();
        while (currentIndex < tokens.size() &&
                (tokens.get(currentIndex).type == TokenType.ADD ||
                        tokens.get(currentIndex).type == TokenType.SUB)) {
            Token operator = tokens.get(currentIndex);
            eat(operator.type);
            ExpressionNode right = parseFactor();
            node = new BinaryOperationNode(node, operator, right);
        }
        return node;
    }

    ExpressionNode parseFactor() {
        // logger.info("[parseFactor()] -- start of factor");
        ExpressionNode node = parsePrimary();
        while (currentIndex < tokens.size() &&
                (tokens.get(currentIndex).type == TokenType.MUL ||
                        tokens.get(currentIndex).type == TokenType.DIV)) {
            Token operator = tokens.get(currentIndex);
            eat(operator.type);
            ExpressionNode right = parsePrimary();
            node = new BinaryOperationNode(node, operator, right);
        }
        return node;
    }

    ExpressionNode parsePrimary() {
        // logger.info("[parsePrimary()] -- start of primary");
        Token token = tokens.get(currentIndex);
        switch (token.type) {
            case NUMBER:
                eat(TokenType.NUMBER);
                return new NumberNode(Integer.parseInt(token.value));
            case IDENTIFIER:
                eat(TokenType.IDENTIFIER);
                return new VariableNode(token.value, context);
            case LPAREN:
                eat(TokenType.LPAREN);
                ExpressionNode node = parseExpression();
                eat(TokenType.RPAREN);
                return new ParenthesisNode(node);
            default:
                throw new RuntimeException("Unexpected token: " + token.type);
        }
    }

    boolean isComparisonOperator(Token token) {
        return token.type == TokenType.EQ || token.type == TokenType.NEQ ||
                token.type == TokenType.LT || token.type == TokenType.GT ||
                token.type == TokenType.LTE || token.type == TokenType.GTE;
    }

    Token peek() {
        if (currentIndex < tokens.size()) {
            return tokens.get(currentIndex);
        }
        return new Token(TokenType.EOF, "");

    }

    void eat(TokenType tokenType) {
        if (currentIndex < tokens.size() && tokens.get(currentIndex).type == tokenType) {
            currentIndex++;
        } else {
            throw new RuntimeException(
                    "Unexpected token: " + tokens.get(currentIndex) + ", expected: " + tokenType);
        }
    }

}

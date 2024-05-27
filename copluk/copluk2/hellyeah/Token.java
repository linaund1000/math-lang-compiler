package hellyeah;

public class Token {

    /*
     * /*
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
    /*
     * /*
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
     */public enum TokenType {
        IDENTIFIER,
        NUMBER,
        ASSIGN,
        SEMICOLON,
        ADD,
        SUB,
        MUL,
        DIV,
        EOF,
        IF,
        WHILE,
        LPAREN,
        RPAREN,
        LBRACE,
        RBRACE,
        EQ,
        NEQ,
        LT,
        GT,
        LTE,
        GTE,
        COMMA,
        COMMENT,
        WS, END
    }

    public TokenType type;
    public String value;
    public String column;
    public String line;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value; 
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
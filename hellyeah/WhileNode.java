package hellyeah;

public class WhileNode extends StatementNode {
    private ExpressionNode expression;
    private StatementListNode statementList;

    public WhileNode(ExpressionNode expression, StatementListNode statementList) {
        this.expression = expression;
        this.statementList = statementList;
    }

    // getters and setters
    public ExpressionNode getExpression() {
        return expression;
    }

    public void setExpression(ExpressionNode expression) {
        this.expression = expression;
    }

    public StatementListNode getStatementList() {
        return statementList;
    }

    public void setStatementList(StatementListNode statementList) {
        this.statementList = statementList;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("WhileNode{\n");
        sb.append("  expression: ").append(getExpression().toString()).append("\n");
        sb.append("  statementList: ").append(getStatementList().toString()).append("\n");
        sb.append("}");
        return sb.toString();
    }
}

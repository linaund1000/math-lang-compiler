package hellyeah;

import java.util.ArrayList;
import java.util.List;

public class StatementListNode {
    private List<StatementNode> children = new ArrayList<>();

    public StatementListNode() {
    }

    public StatementListNode(List<StatementNode> children) {
        this.children.addAll(children);
    }

    public StatementListNode addStatement(StatementNode statement) {
        children.add(statement);
        return this;
    }

    public List<StatementNode> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StatementListNode{\n");
        sb.append("  children: [\n");
        for (StatementNode child : children) {
            sb.append("    ").append(child.toString()).append("\n");
        }
        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }
}

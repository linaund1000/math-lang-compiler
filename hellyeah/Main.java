package hellyeah;

import java.util.List;



public class Main {
    public static void main(String[] args) {
        String code = "a = 5; b = 10; eğer (a == b) { c = a+2+3 ; pipoipoipoi = 1; }";
        List<Token> tokens = Tokenizer.tokenize(code);

        for (Token token : tokens) {
            System.out.println(token);
        }

        Parser parser = new Parser(tokens);
        ProgramNode program = parser.parseProgram();

        // Print the AST tree
        printAST(program, 0);
    }

    public static void printAST(ASTNode node, int indent) {
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        if (node instanceof ProgramNode) {
            System.out.println("Program");
            for (StatementNode statement : ((ProgramNode) node).statements) {
                printAST(statement, indent + 1);
            }
        } else if (node instanceof AssignmentNode) {
            System.out.println("Assignment: " + ((AssignmentNode) node).variable);
            printAST(((AssignmentNode) node).expression, indent + 1);
        } else if (node instanceof NumberNode) {
            System.out.println("Number: " + ((NumberNode) node).value);
        } else if (node instanceof VariableNode) {
            System.out.println("Variable: " + ((VariableNode) node).name);
        }
    }
}

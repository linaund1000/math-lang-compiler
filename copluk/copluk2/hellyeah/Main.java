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
        System.out.println(program);
    }


}

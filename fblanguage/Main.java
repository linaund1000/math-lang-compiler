package hellyeah;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Context context = new Context();
        String code = """
                               a = 5;
                dön (a < 50) {
                 a = a + 1;
                 }
                 gelsinchatgptbunuyazsin = a;
                 yazamaz = 0;
                               a = a + 1;
                               """;

        ArrayList<Token> tokens = Tokenizer.tokenize(code);

        for (Token token : tokens) {
            System.out.println(token);
        }

        Parser parser = new Parser(tokens, context);
        ProgramNode program = parser.parseProgram();

        System.out.println(context);
    }
}
// example codes

// true codes

/*
 * a = 5;
 * beğer = 10;
 * 
 * eğer (a != beğer) {
 * c = a + 2 + 3;
 * pipoipoipoi = 1;
 * }
 * 
 * dön (a < 10) {
 * a = a + 1;
 * }
 * 
 * eğer (pipoipoipoi == 1) {
 * pipoipoipoi = 2;
 * }
 * 
 * a = a * 2;
 * 
 * eğer (a > 20) {
 * a = a + 1;
 * }
 * 
 * eğer (a > 25) {
 * a = a + 2;
 * }
 * 
 * eğer (a > 30) {
 * a = a + 3;
 * }
 * 
 * dön (a < 50) {
 * a = a + 1;
 * }
 * 
 * sonucu {a=50, c=10, pipoipoipoi=2, beğer=10}
 */

/*
 * a = 5;
 * b = 10;
 * 
 * eğer (a != b) {
 * c = a + 2 + 3;
 * pipoipoipoi = 1;
 * }
 * 
 * dön (a < 10) {
 * a = a + 1;
 * }
 * 
 * 
 * eğer (pipoipoipoi == 1) {
 * pipoipoipoi = 2;
 * }
 * 
 * a = a * 2;
 * 
 * eğer (a > 20) {
 * a = a + 1;
 * }
 */

/*
 * while loop test
 * 
 * 
 * a = 5;
 * b = 10;
 * 
 * eğer (a != b) {
 * c = a + 2 + 3;
 * pipoipoipoi = 1;
 * }
 * 
 * dön (a < 10) {
 * a = a + 1;
 * }
 * 
 * 
 * 
 * 
 */
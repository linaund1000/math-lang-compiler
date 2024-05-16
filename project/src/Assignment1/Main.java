package Assignment1;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//<program> ::= <stmt> EOF
//<stmt> ::= <assign_stmt>
//<assign_stmt> ::= IDENTIFIER ASSIGN <expr> ;
//<expr> ::= NUMBER
//<program> ::= <stmt_list>
//<stmt_list> ::= <stmt> | <stmt> <stmt_list>
//<stmt> ::= <assign_stmt>
//<assign_stmt> ::= <identifier> = <expr> ;
//<expr> ::= <number>
//<identifier> ::= IDENTIFIER
//<number> ::= NUMBER


//birsonraki soyle olmali -- variable clasorunde calisan var

/*
<program> ::= <stmt_list> EOF
<stmt_list> ::= <stmt> | <stmt> <stmt_list>
<stmt> ::= <assign_stmt> | <expr_stmt>
<assign_stmt> ::= <identifier> = <expr> ;
<expr_stmt> ::= <expr> ;
<expr> ::= <term> ((+ | -) <term>)*
<term> ::= <factor> (( * | / ) <factor>)*
<factor> ::= <number> | <identifier>
<identifier> ::= IDENTIFIER
<number> ::= NUMBER


x = 5;
y = x + 2;
z = y * 3;
 */


class Tok {
    String typ;
    String val;

    Tok(String typ, String val) {
        this.typ = typ;
        this.val = val;
    }
}

class Idt {
    String val;

    Idt(String val) {
        this.val = val;
    }
}

class Num {
    int val;

    Num(int val) {
        this.val = val;
    }
}

class AsnStmt {
    Idt idt;
    Num expr;

    AsnStmt(Idt idt, Num expr) {
        this.idt = idt;
        this.expr = expr;
    }
}

class StmtLst {
    AsnStmt stmt;
    StmtLst stmtLst;

    StmtLst(AsnStmt stmt) {
        this.stmt = stmt;
        this.stmtLst = null;
    }

    StmtLst(AsnStmt stmt, StmtLst stmtLst) {
        this.stmt = stmt;
        this.stmtLst = stmtLst;
    }
}

public class Main {
    public static List<Tok> tokenize(String cd) {
        List<Tok> toks = new ArrayList<>();
        String[] lines = cd.split("\n");
        for (String ln : lines) {
            Pattern p = Pattern.compile("\\w+|[=;]|\\d+");
            Matcher m = p.matcher(ln);
            while (m.find()) {
                String tok = m.group();
                if (tok.equals("=")) {
                    toks.add(new Tok("ASSIGN", "="));
                } else if (tok.equals("=")) {
                    toks.add(new Tok("ASSIGN", "="));
                } else if (tok.equals(";")) {
                    toks.add(new Tok("STATEMENT_SEPARATOR", ";"));
                } else if (tok.matches("0|[1-9][0-9]*")) {
                    toks.add(new Tok("NUMBER", tok));
                } else if (tok.matches("[a-zA-Z_][a-zA-Z_0-9]*")) {
                    toks.add(new Tok("IDENTIFIER", tok));
                } else {
                    throw new RuntimeException("Invalid token: " + tok);
                }
            }
        }
        toks.add(new Tok("EOF", null));
        return toks;
    }

    static class Prsr {
        List<Tok> toks;
        int idx;

        Prsr(List<Tok> toks) {
            this.toks = toks;
            this.idx = 0;
        }

        StmtLst program() {
            StmtLst stmtLst = stmtList();
            match("EOF");
            return stmtLst;
        }

        StmtLst stmtList() {
            AsnStmt stmt = stmt();
            StmtLst stmtLst = new StmtLst(stmt);
            while (true) {
                if (peek().typ.equals("EOF")) {
                    break;
                }
                stmt = stmt();
                stmtLst = new StmtLst(stmt, stmtLst);
            }
            return stmtLst;
        }

        AsnStmt stmt() {
            Idt idt = identifier();
            match("ASSIGN");
            Num expr = expr();
            match("STATEMENT_SEPARATOR");
            return new AsnStmt(idt, expr);
        }

        Idt identifier() {
            Tok tok = match("IDENTIFIER");
            return new Idt(tok.val);
        }

        Num expr() {
            Tok tok = match("NUMBER");
            return new Num(Integer.parseInt(tok.val));
        }

        Tok match(String typ) {
            Tok tok = toks.get(idx);
            System.out.println(tok.val);
            System.out.println(tok.typ);
            if (!tok.typ.equals(typ)) {
                throw new RuntimeException("Expected " + typ + " but got " + tok.typ);
            }
            idx++;
            return tok;
        }

        Tok peek() {
            return toks.get(idx);
        }
    }

    public static void main(String[] args) {
        String cd = "x = 5;\ny = 10;\n";
        List<Tok> toks = tokenize(cd);
        for (Tok tok : toks) {
            System.out.println(tok.typ);
            System.out.println(tok.val);
            System.out.println("------");
        }
        Prsr prsr = new Prsr(toks);
        StmtLst program = prsr.program();
        System.out.println("Parsed program:");
        printProgram(program);
    }

    static void printProgram(StmtLst stmtLst) {
        while (stmtLst != null) {
            System.out.println(stmtLst.stmt.idt.val + " = " + stmtLst.stmt.expr.val + ";");
            stmtLst = stmtLst.stmtLst;
        }
    }
}
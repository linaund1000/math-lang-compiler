package Variable2.ilayda;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    Expr expr;

    AsnStmt(Idt idt, Expr expr) {
        this.idt = idt;
        this.expr = expr;
    }
}

class ExprStmt {
    Expr expr;

    ExprStmt(Expr expr) {
        this.expr = expr;
    }
}

class Stmt {
    AsnStmt asnStmt;
    ExprStmt exprStmt;

    Stmt(AsnStmt asnStmt) {
        this.asnStmt = asnStmt;
        this.exprStmt = null;
    }

    Stmt(ExprStmt exprStmt) {
        this.asnStmt = null;
        this.exprStmt = exprStmt;
    }
}

class StmtLst {
    Stmt stmt;
    StmtLst stmtLst;

    StmtLst(Stmt stmt) {
        this.stmt = stmt;
        this.stmtLst = null;
    }

    StmtLst(Stmt stmt, StmtLst stmtLst) {
        this.stmt = stmt;
        this.stmtLst = stmtLst;
    }
}

class Expr {
    Term term;
    List<String> ops;
    List<Term> terms;

    Expr(Term term) {
        this.term = term;
        this.ops = new ArrayList<>();
        this.terms = new ArrayList<>();
    }

    Expr(Term term, List<String> ops, List<Term> terms) {
        this.term = term;
        this.ops = ops;
        this.terms = terms;
    }
}

class Term {
    Factor factor;
    List<String> ops;
    List<Factor> factors;

    Term(Factor factor) {
        this.factor = factor;
        this.ops = new ArrayList<>();
        this.factors = new ArrayList<>();
    }

    Term(Factor factor, List<String> ops, List<Factor> factors) {
        this.factor = factor;
        this.ops = ops;
        this.factors = factors;
    }
}

class Factor {
    Num num;
    Idt idt;

    Factor(Num num) {
        this.num = num;
        this.idt = null;
    }

    Factor(Idt idt) {
        this.num = null;
        this.idt = idt;
    }
}

public class Main {
public static List<Tok> tokenize(String cd) {
    List<Tok> toks = new ArrayList<>();
    String[] lines = cd.split("\n");
    
    for (String ln : lines) {
        System.out.println("Processing line: " + ln); // Debugging line
        Pattern p = Pattern.compile("[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*|[=;+\\-*/]|\\d+");
        Matcher m = p.matcher(ln);
        
        while (m.find()) {
            String tok = m.group();
            System.out.println("Matched token: " + tok); // Debugging line
            switch (tok) {
                case "=":
                    toks.add(new Tok("ASSIGN", "="));
                    break;
                case ";":
                    toks.add(new Tok("STATEMENT_SEPARATOR", ";"));
                    break;
                case "+":
                    toks.add(new Tok("PLUS", "+"));
                    break;
                case "-":
                    toks.add(new Tok("MINUS", "-"));
                    break;
                case "*":
                    toks.add(new Tok("MULTIPLY", "*"));
                    break;
                case "/":
                    toks.add(new Tok("DIVIDE", "/"));
                    break;
                default:
                    if (tok.matches("0|[1-9][0-9]*")) {
                        toks.add(new Tok("NUMBER", tok));
                    } else if (tok.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*")) {
                        toks.add(new Tok("IDENTIFIER", tok));
                    } else {
                        System.out.println("Invalid token: " + tok); // Debugging line
                        throw new RuntimeException("Invalid token: " + tok);
                    }
            }
        }
    }
    
    toks.add(new Tok("EOF", null));
    System.out.println("Final tokens: " + toks); // Debugging line
    return toks;
}
    public static void main(String[] args) {
        String code = "x = 5;\ny = x + 2;\nz = y * 3;";
        List<Tok> tokens = tokenize(code);
        
    }



}

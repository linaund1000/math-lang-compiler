package Variable;

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
            Pattern p = Pattern.compile("\\w+|[=;+\\-*/]|\\d+");
            Matcher m = p.matcher(ln);
            while (m.find()) {
                String tok = m.group();
                if (tok.equals("=")) {
                    toks.add(new Tok("ASSIGN", "="));
                } else if (tok.equals(";")) {
                    toks.add(new Tok("STATEMENT_SEPARATOR", ";"));
                } else if (tok.equals("+")) {
                    toks.add(new Tok("PLUS", "+"));
                } else if (tok.equals("-")) {
                    toks.add(new Tok("MINUS", "-"));
                } else if (tok.equals("*")) {
                    toks.add(new Tok("MULTIPLY", "*"));
                } else if (tok.equals("/")) {
                    toks.add(new Tok("DIVIDE", "/"));
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
            Stmt stmt = stmt();
            StmtLst stmtLst = new StmtLst(stmt);
            while (!peek().typ.equals("EOF")) {
                stmt = stmt();
                stmtLst = new StmtLst(stmt, stmtLst);
            }
            return stmtLst;
        }

        Stmt stmt() {
            if (peek().typ.equals("IDENTIFIER")) {
                AsnStmt asnStmt = asnStmt();
                return new Stmt(asnStmt);
            } else if (peek().typ.equals("NUMBER")) {
                ExprStmt exprStmt = exprStmt();
                return new Stmt(exprStmt);
            } else {
                throw new RuntimeException("Expected IDENTIFIER or NUMBER, but got " + peek().typ);
            }
        }

        AsnStmt asnStmt() {
            Idt idt = identifier();
            match("ASSIGN");
            Expr expr = expr();
            match("STATEMENT_SEPARATOR");
            return new AsnStmt(idt, expr);
        }

        ExprStmt exprStmt() {
            Expr expr = expr();
            match("STATEMENT_SEPARATOR");
            return new ExprStmt(expr);
        }

        Expr expr() {
            Term term = term();
            Expr expr = new Expr(term);
            while (peek().typ.equals("PLUS") || peek().typ.equals("MINUS")) {
                String op = peek().typ.equals("PLUS") ? "+" : "-";
                match(peek().typ); // Pass the token type to match
                term = term();
                expr.ops.add(op);
                expr.terms.add(term);
            }
            return expr;
        }

        Term term() {
            Factor factor = factor();
            Term term = new Term(factor);
            while (peek().typ.equals("MULTIPLY") || peek().typ.equals("DIVIDE")) {
                String op = peek().typ.equals("MULTIPLY") ? "*" : "/";
                match(peek().typ); // Pass the token type to match
                factor = factor();
                term.ops.add(op);
                term.factors.add(factor);
            }
            return term;
        }

        Factor factor() {
            if (peek().typ.equals("NUMBER")) {
                Tok tok = match("NUMBER");
                return new Factor(new Num(Integer.parseInt(tok.val)));
            } else if (peek().typ.equals("IDENTIFIER")) {
                Tok tok = match("IDENTIFIER");
                return new Factor(new Idt(tok.val));
            } else {
                throw new RuntimeException("Expected NUMBER or IDENTIFIER, but got " + peek().typ);
            }
        }

        Idt identifier() {
            Tok tok = match("IDENTIFIER");
            return new Idt(tok.val);
        }

        Tok match(String typ) {
            Tok tok = toks.get(idx);
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
        String cd = "x = 5;\ny = x  + 432;\nz = y * 3;";
        List<Tok> toks = tokenize(cd);
        Prsr prsr = new Prsr(toks);
        StmtLst program = prsr.program();
        System.out.println("Parsed program:");
        printProgram(program);
    }

    static void printProgram(StmtLst stmtLst) {
        while (stmtLst != null) {
            if (stmtLst.stmt.asnStmt != null) {
                AsnStmt asnStmt = stmtLst.stmt.asnStmt;
                System.out.print(asnStmt.idt.val + " = ");
                printExpr(asnStmt.expr);
                System.out.println(";");
            } else {
                ExprStmt exprStmt = stmtLst.stmt.exprStmt;
                printExpr(exprStmt.expr);
                System.out.println(";");
            }
            stmtLst = stmtLst.stmtLst;
        }
    }

    static void printExpr(Expr expr) {
        printTerm(expr.term);
        for (int i = 0; i < expr.ops.size(); i++) {
            System.out.print(" " + expr.ops.get(i) + " ");
            printTerm(expr.terms.get(i));
        }
    }

    static void printTerm(Term term) {
        printFactor(term.factor);
        for (int i = 0; i < term.ops.size(); i++) {
            System.out.print(" " + term.ops.get(i) + " ");
            printFactor(term.factors.get(i));
        }
    }

    static void printFactor(Factor factor) {
        if (factor.num != null) {
            System.out.print(factor.num.val);
        } else {
            System.out.print(factor.idt.val);
        }
    }

}
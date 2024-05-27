package copluk.copluk2.toyguns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Tok {
    String typ;
    String val;

    Tok(String typ, String val) {
        this.typ = typ;
        this.val = val;
    }

    // Static constants for the token types
    public static final String ASSIGN = "ASSIGN";
    public static final String STATEMENT_SEPARATOR = "STATEMENT_SEPARATOR";
    public static final String PLUS = "PLUS";
    public static final String MINUS = "MINUS";
    public static final String MULTIPLY = "MULTIPLY";
    public static final String DIVIDE = "DIVIDE";
    public static final String NUMBER = "NUMBER";
    public static final String IDENTIFIER = "IDENTIFIER";
    public static final String EOF = "EOF";
    public static final String IF = "IF";
    public static final String THEN = "THEN";
    public static final String ELSE = "ELSE";
    public static final String WHILE = "WHILE";
    public static final String DO = "DO";
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

class IfStmt {
    Expr condition;
    Stmt thenStmt;
    Stmt elseStmt;

    IfStmt(Expr condition, Stmt thenStmt, Stmt elseStmt) {
        this.condition = condition;
        this.thenStmt = thenStmt;
        this.elseStmt = elseStmt;
    }
}

class WhileStmt {
    Expr condition;
    Stmt bodyStmt;

    WhileStmt(Expr condition, Stmt bodyStmt) {
        this.condition = condition;
        this.bodyStmt = bodyStmt;
    }
}

class Stmt {
    AsnStmt asnStmt;
    ExprStmt exprStmt;
    IfStmt ifStmt;
    WhileStmt whileStmt;

    Stmt(AsnStmt asnStmt) {
        this.asnStmt = asnStmt;
        this.exprStmt = null;
        this.ifStmt = null;
        this.whileStmt = null;
    }

    Stmt(ExprStmt exprStmt) {
        this.asnStmt = null;
        this.exprStmt = exprStmt;
        this.ifStmt = null;
        this.whileStmt = null;
    }

    Stmt(IfStmt ifStmt) {
        this.asnStmt = null;
        this.exprStmt = null;
        this.ifStmt = ifStmt;
        this.whileStmt = null;
    }

    Stmt(WhileStmt whileStmt) {
        this.asnStmt = null;
        this.exprStmt = null;
        this.ifStmt = null;
        this.whileStmt = whileStmt;
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
            Pattern p = Pattern.compile("[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*|[=;+\\-*/]|\\d+");
            Matcher m = p.matcher(ln);
            while (m.find()) {
                String tok = m.group();
                if (tok.equals("=")) {
                    toks.add(new Tok(Tok.ASSIGN, "="));
                } else if (tok.equals(";")) {
                    toks.add(new Tok(Tok.STATEMENT_SEPARATOR, ";"));
                } else if (tok.equals("+")) {
                    toks.add(new Tok(Tok.PLUS, "+"));
                } else if (tok.equals("-")) {
                    toks.add(new Tok(Tok.MINUS, "-"));
                } else if (tok.equals("*")) {
                    toks.add(new Tok(Tok.MULTIPLY, "*"));
                } else if (tok.equals("/")) {
                    toks.add(new Tok(Tok.DIVIDE, "/"));
                } else if (tok.equals("eğer")) {
                    toks.add(new Tok(Tok.IF, "eğer"));
                } else if (tok.equals("ise")) {
                    toks.add(new Tok(Tok.THEN, "ise"));
                } else if (tok.equals("aksi")) {
                    toks.add(new Tok(Tok.ELSE, "aksi"));
                } else if (tok.equals("iken")) {
                    toks.add(new Tok(Tok.WHILE, "iken"));
                } else if (tok.equals("yap")) {
                    toks.add(new Tok(Tok.DO, "yap"));
                } else if (tok.matches("0|[1-9][0-9]*")) {
                    toks.add(new Tok(Tok.NUMBER, tok));
                } else if (tok.matches("[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*")) {
                    toks.add(new Tok(Tok.IDENTIFIER, tok));
                } else {
                    throw new RuntimeException("Invalid token: " + tok);
                }
            }
        }
        toks.add(new Tok(Tok.EOF, null));
        return toks;
    }

    public static void main(String[] args) {
        String code = "a = 5;\nb = 10;\neğer a + b - 3 ise\n  c = a + b;\n  aksi\n  c = 0;\niken a - 1 yap\n  a = a - 1;\n";
        List<Tok> toks = tokenize(code);
        Prsr prsr = new Prsr(toks);
        StmtLst stmtLst = prsr.program();
        Prnt prnt = new Prnt();
        prnt.prntStmtLst(stmtLst);
    }
}

class Prsr {
    List<Tok> toks;
    int idx;

    Prsr(List<Tok> toks) {
        this.toks = toks;
        this.idx = 0;
    }

    StmtLst program() {
        StmtLst stmtLst = stmtList();
        match(Tok.EOF);
        return stmtLst;
    }

    StmtLst stmtList() {
        Stmt stmt = stmt();
        StmtLst stmtLst = new StmtLst(stmt);
        while (!peek().typ.equals(Tok.EOF)) {
            stmt = stmt();
            stmtLst = new StmtLst(stmt, stmtLst);
        }
        return stmtLst;
    }

    Stmt stmt() {
        if (peek().typ.equals(Tok.IDENTIFIER)) {
            AsnStmt asnStmt = asnStmt();
            return new Stmt(asnStmt);
        } else if (peek().typ.equals(Tok.NUMBER)) {
            ExprStmt exprStmt = exprStmt();
            return new Stmt(exprStmt);
        } else if (peek().typ.equals(Tok.IF)) {
            IfStmt ifStmt = ifStmt();
            return new Stmt(ifStmt);
        } else if (peek().typ.equals(Tok.WHILE)) {
            WhileStmt whileStmt = whileStmt();
            return new Stmt(whileStmt);
        } else {
            throw new RuntimeException("Expected IDENTIFIER, NUMBER, IF, or WHILE, but got " + peek().typ);
        }
    }

    AsnStmt asnStmt() {
        Idt idt = identifier();
        match(Tok.ASSIGN);
        Expr expr = expr();
        match(Tok.STATEMENT_SEPARATOR);
        return new AsnStmt(idt, expr);
    }

    ExprStmt exprStmt() {
        Expr expr = expr();
        match(Tok.STATEMENT_SEPARATOR);
        return new Expr
    IfStmt ifStmt() {
    match(Tok.IF);
    Expr condition = expr();
    match(Tok.THEN);
    Stmt thenStmt = stmt();
    Stmt elseStmt = null;
    if (peek().typ.equals(Tok.ELSE)) {
        match(Tok.ELSE);
        elseStmt = stmt();
    }
    return new IfStmt(condition, thenStmt, elseStmt);
}

WhileStmt whileStmt() {
    match(Tok.WHILE);
    Expr condition = expr();
    match(Tok.DO);
    Stmt bodyStmt = stmt();
    return new WhileStmt(condition, bodyStmt);
}

Expr expr() {
    Term term = term();
    List<String> ops = new ArrayList<>();
    List<Term> terms = new ArrayList<>();
    while (peek().typ.equals(Tok.PLUS) || peek().typ.equals(Tok.MINUS)) {
        String op = next().val;
        Term nextTerm = term();
        ops.add(op);
        terms.add(nextTerm);
    }
    return new Expr(term, ops, terms);
}

Term term() {
    Factor factor = factor();
    List<String> ops = new ArrayList<>();
    List<Factor> factors = new ArrayList<>();
    while (peek().typ.equals(Tok.MULTIPLY) || peek().typ.equals(Tok.DIVIDE)) {
        String op = next().val;
        Factor nextFactor = factor();
        ops.add(op);
        factors.add(nextFactor);
    }
    return new Term(factor, ops, factors);
}

Factor factor() {
    if (peek().typ.equals(Tok.NUMBER)) {
        int num = Integer.parseInt(next().val);
        return new Factor(new Num(num));
    } else if (peek().typ.equals(Tok.IDENTIFIER)) {
        Idt idt = identifier();
        return new Factor(idt);
    } else {
        throw new RuntimeException("Expected NUMBER or IDENTIFIER, but got " + peek().typ);
    }
}

Idt identifier() {
    String val = next().val;
    return new Idt(val);
}

Tok next() {
    return toks.get(idx++);
}

Tok peek() {
    return toks.get(idx);
}

void match(String expectedType) {
    Tok tok = next();
    if (!tok.typ.equals(expectedType)) {
        throw new RuntimeException("Expected " + expectedType + ", but got " + tok.typ);
    }
}

class Prnt {
    void prntStmtLst(StmtLst stmtLst) {
        while (stmtLst != null) {
            prntStmt(stmtLst.stmt);
            stmtLst = stmtLst.stmtLst;
        }
    }

    void prntStmt(Stmt stmt) {
        if (stmt.asnStmt != null) {
            prntAsnStmt(stmt.asnStmt);
        } else if (stmt.exprStmt != null) {
            prntExprStmt(stmt.exprStmt);
        } else if (stmt.ifStmt != null) {
            prntIfStmt(stmt.ifStmt);
        } else if (stmt.whileStmt != null) {
            prntWhileStmt(stmt.whileStmt);
        }
    }

    void prntAsnStmt(AsnStmt asnStmt) {
        System.out.println(asnStmt.idt.val + " = " + prntExpr(asnStmt.expr));
    }

    void prntExprStmt(ExprStmt exprStmt) {
        System.out.println(prntExpr(exprStmt.expr));
    }

    void prntIfStmt(IfStmt ifStmt) {
        System.out.println("eğer " + prntExpr(ifStmt.condition) + " ise");
        prntStmt(ifStmt.thenStmt);
        if (ifStmt.elseStmt != null) {
            System.out.println("aksi");
            prntStmt(ifStmt.elseStmt);
        }
    }

    void prntWhileStmt(WhileStmt whileStmt) {
        System.out.println("iken " + prntExpr(whileStmt.condition) + " yap");
        prntStmt(whileStmt.bodyStmt);
    }

    String prntExpr(Expr expr) {
        StringBuilder sb = new StringBuilder();
        sb.append(prntTerm(expr.term));
        for (int i = 0; i < expr.ops.size(); i++) {
            sb.append(" ").append(expr.ops.get(i)).append(" ").append(prntTerm(expr.terms.get(i)));
        }
        return sb.toString();
    }

    String prntTerm(Term term) {
        StringBuilder sb = new StringBuilder();
        sb.append(prntFactor(term.factor));
        for (int i = 0; i < term.ops.size(); i++) {
            sb.append(" ").append(term.ops.get(i)).append(" ").append(prntFactor(term.factors.get(i)));
        }
        return sb.toString();
    }

    String prntFactor(Factor factor) {
        if (factor.num != null) {
            return Integer.toString(factor.num.val);
        } else {
            return factor.idt.val;
        }
    }
}

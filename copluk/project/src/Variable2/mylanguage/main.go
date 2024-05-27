package main

import (
	"fmt"
)

// <program> ::= <stmt_list> EOF
type Program struct {
	stmtLst StmtLst
}

// <identifier> ::= IDENTIFIER
type Idt struct {
	val string
}

// <number> ::= NUMBER
type Num struct {
	val int
}

// <assign_stmt> ::= <identifier> = <expr> ;
type AsnStmt struct {
	idt  Idt
	expr Expr
}

// <expr_stmt> ::= <expr> ;
type ExprStmt struct {
	expr Expr
}

// <stmt> ::= <assign_stmt> | <expr_stmt> | <if_stmt> | <if_else_stmt> | <while_loop>
type Stmt struct {
	asnStmt   *AsnStmt
	exprStmt  *ExprStmt
	ifStmt    *IfStmt
	ifElseStmt *IfElseStmt
	whileLoop *WhileLoop
}

// <stmt_list> ::= <stmt> | <stmt> <stmt_list>
type StmtLst struct {
	stmt    Stmt
	stmtLst *StmtLst
}

// <expr> ::= <term> ((+ | -) <term>)*
type Expr struct {
	term  Term
	ops   []string
	terms []Term
}

// <term> ::= <factor> (( * | / ) <factor>)*
type Term struct {
	factor  Factor
	ops     []string
	factors []Factor
}

// <factor> ::= <number> | <identifier>
type Factor struct {
	num *Num
	idt *Idt
}

// <if_stmt> ::= eğer (<condition>) ise: [<stmt_list>] son!
type IfStmt struct {
	condition Condition
	stmtLst   StmtLst
}

// <if_else_stmt> ::= eğer (<condition>) ise: [<stmt_list>] değilse: [<stmt_list>] son!
type IfElseStmt struct {
	condition   Condition
	ifStmtLst   StmtLst
	elseStmtLst StmtLst
}

// <while_loop> ::= dön eğer (<condition>) ise: [<stmt_list>] son!
type WhileLoop struct {
	condition Condition
	stmtLst   StmtLst
}

// <condition> ::= <expr> (eşittir | değil) <expr>
type Condition struct {
	leftExpr  Expr
	operator  string
	rightExpr Expr
}

func main() {
	cd := "çalışma = 12;\ny = ö + 3;\nz = y * 3;"
	fmt.Println(cd)
	toks := tokenize(cd)
	parser := NewParser(toks)
	program := parser.program()
	fmt.Println("Parsed program:")
	printProgram(*program.stmtLst)
}

func printProgram(stmtLst StmtLst) {
	for stmtLst.stmtLst != nil {
		if stmtLst.stmt.asnStmt != nil {
			asnStmt := stmtLst.stmt.asnStmt
			fmt.Print(asnStmt.idt.val + " = ")
			printExpr(asnStmt.expr)
			fmt.Println(";")
		} else {
			exprStmt := stmtLst.stmt.exprStmt
			printExpr(exprStmt.expr)
			fmt.Println(";")
		}
		stmtLst = *stmtLst.stmtLst
	}
}

func printExpr(expr Expr) {
	printTerm(expr.term)
	for i, op := range expr.ops {
		fmt.Print(" " + op + " ")
		printTerm(expr.terms[i])
	}
}

func printTerm(term Term) {
	printFactor(term.factor)
	for i, op := range term.ops {
		fmt.Print(" " + op + " ")
		printFactor(term.factors[i])
	}
}

func printFactor(factor Factor) {
	if factor.num != nil {
		fmt.Print(factor.num.val)
	} else {
		fmt.Print(factor.idt.val)
	}
}
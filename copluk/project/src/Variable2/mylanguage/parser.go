package main

import "strconv"


type Parser struct {
	toks []Tok
	idx  int
}

func NewParser(toks []Tok) *Parser {
	return &Parser{toks, 0}
}

func (p *Parser) program() StmtLst {
	stmtLst := p.stmtList()
	p.match("EOF")
	return stmtLst
}
func (p *Parser) stmtList() StmtLst {
	stmt := p.stmt()
	stmtLst := StmtLst{stmt, nil}
	currentStmtLst := &stmtLst
	for p.peek().typ != "EOF" {
		stmt = p.stmt()
		newStmtLst := StmtLst{stmt, nil}
		currentStmtLst.stmtLst = &newStmtLst
		currentStmtLst = &newStmtLst
	}
	return stmtLst
}
func (p *Parser) stmt() Stmt {
	if p.peek().typ == "IDENTIFIER" {
		asnStmt := p.asnStmt()
		return Stmt{&asnStmt, nil, nil, nil, nil}
	} else if p.peek().typ == "NUMBER" {
		exprStmt := p.exprStmt()
		return Stmt{nil, &exprStmt, nil, nil, nil}
	} else {
		panic("Expected IDENTIFIER or NUMBER, but got " + p.peek().typ)
	}
}

func (p *Parser) asnStmt() AsnStmt {
	idt := p.identifier()
	p.match("ASSIGN")
	expr := p.expr()
	p.match("STATEMENT_SEPARATOR")
	return AsnStmt{idt, expr}
}

func (p *Parser) exprStmt() ExprStmt {
	expr := p.expr()
	p.match("STATEMENT_SEPARATOR")
	return ExprStmt{expr}
}

func (p *Parser) expr() Expr {
	term := p.term()
	expr := Expr{term, []string{}, []Term{}}
	for p.peek().typ == "PLUS" || p.peek().typ == "MINUS" {
		op := "+"
		if p.peek().typ == "MINUS" {
			op = "-"
		}
		p.match(p.peek().typ)
		term := p.term()
		expr.ops = append(expr.ops, op)
		expr.terms = append(expr.terms, term)
	}
	return expr
}
func (p *Parser) term() Term {
	factor := p.factor()
	term := Term{factor, []string{}, []Factor{}}
	for p.peek().typ == "MULTIPLY" || p.peek().typ == "DIVIDE" {
		op := "*"
		if p.peek().typ == "DIVIDE" {
			op = "/"
		}
		p.match(p.peek().typ)
		factor := p.factor()
		term.ops = append(term.ops, op)
		term.factors = append(term.factors, factor)
	}
	return term
}

func (p *Parser) factor() Factor {
	if p.peek().typ == "NUMBER" {
		tok := p.match("NUMBER")
		val, _ := strconv.Atoi(tok.val)
		return Factor{&Num{val}, nil}
	} else if p.peek().typ == "IDENTIFIER" {
		tok := p.match("IDENTIFIER")
		return Factor{nil, &Idt{tok.val}}
	} else {
		panic("Expected NUMBER or IDENTIFIER, but got " + p.peek().typ)
	}
}

func (p *Parser) identifier() Idt {
	tok := p.match("IDENTIFIER")
	return Idt{tok.val}
}

func (p *Parser) match(typ string) Tok {
	tok := p.toks[p.idx]
	if tok.typ != typ {
		panic("Expected " + typ + " but got " + tok.typ)
	}
	p.idx++
	return tok
}

func (p *Parser) peek() Tok {
	return p.toks[p.idx]
}
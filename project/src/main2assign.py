#<program> ::= <stmt> EOF
#<stmt> ::= <assign_stmt>
#<assign_stmt> ::= IDENTIFIER ASSIGN <expr> STATEMENT_SEPARATOR
#<expr> ::= NUMBER
#<program> ::= <stmt_list>
#<stmt_list> ::= <stmt> | <stmt> <stmt_list>
#<stmt> ::= <assign_stmt>
#<assign_stmt> ::= <identifier> ASSIGN <expr> STATEMENT_SEPARATOR
#<expr> ::= <number>
#<identifier> ::= IDENTIFIER
#<number> ::= NUMBER

# \w+           |        [=;]        |           \d+
#chartowords   or       =;          or        digit 
import re
from typing import List, Optional

# Token class represents a single token in the input code
class Tok:
    def __init__(self, typ: str, val: str):
        # typ: the type of token (e.g. IDENTIFIER, NUMBER, ASSIGN, etc.)
        self.typ: str = typ
        # val: the actual value of the token (e.g. "x" for an identifier, 5 for a number)
        self.val: str = val

# Identifier class represents an identifier (e.g. a variable name)
class Idt:
    def __init__(self, val: str):
        # val: the actual value of the identifier (e.g. "x")
        self.val: str = val

# Number class represents a number literal
class Num:
    def __init__(self, val: int):
        # val: the actual value of the number (e.g. 5)
        self.val: int = val

# AssignStmt class represents an assignment statement (e.g. x = 5)
class AsnStmt:
    def __init__(self, idt: Idt, expr: Num):
        # idt: the identifier being assigned to (e.g. x)
        self.idt: Idt = idt
        # expr: the expression being assigned (e.g. 5)
        self.expr: Num = expr

# StmtList class represents a list of statements
class StmtLst:
    def __init__(self, stmt: AsnStmt, stmt_lst: Optional['StmtLst'] = None):
        # stmt: the current statement
        self.stmt: AsnStmt = stmt
        # stmt_lst: the remaining statements in the list (optional)
        self.stmt_lst: Optional['StmtLst'] = stmt_lst

# tokenize function takes in the input code and returns a list of tokens
def tokenize(cd: str) -> List[Tok]:
    toks = []
    for ln in cd.splitlines():
        for tok in re.findall(r'\w+|[=;]|\d+', ln):
            if tok == "=":
                toks.append(Tok("ASSIGN", "="))
            elif tok == ";":
                toks.append(Tok("STATEMENT_SEPARATOR", ";"))
            elif tok.isalpha():
                toks.append(Tok("IDENTIFIER", tok))
            elif tok.isdigit():
                toks.append(Tok("NUMBER", int(tok)))
            else:
                raise Exception("Invalid token: " + tok)
    toks.append(Tok("EOF", None))
    return toks

# Parser needed here

class Prsr:
    def __init__(self, toks: List[Tok]):
        self.toks = toks
        self.idx = 0

    def program(self) -> StmtLst:
        stmt_lst = self.stmt_list()
        self.match("EOF")
        return stmt_lst

    def stmt_list(self) -> StmtLst:
        stmt = self.stmt()
        stmt_lst = StmtLst(stmt)
        while True:
            if self.peek().typ == "EOF":
                break
            stmt = self.stmt()
            stmt_lst = StmtLst(stmt, stmt_lst)
        return stmt_lst

    def stmt(self) -> AsnStmt:
        idt = self.identifier()
        self.match("ASSIGN")
        expr = self.expr()
        self.match("STATEMENT_SEPARATOR")
        return AsnStmt(idt, expr)

    def identifier(self) -> Idt:
        tok = self.match("IDENTIFIER")
        return Idt(tok.val)

    def expr(self) -> Num:
        tok = self.match("NUMBER")
        return Num(tok.val)

    def match(self, typ: str) -> Tok:
        tok = self.toks[self.idx]
        if tok.typ != typ:
            raise Exception(f"Expected {typ} but got {tok.typ}")
        self.idx += 1
        return tok

    def peek(self) -> Tok:
        return self.toks[self.idx]
# main function runs the parser on the input code
def main() -> None:
    cd = """
x = 5;
y = 10;

"""
    toks = tokenize(cd)
    for tok in toks:
        print(tok.typ)
        print(tok.val)
        print("------")
    prsr = Prsr(toks)
    program = prsr.program()
    print("Parsed program:")
    print_program(program)

def print_program(stmt_lst: StmtLst) -> None:
    while stmt_lst:
        print(f"{stmt_lst.stmt.idt.val} = {stmt_lst.stmt.expr.val};")
        stmt_lst = stmt_lst.stmt_lst

if __name__ == "__main__":
    main()
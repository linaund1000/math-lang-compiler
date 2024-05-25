import re

class Parser:
    def __init__(self, tokens):
        self.tokens = tokens
        self.current_token = tokens[0]

    def error(self):
        raise Exception("Invalid syntax")

    def eat(self, token_type):
        if self.current_token.type == token_type:
            self.current_token = self.tokens.pop(0)
        else:
            self.error()

    def program(self):
        stmt_list = self.stmt_list()
        if self.current_token.type != "EOF":
            self.error()
        return stmt_list

    def stmt_list(self):
        stmts = [self.stmt()]
        while self.current_token.type == "STATEMENT_SEPARATOR":
            self.eat("STATEMENT_SEPARATOR")
            stmts.append(self.stmt())
        return stmts

    def stmt(self):
        if self.current_token.type == "IDENTIFIER":
            return self.assign_stmt()
        elif self.current_token.type == "PRINT":
            return self.print_stmt()
        else:
            self.error()

    def assign_stmt(self):
        identifier = self.current_token.value
        self.eat("IDENTIFIER")
        self.eat("ASSIGN")
        expr = self.expr()
        self.eat("STATEMENT_SEPARATOR")
        return AssignStmt(identifier, expr)

    def print_stmt(self):
        self.eat("PRINT")
        expr = self.expr()
        self.eat("STATEMENT_SEPARATOR")
        return PrintStmt(expr)

    def expr(self):
        term = self.term()
        while self.current_token.type in ["ADD", "SUB"]:
            op = self.current_token.type
            self.eat(self.current_token.type)
            term2 = self.term()
            term = BinaryExpr(op, term, term2)
        return term

    def term(self):
        factor = self.factor()
        while self.current_token.type in ["MUL", "DIV"]:
            op = self.current_token.type
            self.eat(self.current_token.type)
            factor2 = self.factor()
            factor = BinaryExpr(op, factor, factor2)
        return factor

    def factor(self):
        if self.current_token.type == "IDENTIFIER":
            identifier = self.current_token.value
            self.eat("IDENTIFIER")
            return IdentifierExpr(identifier)
        elif self.current_token.type == "NUMBER":
            number = self.current_token.value
            self.eat("NUMBER")
            return NumberExpr(number)
        elif self.current_token.type == "LPAREN":
            self.eat("LPAREN")
            expr = self.expr()
            self.eat("RPAREN")
            return expr
        else:
            self.error()

class Token:
    def __init__(self, type, value):
        self.type = type
        self.value = value

class AssignStmt:
    def __init__(self, identifier, expr):
        self.identifier = identifier
        self.expr = expr

class PrintStmt:
    def __init__(self, expr):
        self.expr = expr

class BinaryExpr:
    def __init__(self, op, left, right):
        self.op = op
        self.left = left
        self.right = right

class IdentifierExpr:
    def __init__(self, identifier):
        self.identifier = identifier

class NumberExpr:
    def __init__(self, number):
        self.number = number


def tokenize(code):
    tokens = []
    for line in code.splitlines():
        for token in re.findall(r'\w+|[=;()*/+-]|\d+', line):
            print(token)
            if token == "=":
                tokens.append(Token("ASSIGN", "="))
            elif token == "print":
                tokens.append(Token("PRINT", "print"))
            elif token == "+":
                tokens.append(Token("ADD", "+"))
            elif token == "-":
                tokens.append(Token("SUB", "-"))
            elif token == "*":
                tokens.append(Token("MUL", "*"))
            elif token == "/":
                tokens.append(Token("DIV", "/"))
            elif token == "(":
                tokens.append(Token("LPAREN", "("))
            elif token == ")":
                tokens.append(Token("RPAREN", ")"))
            elif token == ";":
                tokens.append(Token("STATEMENT_SEPARATOR", ";"))
            elif token.isalpha():
                tokens.append(Token("IDENTIFIER", token))
            elif token.isdigit():
                tokens.append(Token("NUMBER", int(token)))
            else:
                raise Exception("Invalid token: " + token)
    tokens.append(Token("EOF", ""))
    return tokens

def main():
    code = """
x = 5;
print x;
y = x + 3;
print y;
"""
    tokens = tokenize(code)
    print(tokens)
    parser = Parser(tokens)
    print(parser)
    program = parser.program()
    for stmt in program:
        print(stmt)

if __name__ == "__main__":
    main()
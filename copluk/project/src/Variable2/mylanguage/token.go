package main

import (
	"fmt"
	"regexp"
	"strconv"
)

type Tok struct {
	typ string
	val string
}

func tokenize(cd string) []Tok {
	var toks []Tok
	re := regexp.MustCompile(`[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*|[=;+\-*/]|\d+`)
	matches := re.FindAllString(cd, -1)

	for _, match := range matches {
		var tok Tok
		switch match {
		case "=":
			tok = Tok{"ASSIGN", "="}
		case ";":
			tok = Tok{"STATEMENT_SEPARATOR", ";"}
		case "+":
			tok = Tok{"PLUS", "+"}
		case "-":
			tok = Tok{"MINUS", "-"}
		case "*":
			tok = Tok{"MULTIPLY", "*"}
		case "/":
			tok = Tok{"DIVIDE", "/"}
		default:
			if isNumber(match) {
				tok = Tok{"NUMBER", match}
			} else if isIdentifier(match) {
				tok = Tok{"IDENTIFIER", match}
			} else {
				fmt.Println("Invalid token:", match)
				panic("Invalid token: " + match)
			}
		}
		toks = append(toks, tok)
	}
	toks = append(toks, Tok{"EOF", ""})
	fmt.Println("Final tokens:", toks)
	return toks
}

func isNumber(s string) bool {
	_, err := strconv.ParseFloat(s, 64)
	return err == nil
}

func isIdentifier(s string) bool {
	re := regexp.MustCompile(`^[a-zA-ZçÇğĞıİöÖşŞüÜ_][a-zA-Z0-9çÇğĞıİöÖşŞüÜ_]*$`)
	return re.MatchString(s)
}
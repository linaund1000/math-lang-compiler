#include <stdio.h>
#include <string.h>
#include <ctype.h>

char *input;
int pos;

int expr();
int term();
int factor();

int match(char c) {
    if (input[pos] == c) {
        pos++;
        return 1;
    }
    return 0;
}

int expr() {
    int value = term();
    while (1) {
        if (match('+')) {
            value += term();
        } else if (match('-')) {
            value -= term();
        } else {
            return value;
        }
    }
}

int term() {
    int value = factor();
    while (1) {
        if (match('*')) {
            value *= factor();
        } else if (match('/')) {
            int divisor = factor();
            if (divisor == 0) {
                printf("Error: Division by zero\n");
                return 0;
            }
            value /= divisor;
        } else {
            return value;
        }
    }
}

int factor() {
    int value;
    if (match('(')) {
        value = expr();
        if (!match(')')) {
            printf("Error: Expected )\n");
            return 0;
        }
    } else if (isdigit(input[pos])) {
        value = 0;
        while (isdigit(input[pos])) {
            value = value * 10 + (input[pos] - '0');
            pos++;
        }
    } else {
        printf("Error: Expected ( or digit\n");
        return 0;
    }
    return value;
}

int main() {
    input = "3+4*5-(6+7)";
    pos = 0;
    int result = expr();
    printf("Result: %d\n", result);
    return 0;
c
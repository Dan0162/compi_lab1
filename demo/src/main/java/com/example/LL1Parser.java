package com.example;

import java.util.*;

class Lexer {
    private String input;
    private int index;
    private char currentChar;
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList("bin", "oct", "hex"));

    public Lexer(String input) {
        this.input = input;
        this.index = 0;
        this.currentChar = input.length() > 0 ? input.charAt(0) : '\0';
    }

    private void advance() {
        index++;
        currentChar = index < input.length() ? input.charAt(index) : '\0';
    }

    public String nextToken() {
        while (Character.isWhitespace(currentChar))
            advance();
        if (Character.isLetter(currentChar)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isLetterOrDigit(currentChar)) {
                sb.append(currentChar);
                advance();
            }
            String word = sb.toString();
            return KEYWORDS.contains(word) ? word : "id";
        } else if (Character.isDigit(currentChar)) {
            StringBuilder sb = new StringBuilder();
            while (Character.isDigit(currentChar)) {
                sb.append(currentChar);
                advance();
            }
            return "num";
        } else if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/'
                || currentChar == ';' || currentChar == '(' || currentChar == ')' || currentChar == '=') {
            char temp = currentChar;
            advance();
            return String.valueOf(temp);
        }
        return "EOF";
    }
}

class Parser {
    private Lexer lexer;
    private String currentToken;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.currentToken = lexer.nextToken();
    }

    private void match(String expected) {
        if (currentToken.equals(expected)) {
            currentToken = lexer.nextToken();
        } else {
            throw new RuntimeException("Syntax Error: expected " + expected + " but found " + currentToken);
        }
    }

    public void parse() {
        S();
        if (!currentToken.equals("EOF")) {
            throw new RuntimeException("Syntax Error: Unexpected token " + currentToken);
        }
        System.out.println("Parsing completed successfully.");
    }

    private void S() {
        DEC();
        ASIGNACIONES();
    }

    private void DEC() {
        while (currentToken.equals("bin") || currentToken.equals("oct") || currentToken.equals("hex")) {
            dec();
        }
    }

    private void dec() {
        tipo();
        match("id");
        match(";");
    }

    private void tipo() {
        if (currentToken.equals("bin") || currentToken.equals("oct") || currentToken.equals("hex")) {
            match(currentToken);
        } else {
            throw new RuntimeException("Syntax Error: Expected type (bin, oct, hex), but found " + currentToken);
        }
    }

    private void ASIGNACIONES() {
        while (currentToken.equals("id")) {
            ASIGNACION();
        }
    }

    private void ASIGNACION() {
        match("id");
        match("=");
        EXPR();
        match(";");
    }

    private void EXPR() {
        TERM();
        EXPR_prime();
    }

    private void EXPR_prime() {
        if (currentToken.equals("+") || currentToken.equals("-")) {
            match(currentToken);
            TERM();
            EXPR_prime();
        }
    }

    private void TERM() {
        FACT();
        TERM_prime();
    }

    private void TERM_prime() {
        if (currentToken.equals("*") || currentToken.equals("/")) {
            match(currentToken);
            FACT();
            TERM_prime();
        }
    }

    private void FACT() {
        if (currentToken.equals("(")) {
            match("(");
            EXPR();
            match(")");
        } else if (currentToken.equals("num") || currentToken.equals("id")) {
            match(currentToken);
        } else {
            throw new RuntimeException("Syntax Error: Unexpected token " + currentToken);
        }
    }
}

public class LL1Parser {
    public static void main(String[] args) {
        String[] testInputs = {
                "bin a; oct b; hex c; a = 101 + 11; b = 7 * 2; c = A + 1;",
                "bin x; oct y; hex z; x = 110; y = 5 + 3; z = F * 2;",
                "bin p; oct q; hex r; p = 1001 - 1; q = 6 / 2; r = B + 4;"
        };

        for (String input : testInputs) {
            try {
                System.out.println("Parsing input: " + input);
                Lexer lexer = new Lexer(input);
                Parser parser = new Parser(lexer);
                parser.parse();
                System.out.println("\n");
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
    }
}

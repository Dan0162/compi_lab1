package com.example;

public class Main {
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


package com.example;

import java.io.*;
import java.util.Scanner;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese la dirección del archivo que desea analizar: ");
        String file_path = scanner.nextLine();

        String[] testInputs = {};

        // Leer el archivo y cargar sus líneas en testInputs
        List<String> lineas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
            testInputs = lineas.toArray(new String[0]); // Convertir lista a arreglo

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        // Procesar las entradas con Lexer y Parser
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

        scanner.close();
    }
}

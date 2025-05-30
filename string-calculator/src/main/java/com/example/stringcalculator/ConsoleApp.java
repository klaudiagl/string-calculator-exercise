package com.example.stringcalculator;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.service.StringCalculatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Profile("cli")
public class ConsoleApp implements CommandLineRunner {

    private final StringCalculatorService calculatorService;

    public ConsoleApp(StringCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }


    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder inputBuilder = new StringBuilder();

        printUsage();
        System.out.println("Enter numbers to add:");

        while (true) {
            String input = scanner.nextLine();

            if ("exit".equalsIgnoreCase(input.trim())) {
                System.out.println("Exiting...");
                break;
            }

            if (input.trim().isEmpty()) {
                int length = inputBuilder.length();
                if (length > 0 && inputBuilder.charAt(length - 1) == '\n') {
                    inputBuilder.deleteCharAt(length - 1);
                }
                try {
                    int result = calculatorService.add(inputBuilder.toString());
                    System.out.println("Result: " + result);
                } catch (CalculatorException e) {
                    System.out.println(e.getMessage());
                } finally {
                    inputBuilder.setLength(0);
                    System.out.println("Enter numbers to add:");
                }
            } else {
                inputBuilder.append(input).append("\n");
            }
        }
        scanner.close();
    }

    private void printUsage() {
        System.out.println("Welcome to the String Calculator!");
        System.out.println("---------------------------------");
        System.out.println("Enter numbers to add, using ',' or enter as separators.");
        System.out.println("To finish entering arguments, press enter on a blank line.");
        System.out.println("To define a custom delimiter, use: //[delimiter] on the first line.");
        System.out.println("\nExamples:");
        System.out.println("1,2");
        System.out.println("Result: 3\n");

        System.out.println("1");
        System.out.println("2,3");
        System.out.println("Result: 6\n");

        System.out.println("//;");
        System.out.println("2;3;4");
        System.out.println("Result: 9\n");

        System.out.println("Notes:");
        System.out.println("- Numbers > 1000 are ignored.");
        System.out.println("- Negative numbers are not allowed.");
        System.out.println("- Type 'exit' to quit the application.\n");
    }


}

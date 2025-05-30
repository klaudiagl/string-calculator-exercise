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

        System.out.println("Enter 'exit' to quit");
        System.out.println("Enter numbers to add (enter on an empty line to finish):");

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
                    System.out.println("Enter numbers to add (enter on an empty line to finish):");
                }
            } else {
                inputBuilder.append(input).append("\n");
            }
        }
        scanner.close();
    }


}

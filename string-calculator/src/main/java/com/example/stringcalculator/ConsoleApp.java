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
        System.out.println("Enter numbers to add:");

        while (true) {
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                System.out.println("Exiting...");
                break;
            }

            try {
                int result = calculatorService.add(input);
                System.out.println("Result: " + result);
            } catch (CalculatorException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Enter numbers to add:");
        }

    }
}

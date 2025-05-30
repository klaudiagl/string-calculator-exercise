package com.example.stringcalculator.parser;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.exceptions.NegativeNumbersException;
import com.example.stringcalculator.exceptions.NumberExpectedException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class NumerParser {
    public record NumberParseResult(List<Integer> numbers, List<CalculatorException> errors) {
    }

    public NumberParseResult parse(String input, String delimiterRegex) {
        List<CalculatorException> errors = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        List<Integer> negatives = new ArrayList<>();

        Arrays.stream(input.split(delimiterRegex))
                .map(String::trim)
                .forEach(value -> {
                    if (value.isEmpty()) {
                        errors.add(new NumberExpectedException("Invalid number: empty number between separators"));
                    } else {
                        try {
                            int number = Integer.parseInt(value);
                            if (number < 0) {
                                negatives.add(number);
                            } else if (number <= 1000) {
                                numbers.add(number);
                            }
                        } catch (NumberFormatException e) {
                            errors.add(new NumberExpectedException("Invalid number: " + value));
                        }
                    }
                });

        if (!negatives.isEmpty()) {
            errors.add(new NegativeNumbersException(negatives));
        }

        return new NumberParseResult(numbers, errors);
    }

}

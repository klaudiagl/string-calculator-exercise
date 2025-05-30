package com.example.stringcalculator.service;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.exceptions.DelimiterException;
import com.example.stringcalculator.exceptions.NumberExpectedException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class StringCalculatorService {

    public int add(String input) throws CalculatorException {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        // default delimiters
        String delimiterRegex = "[,\n]";

        if (input.endsWith(",") || input.endsWith("\n")) {
            throw new DelimiterException("Invalid input: cannot end with a separator");
        }

        return Arrays.stream(input.split(delimiterRegex))
                .map(String::trim)
                .mapToInt(value -> {
                    if (value.isEmpty()) {
                        throw new RuntimeException(new NumberExpectedException("Invalid number: empty number between separators"));
                    }
                    try {
                        return Integer.parseInt(value);
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(new NumberExpectedException("Invalid number: " + value));
                    }
                }).sum();
    }

}

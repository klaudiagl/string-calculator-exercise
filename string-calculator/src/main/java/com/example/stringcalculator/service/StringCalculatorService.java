package com.example.stringcalculator.service;

import com.example.stringcalculator.exceptions.NumberExpectedException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class StringCalculatorService {

    public int add(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        // default delimiters
        String delimiterRegex = "[,\n]";

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

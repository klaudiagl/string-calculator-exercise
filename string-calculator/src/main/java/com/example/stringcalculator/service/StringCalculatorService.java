package com.example.stringcalculator.service;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.exceptions.DelimiterException;
import com.example.stringcalculator.exceptions.NumberExpectedException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StringCalculatorService {

    public int add(String input) throws CalculatorException {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        String delimiterRegex = "[,\n]"; // default delimiters
        String numbersPart = input;
        boolean customDelimiterUsed = false;

        if (input.startsWith("//")) {
            int delimiterEndIndex = input.indexOf('\n');
            if (delimiterEndIndex == -1)
                throw new DelimiterException("Invalid input: missing newline after delimiter declaration.");

            String custom = input.substring(2, delimiterEndIndex);
            if (custom.isEmpty()) {
                throw new DelimiterException("Invalid input: empty delimiter.");
            }
            numbersPart = input.substring(delimiterEndIndex + 1);
            delimiterRegex = Pattern.quote(custom);
            customDelimiterUsed = true;
        }

        if (numbersPart.isEmpty()) {
            return 0;
        }

        if (numbersPart.matches(".*" + delimiterRegex + "$")) {
            throw new DelimiterException("Invalid input: cannot end with a separator");
        }

        if (customDelimiterUsed) {
            Pattern invalidSeparatorPattern = Pattern.compile("[,\n]");
            Matcher matcher = invalidSeparatorPattern.matcher(numbersPart);
            if (matcher.find()) {
                int pos = matcher.start();
                char found = matcher.group().charAt(0);
                throw new DelimiterException("Invalid input: '" + delimiterRegex.replace("\\Q", "").replace("\\E", "") +
                        "' expected but '" + found + "' found at position " + pos + ".");
            }
        }

        return Arrays.stream(numbersPart.split(delimiterRegex))
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

package com.example.stringcalculator.service;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.exceptions.DelimiterException;
import com.example.stringcalculator.exceptions.NegativeNumbersException;
import com.example.stringcalculator.exceptions.NumberExpectedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
            } else if (custom.contains(",") || custom.contains("\\n")) {
                throw new DelimiterException("Custom delimiter cannot contain default delimiters: ',' or '\\n'");
            }
            numbersPart = input.substring(delimiterEndIndex + 1);
            delimiterRegex = Pattern.quote(custom);
            customDelimiterUsed = true;
        }

        if (numbersPart.isEmpty()) {
            return 0;
        }

        List<CalculatorException> errors = new ArrayList<>();

        if (numbersPart.matches(".*" + delimiterRegex + "$")) {
            errors.add(new DelimiterException("Invalid input: cannot end with a separator"));
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

        List<Integer> numbers = new ArrayList<>();
        List<Integer> negatives = new ArrayList<>();

        Arrays.stream(numbersPart.split(delimiterRegex))
                .map(String::trim)
                .forEach(value -> {
                    if (value.isEmpty()) {
                        errors.add(new NumberExpectedException("Invalid number: empty number between separators"));
                    }else{
                        try {
                            int number = Integer.parseInt(value);
                            if (number < 0) {
                                negatives.add(number);
                            } else {
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

        throwErrors(errors);

        return numbers.stream().mapToInt(Integer::intValue).sum();
    }

    private void throwErrors(List<CalculatorException> errors) throws CalculatorException {
        if (!errors.isEmpty()) {
            if (errors.size() == 1) {
                throw errors.get(0);
            } else {
                String combined = errors.stream()
                        .map(Throwable::getMessage)
                        .collect(Collectors.joining("\n"));
                throw new CalculatorException(combined);
            }
        }
    }


}

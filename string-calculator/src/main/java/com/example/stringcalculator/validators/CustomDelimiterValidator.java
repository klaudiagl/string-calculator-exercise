package com.example.stringcalculator.validators;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.exceptions.DelimiterException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CustomDelimiterValidator implements InputValidator {

    @Override
    public List<CalculatorException> validate(String input, String delimiterRegex) {
        List<CalculatorException> errors = new ArrayList<>();

        Pattern invalidSeparatorPattern = Pattern.compile("[,\n]");
        Matcher matcher = invalidSeparatorPattern.matcher(input);
        while (matcher.find()) {
            int pos = matcher.start();
            char found = matcher.group().charAt(0);
            errors.add(new DelimiterException("Invalid input: '" + delimiterRegex.replace("\\Q", "").replace("\\E", "") +
                    "' expected but '" + found + "' found at position " + pos + "."));
        }

        return errors;
    }
}

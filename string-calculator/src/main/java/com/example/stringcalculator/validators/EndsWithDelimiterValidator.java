package com.example.stringcalculator.validators;


import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.exceptions.DelimiterException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EndsWithDelimiterValidator implements InputValidator {

    @Override
    public List<CalculatorException> validate(String input, String delimiterRegex) {
        List<CalculatorException> errors = new ArrayList<>();
        if (input.matches(".*" + delimiterRegex + "$")) {
            errors.add(new DelimiterException("Invalid input: cannot end with a separator"));
        }
        return errors;
    }
}

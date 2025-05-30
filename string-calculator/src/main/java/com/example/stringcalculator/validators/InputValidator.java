package com.example.stringcalculator.validators;


import com.example.stringcalculator.exceptions.CalculatorException;

import java.util.List;

public interface InputValidator {
    List<CalculatorException> validate(String input, String delimiterRegex);

}

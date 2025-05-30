package com.example.stringcalculator.service;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.parser.DelimiterParser;
import com.example.stringcalculator.parser.NumerParser;
import com.example.stringcalculator.validators.CustomDelimiterValidator;
import com.example.stringcalculator.validators.InputValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StringCalculatorService {

    private final DelimiterParser delimiterParser;
    private final NumerParser numberParser;
    private final List<InputValidator> validators;

    public StringCalculatorService(DelimiterParser delimiterParser, NumerParser numberParser, List<InputValidator> validators) {
        this.delimiterParser = delimiterParser;
        this.numberParser = numberParser;
        this.validators = validators;
    }

    public int add(String input) throws CalculatorException {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        List<CalculatorException> errors = new ArrayList<>();

        DelimiterParser.DelimiterParseResult parseResult = delimiterParser.parse(input);
        String delimiterRegex = parseResult.delimiterRegex();
        String numbersPart = parseResult.numbersPart();

        if (numbersPart.isEmpty()) {
            return 0;
        }

        for (InputValidator validator : validators) {
            if (validator instanceof CustomDelimiterValidator) {
                if (parseResult.customDelimiterUsed()) {
                    List<CalculatorException> exceptionList = validator.validate(numbersPart, delimiterRegex);
                    // the presence of an error means that default separators were found during validation
                    // adding default separators to regex allows further validation of entered numbers
                    if (!exceptionList.isEmpty()) {
                        delimiterRegex = delimiterRegex + "|,|\n";
                    }
                    errors.addAll(exceptionList);
                }
            } else {
                errors.addAll(validator.validate(numbersPart, delimiterRegex));
            }
        }

        NumerParser.NumberParseResult numberParseResult = numberParser.parse(numbersPart, delimiterRegex);
        errors.addAll(numberParseResult.errors());

        throwErrors(errors);

        return numberParseResult.numbers().stream().mapToInt(Integer::intValue).sum();
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

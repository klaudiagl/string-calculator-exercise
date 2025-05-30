package com.example.stringcalculator.exceptions;

import java.util.List;

public class NegativeNumbersException extends CalculatorException {

    public NegativeNumbersException(List<Integer> negativeNumbers) {
        super("Negative number(s) not allowed: " + formatNegatives(negativeNumbers));
    }

    private static String formatNegatives(List<Integer> negativeNumbers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < negativeNumbers.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(negativeNumbers.get(i));
        }
        return sb.toString();
    }
}

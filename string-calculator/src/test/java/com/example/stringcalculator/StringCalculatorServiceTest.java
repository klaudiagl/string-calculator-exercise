package com.example.stringcalculator;

import com.example.stringcalculator.service.StringCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringCalculatorServiceTest {

    private StringCalculatorService calculatorService;


    @BeforeEach
    void setup() {
        calculatorService = new StringCalculatorService();
    }

    @Test
    void shouldReturnZeroForEmptyString() {
        assertEquals(0, calculatorService.add(""));
        assertEquals(0, calculatorService.add(null));
    }


}
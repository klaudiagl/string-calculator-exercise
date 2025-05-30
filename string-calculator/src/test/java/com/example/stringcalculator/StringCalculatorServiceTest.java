package com.example.stringcalculator;

import com.example.stringcalculator.exceptions.NumberExpectedException;
import com.example.stringcalculator.service.StringCalculatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringCalculatorServiceTest {

    private StringCalculatorService calculatorService;


    @BeforeEach
    void setup() {
        calculatorService = new StringCalculatorService();
    }

    @Test
    void testAddEmptyStringReturnsZero() {
        assertEquals(0, calculatorService.add(""));
        assertEquals(0, calculatorService.add(null));
    }

    @Test
    void testAddSingleNumberReturnsSameNumber() {
        assertEquals(1, calculatorService.add("1"));
    }

    @Test
    void testAddTwoNumbersReturnsTheirSum() {
        assertEquals(3, calculatorService.add("1,2"));

    }

    @Test
    void testAddMultipleNumbersReturnsSum() {
        assertEquals(12, calculatorService.add("1,2,4,5"));
    }

    @Test
    void testThrowExceptionWhenInputContainsInvalidNumber() {
        Exception exception = assertThrows(Exception.class, () -> calculatorService.add("1,a,2"));

        if (exception instanceof NumberExpectedException) {
            assertTrue(exception.getMessage().contains("Invalid number: a"));
        } else if (exception instanceof RuntimeException) {
            assertTrue(exception.getCause() instanceof NumberExpectedException);
            assertTrue(exception.getCause().getMessage().contains("Invalid number: a"));
        }
    }


}
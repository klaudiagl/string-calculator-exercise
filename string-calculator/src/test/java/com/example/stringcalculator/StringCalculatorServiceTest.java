package com.example.stringcalculator;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.exceptions.DelimiterException;
import com.example.stringcalculator.exceptions.NegativeNumbersException;
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
    void testAddEmptyStringReturnsZero() throws CalculatorException {
        assertEquals(0, calculatorService.add(""));
        assertEquals(0, calculatorService.add(null));
    }

    @Test
    void testAddSingleNumberReturnsSameNumber() throws CalculatorException {
        assertEquals(1, calculatorService.add("1"));
    }

    @Test
    void testAddTwoNumbersReturnsTheirSum() throws CalculatorException {
        assertEquals(3, calculatorService.add("1,2"));
    }

    @Test
    void testAddMultipleNumbersReturnsSum() throws CalculatorException {
        assertEquals(12, calculatorService.add("1,2,4,5"));
    }

    @Test
    void testAddInputContainsInvalidNumberThrowsException() {
        Exception exception = assertThrows(Exception.class, () -> calculatorService.add("1,a,2"));

        if (exception instanceof NumberExpectedException) {
            assertTrue(exception.getMessage().contains("Invalid number: a"));
        } else if (exception instanceof RuntimeException) {
            assertTrue(exception.getCause() instanceof NumberExpectedException);
            assertTrue(exception.getCause().getMessage().contains("Invalid number: a"));
        }
    }

    @Test
    void testAddNumbersWithNewlineSeparatorReturnsSum() throws CalculatorException {
        assertEquals(6, calculatorService.add("1,2\n3"));
    }

    @Test
    void testAddSeparatorCommaFollowedByNewlineThrowsException() {
        Exception exception = assertThrows(Exception.class, () -> calculatorService.add("1,2,\n3"));

        if (exception instanceof NumberExpectedException) {
            assertTrue(exception.getMessage().contains("Invalid number: empty number between separators"));
        } else if (exception instanceof RuntimeException) {
            assertTrue(exception.getCause() instanceof NumberExpectedException);
            assertTrue(exception.getCause().getMessage().contains("Invalid number: empty number between separators"));
        }
    }

    @Test
    void testAddInputEndsWithSeparatorThrowsException() {
        assertDelimiterException("1,2,", "Invalid input: cannot end with a separator");
    }

    @Test
    void testAddWithCustomSingleCharacterDelimiterReturnsSum() throws CalculatorException {
        assertEquals(4, calculatorService.add("//;\n1;3"));
        assertEquals(6, calculatorService.add("//|\n1|2|3"));
    }

    @Test
    void testAddWithCustomMultiCharacterDelimiterReturnsSum() throws CalculatorException {
        assertEquals(7, calculatorService.add("//sep\n2sep5"));
    }

    @Test
    void testAddWithEmptyDelimitersThrowsException() {
        assertDelimiterException("//\n2,3", "Invalid input: empty delimiter.");
    }

    @Test
    void testAddDelimiterMissingNewlineThrowsException() {
        assertDelimiterException("//sep2sep3", "Invalid input: missing newline after delimiter declaration.");
    }

    @Test
    void testAddWithMixedDelimitersThrowsException() {
        assertDelimiterException("//|\n1|2,3", "Invalid input: '|' expected but ',' found at position 3.");
    }

    @Test
    void testAddWithCustomDelimiterContainsCommaThrowsException() {
        assertDelimiterException("//,\n1,2");
    }

    @Test
    void testAddWithCustomDelimiterContainsNewlineThrowsException() {
        assertDelimiterException("//\\n\n1,2\n3");
    }

    @Test
    void testAddWithCustomDelimiterIncludesDefaultSeparatorsThrowsException() {
        assertDelimiterException("//sep,\n1sep,2");
    }

    @Test
    void testAddNegativeNumberThrowsException() {
        assertNegativeNumbersException("1,-2", "Negative number(s) not allowed: -2");
        assertNegativeNumbersException("2,-4, -9", "Negative number(s) not allowed: -4, -9");
    }

    private void assertDelimiterException(String input) {
        assertDelimiterException(input, "Custom delimiter cannot contain default delimiters: ',' or '\\n'");
    }

    private void assertDelimiterException(String input, String expectedMessage) {
        Exception exception = assertThrows(CalculatorException.class, () -> calculatorService.add(input));
        assertTrue(exception instanceof DelimiterException);
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    private void assertNegativeNumbersException(String input, String expectedMessage) {
        Exception exception = assertThrows(CalculatorException.class, () -> calculatorService.add(input));
        assertTrue(exception instanceof NegativeNumbersException);
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void testAddMultipleErrorsReturnsCombinedErrorMessage() {
        Exception exception = assertThrows(CalculatorException.class, () -> calculatorService.add("1,\n2,-3"));
        assertTrue(exception.getMessage().contains("Invalid number: empty number between separators\nNegative number(s) not allowed: -3"));
    }

    @Test
    void testAddMultipleTypeErrorsReturnsCombinedErrorMessage() {
        Exception exception = assertThrows(CalculatorException.class, () -> calculatorService.add("//|\n1|2,-3"));
        assertTrue(exception.getMessage().contains("Invalid input: '|' expected but ',' found at position 3.\nNegative number(s) not allowed: -3"));
    }

}
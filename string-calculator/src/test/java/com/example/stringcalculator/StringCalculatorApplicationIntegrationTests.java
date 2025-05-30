package com.example.stringcalculator;

import com.example.stringcalculator.exceptions.CalculatorException;
import com.example.stringcalculator.service.StringCalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.profiles.active=test")
class StringCalculatorApplicationIntegrationTests {

    @Autowired
    private StringCalculatorService calculatorService;

    @Test
    void contextLoads() throws CalculatorException {
        assertEquals(6, calculatorService.add("1,2,3"));
    }

}

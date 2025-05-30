package com.example.stringcalculator.service;

import org.springframework.stereotype.Service;

@Service
public class StringCalculatorService {

    public int add(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }

}

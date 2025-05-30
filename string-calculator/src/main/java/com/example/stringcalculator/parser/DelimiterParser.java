package com.example.stringcalculator.parser;

import com.example.stringcalculator.exceptions.DelimiterException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DelimiterParser {

    public record DelimiterParseResult(String delimiterRegex, String numbersPart, boolean customDelimiterUsed) {
    }

    public DelimiterParseResult parse(String input) throws DelimiterException {
        if (input.startsWith("//")) {
            int delimiterEndIndex = input.indexOf('\n');
            if (delimiterEndIndex == -1)
                throw new DelimiterException("Invalid input: missing newline after delimiter declaration.");

            String rawDelimiter = input.substring(2, delimiterEndIndex);
            if (rawDelimiter.isEmpty()) {
                throw new DelimiterException("Invalid input: empty delimiter.");
            } else if (rawDelimiter.contains(",") || rawDelimiter.contains("\\n")) {
                throw new DelimiterException("Custom delimiter cannot contain default delimiters: ',' or '\\n'");
            }
            return new DelimiterParseResult(Pattern.quote(rawDelimiter), input.substring(delimiterEndIndex + 1), true);
        } else {
            return new DelimiterParseResult("[,\n]", input, false);
        }
    }


}

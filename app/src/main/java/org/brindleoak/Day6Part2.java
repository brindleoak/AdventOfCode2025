package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Day6Part2 {

    record InterimResults(BigInteger result, int nextOperatorIndex) {
    }

    public static void main(final String[] args) throws IOException {
        final InputStream is = Day6Part2.class.getResourceAsStream("/data/day-6-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-6-input.txt");
        }

        List<String> rules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            rules = br.lines().toList();
        }
        BigInteger result = computeResult(rules);
        System.out.println("The final result is: " + result);

    }

    public static BigInteger computeResult(List<String> ruleList) {
        ArrayList<String> rules = new ArrayList<>(ruleList);

        var operators = rules.get(4);

        int currentOperatorIndex = 0;
        BigInteger finalResult = BigInteger.ZERO;

        InterimResults interimResults = computeNextResult(rules, operators, currentOperatorIndex);
        finalResult = interimResults.result;

        while (interimResults.nextOperatorIndex != 0) {
            currentOperatorIndex = interimResults.nextOperatorIndex;
            interimResults = computeNextResult(rules, operators, currentOperatorIndex);
            finalResult = finalResult.add(interimResults.result);
        }

        return finalResult;

    }

    public static InterimResults computeNextResult(List<String> ruleList, String operators, int currentOperatorIndex) {
        if (currentOperatorIndex >= operators.length()) {
            throw new IllegalArgumentException("Current operator index is out of bounds");
        }

        ArrayList<String> rules = new ArrayList<>(ruleList);

        int nextOperatorIndex = 0;
        int i;
        char currentOperator = operators.charAt(currentOperatorIndex);

        for (i = currentOperatorIndex + 1; i < operators.length() && operators.charAt(i) == ' '; i++) {
        }

        int numberOfValues = 0;
        if (i < operators.length()) {
            nextOperatorIndex = i;
            numberOfValues = nextOperatorIndex - currentOperatorIndex - 1;
        } else {
            nextOperatorIndex = 0;
            numberOfValues = operators.length() - currentOperatorIndex;
        }

        BigInteger interimResult = BigInteger.ZERO;
        if (currentOperator == '*') {
            interimResult = BigInteger.ONE;
        }

        for (i = currentOperatorIndex; i < currentOperatorIndex + numberOfValues; i++) {
            String value1 = String.valueOf(rules.get(0).charAt(i)).strip();
            String value2 = String.valueOf(rules.get(1).charAt(i)).strip();
            String value3 = String.valueOf(rules.get(2).charAt(i)).strip();
            String value4 = String.valueOf(rules.get(3).charAt(i)).strip();
            BigInteger value = new BigInteger(value1 + value2 + value3 + value4);
            System.out.println("Values computed: " + value);
            if (currentOperator == '+') {
                interimResult = interimResult.add(value);
            } else {
                interimResult = interimResult.multiply(value);
            }
            System.out.println("The interim result is: " + interimResult);
        }

        System.out.println("Next Operator: " + nextOperatorIndex);
        System.out.println("--------------------");
        return new InterimResults(interimResult, nextOperatorIndex);
    }

}

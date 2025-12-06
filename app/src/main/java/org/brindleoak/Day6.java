package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

    public static void main(final String[] args) throws IOException {
        final InputStream is = Day3Part2.class.getResourceAsStream("/data/day-6-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-6-input.txt");
        }

        List<String> rules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            rules = br.lines().toList();
        }

        var t = storeInput(rules);

    }

    public static ArrayList<String> storeInput(List<String> ruleList) {
        ArrayList<String> rules = new ArrayList<>(ruleList);

        var values1 = rules.get(0).split("\\s+");
        var values2 = rules.get(1).split("\\s+");
        var values3 = rules.get(2).split("\\s+");
        var values4 = rules.get(3).split("\\s+");
        var operators = rules.get(4).split("\\s+");

        BigInteger[] results = new BigInteger[operators.length];

        for (int i = 0; i < operators.length; i++) {
            BigInteger val1 = new BigInteger(values1[i]);
            BigInteger val2 = new BigInteger(values2[i]);
            BigInteger val3 = new BigInteger(values3[i]);
            BigInteger val4 = new BigInteger(values4[i]);

            switch (operators[i]) {
                case "+" -> results[i] = val1.add(val2).add(val3).add(val4);
                case "*" -> results[i] = val1.multiply(val2).multiply(val3).multiply(val4);
                default -> throw new IllegalArgumentException("Unknown operator: " + operators[i]);
            }

        }

        BigInteger finalResult = BigInteger.ZERO;
        for (BigInteger result : results) {
            finalResult = finalResult.add(result);
        }

        System.out.println(finalResult);

        return rules;
    }

}

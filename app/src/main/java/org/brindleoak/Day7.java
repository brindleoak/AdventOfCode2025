package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Day7 {

    record InterimResults(String row, int totalSplits) {
    }

    public static void main(final String[] args) throws IOException {
        final InputStream is = Day6Part2.class.getResourceAsStream("/data/day-7-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-7-input.txt");
        }

        List<String> rules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            rules = br.lines().toList();
        }
        int result = computeResult(rules);
        System.out.println("The final result is: " + result);

    }

    public static int computeResult(List<String> rules) {
        String head = rules.isEmpty() ? null : rules.getFirst();
        List<String> tail = rules.isEmpty() ? List.of()
                : rules.subList(1, rules.size());

        int numberOfSplits = 0;
        InterimResults interimResults = new InterimResults(head, numberOfSplits);

        for (String rule : tail) {
            interimResults = processRow(interimResults, rule);
            numberOfSplits = interimResults.totalSplits;
        }

        return numberOfSplits;
    }

    public static InterimResults processRow(InterimResults interimResults, String nextRow) {
        StringBuilder newRow = new StringBuilder(interimResults.row);
        int newSplits = 0;

        for (int i = 0; i < nextRow.length(); i++) {
            if (nextRow.charAt(i) == '.') {
                if (interimResults.row.charAt(i) == 'S' || interimResults.row.charAt(i) == '|'
                        || newRow.charAt(i) == '|') {
                    newRow.setCharAt(i, '|');
                } else {
                    newRow.setCharAt(i, '.');
                }
            } else if (nextRow.charAt(i) == '^') {
                newRow.setCharAt(i, '^');
                if (interimResults.row.charAt(i) == '|') {
                    newRow.setCharAt(i - 1, '|');
                    newRow.setCharAt(i + 1, '|');
                    newSplits++;

                }
            }

        }
        System.out.println(newRow + " with totalSplits: " + (interimResults.totalSplits + newSplits));

        return new InterimResults(newRow.toString(), interimResults.totalSplits + newSplits);
    }

}

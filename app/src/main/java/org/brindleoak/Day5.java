package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Day5 {

    public static void main(final String[] args) throws IOException {
        final InputStream is = Day3Part2.class.getResourceAsStream("/data/day-5-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-5-input.txt");
        }

        List<String> rules = List.of();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            rules = br.lines().toList();
        }

        final long totalFreshCount = getFreshIngredientCount(rules);
        System.out.println("Total fresh ingredients = " + totalFreshCount);

    }

    public static boolean isIngredientFresh(final List<String> rules, final long ingredient) {

        for (String rule : rules) {
            if (rule.isEmpty()) {
                return false;
            }

            System.out.println("Processing rule: " + rule);
            String[] parts = rule.split("-");
            long firstIngredientInRange = Long.parseLong(parts[0]);
            long lastIngredientInRange = Long.parseLong(parts[1]);

            if (ingredient >= firstIngredientInRange && ingredient <= lastIngredientInRange) {
                return true;
            }
        }
        return false;
    }

    public static long getFreshIngredientCount(final List<String> rules) {
        int totalFreshCount = 0;

        for (String rule : rules) {
            if (rule.isEmpty() || rule.contains("-")) {
                continue;
            }

            long ingredient = Long.parseLong(rule);
            if (isIngredientFresh(rules, ingredient)) {
                totalFreshCount++;
            }

        }
        return totalFreshCount;
    }
}

package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Day5Part2 {

    record Range(long start, long end) {
    }

    public static void main(final String[] args) throws IOException {
        final InputStream is = Day5Part2.class.getResourceAsStream("/data/day-5-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-5-input.txt");
        }

        List<String> rules = List.of();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            rules = br.lines().toList();
        }

        final ArrayList<Range> ranges = getRanges(rules);
        final ArrayList<Range> consolidatedRanges = consolidateRanges(ranges);
        final long totalFreshCount = countRanges(consolidatedRanges);
        System.out.println("Total fresh ingredients = " + totalFreshCount);

    }

    public static ArrayList<Range> getRanges(final List<String> rules) {
        ArrayList<Range> ranges = new ArrayList<>();

        for (String rule : rules) {
            if (rule.isEmpty()) {
                return ranges;
            }

            String[] parts = rule.split("-");
            long firstIngredientInRange = Long.parseLong(parts[0]);
            long lastIngredientInRange = Long.parseLong(parts[1]);

            ranges.add(new Range(firstIngredientInRange, lastIngredientInRange));

        }
        return ranges;
    }

    public static ArrayList<Range> consolidateRanges(final ArrayList<Range> ranges) {
        ArrayList<Range> consolidatedRanges = new ArrayList<>(ranges);

        ArrayList<Range> overlappingRanges = findAnOverlappingRange(consolidatedRanges);

        while (overlappingRanges != null) {
            Range range1 = overlappingRanges.get(0);
            Range range2 = overlappingRanges.get(1);

            long newStart = Math.min(range1.start(), range2.start());
            long newEnd = Math.max(range1.end(), range2.end());

            consolidatedRanges.remove(range1);
            consolidatedRanges.remove(range2);
            consolidatedRanges.add(new Range(newStart, newEnd));
            System.out.println(
                    "  Consolidated " + range1 + " and " + range2 + " into " + new Range(newStart, newEnd));

            overlappingRanges = findAnOverlappingRange(consolidatedRanges);
        }

        return consolidatedRanges;
    }

    public static ArrayList<Range> findAnOverlappingRange(final ArrayList<Range> ranges) {
        for (int i = 0; i < ranges.size() - 1; i++) {
            Range range = ranges.get(i);

            for (int j = i + 1; j < ranges.size(); j++) {
                Range otherRange = ranges.get(j);

                if (range.start() <= otherRange.end() && range.end() >= otherRange.start()) {
                    System.out.println("Found overlapping ranges: " + range + " and " + otherRange);
                    return new ArrayList<>(List.of(range, otherRange));
                }
            }
        }

        return null;

    }

    public static long countRanges(final ArrayList<Range> ranges) {
        long totalCount = 0;

        for (Range range : ranges) {
            totalCount += (range.end() - range.start() + 1);
            System.out.println(
                    "   Range: " + range.start() + "-" + range.end() + " = " + (range.end() - range.start() + 1));
        }

        return totalCount;
    }

}

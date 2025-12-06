package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Day4 {
    static final int TARGET_LENGTH = 12;

    /**
     * @param selected  Characters already chosen for the final output
     * @param remaining Remaining characters still available for selection
     */
    static record BatterySelectionState(String selected, String remaining) {
    }

    public static void main(final String[] args) throws IOException {
        final InputStream is = Day3Part2.class.getResourceAsStream("/data/day-4-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-4-input.txt");
        }

        String diagram = new String(is.readAllBytes(), StandardCharsets.UTF_8).replace("\n", "");
        final int accessibleCount = computeAccessibleCount(diagram);

        System.out.println(accessibleCount);
    }

    public static int computeAccessibleCount(final String diagram) {
        int totalSize = diagram.length();
        int gridSize = (int) Math.sqrt(totalSize);

        int[] neighbors = new int[totalSize];

        for (int i = 0; i < diagram.length(); i++) {
            if (diagram.charAt(i) == '@') {
                if (!topRow(i, gridSize)) {
                    neighbors[i - gridSize]++;
                    if (!leftColumn(i, gridSize)) {
                        neighbors[i - gridSize - 1]++;
                    }
                    if (!rightColumn(i, gridSize)) {
                        neighbors[i - gridSize + 1]++;
                    }
                }
                if (!bottomRow(i, gridSize)) {
                    neighbors[i + gridSize]++;
                    if (!leftColumn(i, gridSize)) {
                        neighbors[i + gridSize - 1]++;
                    }
                    if (!rightColumn(i, gridSize)) {
                        neighbors[i + gridSize + 1]++;
                    }
                }
                if (!leftColumn(i, gridSize)) {
                    neighbors[i - 1]++;
                }
                if (!rightColumn(i, gridSize)) {
                    neighbors[i + 1]++;
                }
            }
        }

        int accessibleCount = 0;
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i] < 4 && diagram.charAt(i) == '@') {
                System.out.println("Position " + i + " has " + neighbors[i] + " accessible neighbors.");
                accessibleCount++;
            }
        }
        return accessibleCount;
    }

    static boolean topRow(int i, int gridSize) {
        return i < gridSize;
    }

    static boolean bottomRow(int i, int gridSize) {
        return i >= gridSize * (gridSize - 1);
    }

    static boolean leftColumn(int i, int gridSize) {
        return i % gridSize == 0;
    }

    static boolean rightColumn(int i, int gridSize) {
        return i % gridSize == gridSize - 1;
    }

    /**
     * @param batteries A list of battery strings
     * @return The total joltage computed from the batteries
     */
    public static long computeTotalJoltage(final List<String> batteries) {

        long totalJoltage = 0L;

        for (final String battery : batteries) {
            if (battery.length() < TARGET_LENGTH) {
                throw new IllegalArgumentException(
                        "Battery string length cannot be less than " + TARGET_LENGTH + ": " + battery);
            }

            BatterySelectionState ex = new BatterySelectionState("", battery);

            while (ex.selected().length() < TARGET_LENGTH) {
                ex = selectNextLargestBattery(ex);
            }

            final long joltage = Long.parseLong(ex.selected());
            totalJoltage += joltage;
        }

        return totalJoltage;
    }

    /**
     * @param ex An BatterySelectionState holding accumulated and remaining
     *           batteries
     * @return A new BatterySelectionState. The largest battery in remaining is
     *         added to selected. All
     *         batteries up to and including that largest one are removed from
     *         remaining.
     */
    static BatterySelectionState selectNextLargestBattery(final BatterySelectionState ex) {
        Objects.requireNonNull(ex, "BatterySelectionState cannot be null");

        final String remaining = ex.remaining();
        final String selected = ex.selected();

        final int totalLength = selected.length() + remaining.length();

        if (totalLength < TARGET_LENGTH) {
            throw new IllegalStateException("BatterySelectionState total length cannot be less than " + TARGET_LENGTH);
        }

        if (totalLength == TARGET_LENGTH) {
            return new BatterySelectionState(selected + remaining, "");
        }

        if (remaining.isEmpty()) {
            throw new IllegalStateException("BatterySelectionState has no remaining batteries");
        }

        int b = 0;

        // We need to ensure enough characters are left to be able to reach
        // TARGET_LENGTH
        int upperLimit = totalLength - TARGET_LENGTH + 1;

        for (int i = 0; i < upperLimit; i++) {
            if (remaining.charAt(i) > remaining.charAt(b)) {
                b = i;
            }
        }

        return new BatterySelectionState(selected + remaining.charAt(b), remaining.substring(b + 1));
    }
}

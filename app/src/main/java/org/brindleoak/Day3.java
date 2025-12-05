package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Day3 {
    public static void main(String[] args) throws IOException {

        InputStream is = Day3Part2.class.getResourceAsStream("/data/day-3-input.txt");
        if (is == null) {
            throw new IllegalArgumentException("File not found: /data/day-3-input.txt");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            List<String> rules = br.lines().toList();

            long totalJoltage = 0L;

            for (String rule : rules) {
                long joltage = largestJoltage(rule);
                System.out.println(rule + " -->" + joltage);
                totalJoltage += joltage;
            }

            System.out.println("Total joltage = " + totalJoltage);
        }
    }

    /**
     * @param bank Represents a number of batteries
     * @return The largest joltage that can be obtained from two batteries
     */
    static long largestJoltage(String bank) {
        int batteryLast = bank.length() - 1;
        int battery1 = selectBattery(bank, 0, batteryLast - 1);
        int battery2 = selectBattery(bank, battery1 + 1, batteryLast);

        return Long.parseLong(String.valueOf(bank.charAt(battery1))) * 10
                + Long.parseLong(String.valueOf(bank.charAt(battery2)));
    }

    /**
     * @param bank  Represents a number of batteries
     * @param start Index of the first battery to consider
     * @param end   Index of the last battery to consider
     * @return Index of the battery with the largest value in the start-end range
     */
    static int selectBattery(String bank, int start, int end) {
        int battery = start;

        for (int i = start; i <= end; i++) {
            if (bank.charAt(i) > bank.charAt(battery))
                battery = i;
        }

        return battery;
    }
}

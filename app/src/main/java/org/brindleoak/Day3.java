package org.brindleoak;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day3 {
    public static void main(String[] args) throws IOException {

        List<String> rules = Files
                .readAllLines(Path
                        .of("/Users/simon.lewis/GitHub/AdventOfCode2025/app/src/main/java/org/brindleoak/day-3-input.txt"));

        long totalJoltage = 0L;

        for (String rule : rules) {
            Long joltage = largestJoltage(rule);
            System.out.println(rule + " -->" + joltage);

            totalJoltage += joltage;
        }

        System.out.println("Total joltage = " + totalJoltage);
    }

    private static long largestJoltage(String bank) {
        int batteryLast = bank.length() - 1;
        int battery1 = selectBattery(bank, 0, batteryLast - 1);
        int battery2 = selectBattery(bank, battery1 + 1, batteryLast);

        return Long.parseLong(String.valueOf(bank.charAt(battery1))) * 10
                + Long.parseLong(String.valueOf(bank.charAt(battery2)));
    }

    private static int selectBattery(String bank, int start, int end) {
        int battery = start;

        for (int i = start; i <= end; i++) {
            if (bank.charAt(i) > bank.charAt(battery))
                battery = i;
        }

        return battery;
    }
}

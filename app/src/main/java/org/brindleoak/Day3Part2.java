package org.brindleoak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Day3Part2 {
    static record Extraction(String acc, String rem) {
    }

    public static void main(String[] args) throws IOException {
        InputStream is = Day3Part2.class.getResourceAsStream("/data/day-3-input.txt");
        if (is == null) {
            throw new IllegalArgumentException("File not found");
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            List<String> rules = br.lines().toList();
            long totalJoltage = computeTotalJoltage(rules);
            System.out.println("Total joltage = " + totalJoltage);
        }
    }

    public static long computeTotalJoltage(List<String> rules) {
        long totalJoltage = 0L;

        for (String rule : rules) {
            Extraction ex = new Extraction("", rule);

            while (ex.acc().length() < 12) {
                ex = removeSmallest(ex);
            }

            long joltage = Long.parseLong(ex.acc());
            totalJoltage += joltage;
        }

        return totalJoltage;
    }

    static Extraction removeSmallest(Extraction ex) {
        if (ex == null) {
            throw new IllegalArgumentException("Extraction cannot be null");
        }

        String bank = ex.rem();
        String acc = ex.acc();

        if (acc.length() + bank.length() < 12) {
            throw new IllegalArgumentException("Extraction total length cannot be less than 12");
        }

        if (acc.length() + bank.length() == 12) {
            return new Extraction(acc + bank, "");
        }

        if (bank.length() == 0) {
            throw new IllegalArgumentException("Extraction cannot have an empty bank");
        }
        int b = 0;

        for (int i = 0; i < bank.length() - 11 + acc.length(); i++) {
            if (bank.charAt(i) > bank.charAt(b))
                b = i;
        }

        return new Extraction(acc + bank.charAt(b), bank.substring(b + 1));
    }
}

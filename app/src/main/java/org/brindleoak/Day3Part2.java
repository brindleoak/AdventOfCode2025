package org.brindleoak;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

record Extraction(String acc, String rem) {}

public class Day3Part2 {
    public static void main(String[] args) throws IOException {

        List<String> rules = Files
                .readAllLines(Path
                        .of("/Users/simon.lewis/GitHub/AdventOfCode2025/app/src/main/java/org/brindleoak/day-3-input.txt"));

        long totalJoltage = 0L;

        for (String rule : rules) {
            var ex = new Extraction("", rule);

            String startRule = rule;

            while (ex.acc().length() < 12)
                ex = removeSmallest(ex);
            long joltage = Long.parseLong(ex.acc());
            System.out.println(startRule + " -->" + joltage);

            totalJoltage +=joltage;
        }

        System.out.println("Total joltage = " + totalJoltage);
    }

    private static Extraction removeSmallest(Extraction ex) {
        String bank = ex.rem();
        String acc = ex.acc();

        if (acc.length() + bank.length() < 12) {
            System.out.println("Oops. error. Batteries left < 12");
            System.exit(1);
        }

        if (acc.length() + bank.length() == 12) {
            return new Extraction(acc + bank, "");
        }

        int b = 0;

        for (int i=0; i < bank.length() - 11 + acc.length(); i++) {
            if (bank.charAt(i) > bank.charAt(b)) b = i;
        }

        return new Extraction(acc + bank.charAt(b), bank.substring(b + 1));
    }
}

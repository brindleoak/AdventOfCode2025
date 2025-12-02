package org.brindleoak;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day2 {
    public static void main(String[] args) throws IOException {

        List<String> rules = Files
                .readAllLines(Path
                        .of("/home/simon/GitHub/AdventOfCode2025/app/src/main/java/org/brindleoak/day-2-input.txt"));

        String rule = rules.getFirst();
        String[] ranges = rule.split(",");

        BigInteger sumInvalidIds = BigInteger.ZERO;

        for (String range : ranges) {
            List<String> numbers = extractRange(range);

            System.out.println("Checking range: " + range);
            for (String n : numbers) {
                if (!isValid2(n)) {
                    System.out.println(n + " is invalid");

                    sumInvalidIds = sumInvalidIds.add(new BigInteger(n));
                }
            }
        }

        System.out.println("Sum of invalid IDs: " + sumInvalidIds);

    }

    public static List<String> extractRange(String range) {
        String[] parts = range.split("-");

        BigInteger start = new BigInteger(parts[0]);
        BigInteger end = new BigInteger(parts[1]);

        List<BigInteger> listInts = new ArrayList<>();

        for (BigInteger n = start; n.compareTo(end) <= 0; n = n.add(BigInteger.ONE)) {
            listInts.add(n);
        }
        List<String> list = listInts.stream().map(String::valueOf).toList();
        return list;
    }

    // part 1 solution
    public static Boolean isValid(String n) {
        if (n.startsWith("0"))
            return true;

        int len = n.length();
        if (len % 2 != 0) {
            return true;
        }

        int mid = len / 2;

        if (n.substring(0, mid).equals(n.substring(mid))) {
            return false;
        }

        return true;
    }

    // part 2 solution
    public static Boolean isValid2(String n) {
        if (n.startsWith("0"))
            return true;

        int len = n.length();
        for (int patternLen = 1; patternLen <= len / 2; patternLen++) {
            if (len % patternLen == 0) {
                if (checkPattern(n, patternLen)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean checkPattern(String s, int patternLength) {
        String pattern = s.substring(0, patternLength);
        return pattern.repeat(s.length() / patternLength).equals(s);
    }

}

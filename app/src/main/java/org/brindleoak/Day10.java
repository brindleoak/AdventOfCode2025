package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day10 {

    public record Machine(int display, int[] buttons) {
    }

    public static void main(String[] args) throws Exception {

        final InputStream is = Day9.class.getResourceAsStream("/data/day-10-input.txt.test");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-10-input.txt.test");
        }

        List<String> inputRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            inputRecords = br.lines().toList();
        }

        List<Machine> machines = setUpMachines(inputRecords);

        long startTime = System.currentTimeMillis();

        long result = solve(machines);
        System.out.println("Result: " + result);

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed time: " + elapsedTime + " ms");
    }

    static List<Machine> setUpMachines(List<String> inputRecords) {
        List<Machine> machines = new ArrayList<>();
        for (String record : inputRecords) {
            String[] parts = record.split(" ");

            var displayString = parts[0]
                    .replace("[", "")
                    .replace("]", "")
                    .replace(".", "0")
                    .replace("#", "1");
            int display = Integer.parseInt(displayString, 2);
            int[] buttons = { 1, 2, 3 };

            machines.add(new Machine(display, buttons));
        }

        return machines;
    }

    static long solve(List<Machine> machines) {

        return 0L;
    }

}

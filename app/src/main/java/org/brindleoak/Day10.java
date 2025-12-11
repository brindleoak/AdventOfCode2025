package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day10 {

    public record Machine(int display, int[] buttons) {
        @Override
        public String toString() {
            String display = Integer.toBinaryString(this.display);
            ArrayList<String> buttonString = new ArrayList<>();
            for (int b : this.buttons) {
                buttonString.add(Integer.toBinaryString(b));
            }

            return display + " [" + this.display + "] " + buttonString;
        }
    }

    public static void main(String[] args) throws Exception {

        final InputStream is = Day9.class.getResourceAsStream("/data/day-10-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-10-input.txt");
        }

        List<String> inputRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            inputRecords = br.lines().toList();
        }

        List<Machine> machines = setUpMachines(inputRecords);

        for (Machine m : machines) {
            System.out.println(m.toString());
        }

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

            List<Integer> buttonArrayList = new ArrayList<>();

            for (int i = 1; i < parts.length - 1; i++) {
                var digits = parts[i].replace("(", "").replace(")", "").split(",");
                int button = 0;

                for (int j = 0; j < digits.length; j++) {
                    long base10 = (long) Math.pow(10, displayString.length() - Integer.parseInt(digits[j]) - 1);
                    long binary = Long.parseLong(String.valueOf(base10), 2);
                    button += binary;
                }
                buttonArrayList.add(button);
            }
            ;

            int[] buttons = new int[buttonArrayList.size()];
            int i = 0;
            for (Integer b : buttonArrayList) {
                buttons[i++] = b;
            }

            machines.add(new Machine(display, buttons));
        }

        return machines;
    }

    static long solve(List<Machine> machines) {
        long totalNumberOfButtons = 0;

        for (Machine machine : machines) {
            int numberOfButtons = solveMachine(machine);
            totalNumberOfButtons += numberOfButtons;
        }

        return totalNumberOfButtons;
    }

    static int solveMachine(Machine machine) {

        // given the button effect is XOR we know there is no point in ever hitting the
        // same button twice. So we can just try all combinations of buttons: 2^n where
        // n is the number of buttons
        double combinations = Math.pow(2, machine.buttons.length);
        int lowestNumberOfButtons = Integer.MAX_VALUE;

        for (int i = 0; i < combinations; i++) {
            int display = 0;
            int numberOfButtons = 0;
            for (int j = 0; j < machine.buttons.length; j++) {
                if (((i >> j) & 1) == 1) {
                    ++numberOfButtons;
                    display = display ^ machine.buttons[j]; // press the button
                }
            }

            if (display == machine.display) {
                if (numberOfButtons < lowestNumberOfButtons) {
                    lowestNumberOfButtons = numberOfButtons;
                }
            }
        }

        return lowestNumberOfButtons;
    }

}

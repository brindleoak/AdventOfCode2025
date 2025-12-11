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

        // print machines
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
                // System.out.println("-----------------------------------");
                // System.out.println(parts[i]);
                var digits = parts[i].replace("(", "").replace(")", "").split(",");
                int button = 0;
                for (int j = 0; j < digits.length; j++) {

                    long base10 = (long) Math.pow(10, displayString.length() - Integer.parseInt(digits[j]) - 1);
                    long binary = Long.parseLong(String.valueOf(base10), 2);
                    button += binary;
                    // System.out
                    // .println("length: " + displayString.length() + " digit: " + "digit " +
                    // digits[j] + " = "
                    // + binary);
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
            // System.out.println("------------------------------------------------------------");

            int numberOfButtons = solveMachine(machine);
            totalNumberOfButtons += numberOfButtons;

            // System.out.println("Processed machine. Result = " + numberOfButtons + "
            // buttons");
        }

        return totalNumberOfButtons;
    }

    static int solveMachine(Machine machine) {

        double combinations = Math.pow(2, machine.buttons.length);
        int lowestNumberOfButtons = Integer.MAX_VALUE;

        for (int i = 0; i < combinations; i++) {
            // System.out.println("Processing combination " + Integer.toBinaryString(i)
            // + " (looking for result " + machine.display + ")");
            int display = 0;
            int numberOfButtons = 0;
            for (int j = 0; j < machine.buttons.length; j++) {

                if (((i >> j) & 1) == 1) {
                    ++numberOfButtons;
                    var newDisplay = display ^ machine.buttons[j];
                    // System.out.println(
                    // " Processing button " + j + " with value " +
                    // Integer.toBinaryString(machine.buttons[j])
                    // + " " + display + "-->" + newDisplay);
                    display = newDisplay;
                }
            }
            if (display == machine.display) {
                // System.out.println("Found combination " + Integer.toBinaryString(i) + " with
                // result " + display);
                if (numberOfButtons < lowestNumberOfButtons) {
                    lowestNumberOfButtons = numberOfButtons;
                }
            }
        }

        return lowestNumberOfButtons;
    }

}

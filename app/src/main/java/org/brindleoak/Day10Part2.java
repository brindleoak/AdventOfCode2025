package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day10Part2 {

    public record Machine(int[][] buttons, int[] joltage) {
    }

    public record State(boolean solved, int[] joltage, int numberOfPresses) {
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

            List<Integer> buttonArrayList = new ArrayList<>();

            for (int i = 0; i < parts.length - 1; i++) {
                var digits = parts[i].replace("(", "").replace(")", "").split(",");

                for (int j = 0; j < digits.length; j++) {
                    buttonArrayList.add(Integer.parseInt(digits[j]));
                }
            }

            int[] buttons = new int[buttonArrayList.size()];
            int i = 0;
            for (Integer b : buttonArrayList) {
                buttons[i++] = b;
            }

            String[] joltageStrings = parts[parts.length - 1].replace("{", "").replace("}", "").split(",");
            int[] joltages = new int[joltageStrings.length];
            for (int j = 0; j < joltageStrings.length; j++) {
                joltages[j] = Integer.parseInt(joltageStrings[j]);
            }
            machines.add(new Machine(display, buttons, joltages));
        }

        return machines;
    }

    static long solve(List<Machine> machines) {
        long totalNumberOfButtons = 0;

        for (Machine machine : machines) {
            State state = new State(false, new int[machine.joltage.length], 0);
            State newState = solveMachine(machine, state);

            if (newState.solved == false) {
                System.out.println("Could not solve machine: " + machine.toString());
                throw new IllegalStateException("Could not solve machine" + machine.toString());
            }
            totalNumberOfButtons += state.numberOfPresses;
        }

        return totalNumberOfButtons;
    }

    static State solveMachine(Machine machine, State state) {
        System.out.println("Solving machine with state: " + state.toString());

        if (state.solved) {
            return state;
        }

        final int numberOfButtons = machine.buttons.length;

        int lowestNumberOfButtons = Integer.MAX_VALUE;

        for (int i = 0; i < numberOfButtons; i++) {

            int[] newJoltage = pushButton(machine, i, state.joltage);
            if (newJoltage == null) { // this button push blows the joltagetotal. Skip it.
                continue;
            }

            State newState = new State(false, newJoltage, state.numberOfPresses + 1);

            State resultState = solveMachine(machine, new State(false, newJoltage, state.numberOfPresses + 1));

            if (resultState.solved) {
                if (newState.numberOfPresses < lowestNumberOfButtons) {
                    lowestNumberOfButtons = newState.numberOfPresses;
                }
            }
        }

        return new State(true, state.joltage, lowestNumberOfButtons);
    }

    static int[] pushButton(Machine machine, int buttonId, int[] joltage) {
        int[] newJoltage = Arrays.copyOf(joltage, joltage.length);
        int button = machine.buttons[buttonId];
        System.out.println("Pushing button: " + button);

        for (int i = 0; i < joltage.length; i++) {
            if ((button >> i & 1) == 1) {
                System.out.println("  Increasing joltage at position " + i + " from " + newJoltage[i] + " to "
                        + (newJoltage[i] + 1) + ". New value " + machine.joltage.toString());
                newJoltage[i] += 1;
                if (newJoltage[i] > machine.joltage[i]) {
                    return null;
                }
            }
        }
        return newJoltage;
    }

}

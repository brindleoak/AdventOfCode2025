package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day10Part2 {
    static int lowestNumberOfPresses = Integer.MAX_VALUE;

    public record Machine(int[][] buttons, int[] joltage) {
        @Override
        public String toString() {
            return "Machine{" +
                    "buttons=" + Arrays.deepToString(buttons) +
                    ", joltage=" + Arrays.toString(joltage) +
                    '}';
        }
    }

    public record State(int[] joltage, int numberOfPresses) {
        @Override
        public String toString() {
            return "State{" +
                    ", joltage=" + Arrays.toString(joltage) +
                    ", numberOfPresses=" + numberOfPresses +
                    '}';
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

            // process buttons
            int[][] buttons = new int[parts.length - 2][];

            for (int i = 1; i < parts.length - 1; i++) {
                var positionArray = parts[i].replace("(", "").replace(")", "").split(",");

                int[] positions = new int[positionArray.length];
                for (int j = 0; j < positions.length; j++) {
                    positions[j] = Integer.parseInt(positionArray[j]);
                }
                buttons[i - 1] = positions;
            }

            // process joltages
            String[] joltageStrings = parts[parts.length - 1].replace("{", "").replace("}", "").split(",");
            int[] joltages = new int[joltageStrings.length];
            for (int j = 0; j < joltageStrings.length; j++) {
                joltages[j] = Integer.parseInt(joltageStrings[j]);
            }
            machines.add(new Machine(buttons, joltages));
        }

        return machines;
    }

    static long solve(List<Machine> machines) {
        long totalNumberOfButtons = 0;

        for (Machine machine : machines) {
            // Reset the global minimum for each machine
            lowestNumberOfPresses = Integer.MAX_VALUE;

            // Start the optimized recursive search from button index 0
            // Initial state: currentTotals={0,0,...}, currentTotalPresses=0
            solveMachineRecursive(machine,
                    0, // Start with button index 0
                    new int[machine.joltage.length], // Current totals (starts at 0)
                    0); // Current total number of presses

            System.out.println("Lowest number of presses for machine: " + lowestNumberOfPresses);

            totalNumberOfButtons += lowestNumberOfPresses;

            // Assuming you want to process all machines eventually:
            // return totalNumberOfButtons; // <-- Remove this line to process all machines

        }
        return totalNumberOfButtons;
    }

    /**
     * Optimized Backtracking Solver for the Multiset Additive Problem.
     * The recursion depth is fixed to the number of buttons (N).
     *
     * @param buttonIndex         The index of the button currently being tested (0
     *                            to N-1).
     * @param currentTotals       The cumulative effect of button presses so far.
     * @param currentTotalPresses The sum of all button presses so far.
     */
    static void solveMachineRecursive(
            Machine machine,
            int buttonIndex,
            int[] currentTotals,
            int currentTotalPresses) {

        final int numberOfTotals = machine.joltage.length;
        final int numberOfButtons = machine.buttons.length;

        // --- 1. BASE CASE: All buttons have been processed (depth N) ---
        if (buttonIndex == numberOfButtons) {
            // Check if the final state matches the target
            if (Arrays.equals(currentTotals, machine.joltage)) {
                if (currentTotalPresses < lowestNumberOfPresses) {
                    lowestNumberOfPresses = currentTotalPresses;
                    System.out.println("New lowest number of presses: " + lowestNumberOfPresses);
                }
            }
            return;
        }

        // --- 2. RECURSIVE STEP: Iterate on the press count (x_i) for the current
        // button ---

        // Find the absolute maximum number of times the current button can be pressed.
        // This is the CRITICAL PRUNING STEP that stops the infinite loop.
        int maxPressesForCurrentButton = getOptimalMaxPresses(machine, buttonIndex, currentTotals);

        int[] buttonEffect = machine.buttons[buttonIndex];

        // Loop through the number of times (x_i) we press buttonIndex: 0, 1, 2, ... max
        for (int presses = 0; presses <= maxPressesForCurrentButton; presses++) {

            // A. PRUNING CHECK (Minimum Presses Found):
            // If the total presses already meets or exceeds the best solution found, stop.
            if (currentTotalPresses + presses >= lowestNumberOfPresses) {
                return; // Stop exploring this button and all subsequent buttons in this branch
            }

            // B. CHOOSE & CALCULATE NEW STATE:
            int newTotalPresses = currentTotalPresses + presses;

            // Create the new totals for the recursive call. (Clone is key for backtracking)
            int[] newTotals = currentTotals.clone();

            // Apply the effect of pressing the button 'presses' times
            boolean isOvershoot = false;
            for (int k = 0; k < numberOfTotals; k++) {
                // The effect of one press on counter k
                int effect = 0;
                // Check if the button affects counter k (The indices in the button array ARE
                // the counter IDs)
                for (int counterIndex : buttonEffect) {
                    if (counterIndex == k) {
                        effect = 1; // Assuming each listed index means +1 to that counter
                        break;
                    }
                }

                newTotals[k] += presses * effect; // Apply the total press count effect

                // IMMEDIATE PRUNING CHECK (Target Exceeded)
                if (newTotals[k] > machine.joltage[k]) {
                    isOvershoot = true;
                    break;
                }
            }

            if (isOvershoot) {
                // If this press count caused an overshoot, then any higher count will too.
                // Stop the inner 'for (presses...)' loop and move to the next button (or
                // terminate branch).
                continue;
            }

            // C. RECURSE: Move to the next button (buttonIndex + 1)
            solveMachineRecursive(machine,
                    buttonIndex + 1,
                    newTotals,
                    newTotalPresses);

            // D. BACKTRACK: No explicit undo needed because we used newTotals.clone()
        }
    }

    /**
     * Determines the maximum safe number of times to press the current button
     * (B_i).
     *
     * @return The highest press count before any counter is guaranteed to overshoot
     *         the target.
     */
    private static int getOptimalMaxPresses(Machine machine, int buttonIndex, int[] currentTotals) {
        int maxPresses = Integer.MAX_VALUE;
        int[] buttonEffect = machine.buttons[buttonIndex];

        // Loop through the affected counters to find the tightest constraint
        for (int counterId : buttonEffect) {

            int targetValue = machine.joltage[counterId];
            int currentValue = currentTotals[counterId];

            // If the counter is already at or above the target, this button cannot be used
            // further
            if (currentValue >= targetValue) {
                return 0;
            }

            // How many more presses are safe? The effect on this counter is +1 per press.
            int remainingNeeded = targetValue - currentValue;

            // Update the maximum allowed presses to the tightest constraint found so far
            maxPresses = Math.min(maxPresses, remainingNeeded);
        }

        // If the button affects no counters, it can be pressed 0 times (or maxPresses
        // remains MAX_VALUE, which implies it's useless)
        return (maxPresses == Integer.MAX_VALUE) ? 0 : maxPresses;
    }

}

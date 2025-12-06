package org.brindleoak;

import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

public class Day4Part2 {

    public static void main(final String[] args) throws IOException {
        final InputStream is = Day3Part2.class.getResourceAsStream("/data/day-4-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-4-input.txt");
        }

        String diagram = new String(is.readAllBytes(), StandardCharsets.UTF_8).replace("\n", "");
        final int unAccessibleCount = computeUnAccessibleCount(diagram);

        System.out.println(unAccessibleCount);
    }

    record Offset(int dy, int dx) {
    }

    private final static Offset[] NEIGHBOR_OFFSETS = {
            new Offset(-1, -1),
            new Offset(-1, 0),
            new Offset(-1, 1),
            new Offset(0, -1),
            new Offset(0, 1),
            new Offset(1, -1),
            new Offset(1, 0),
            new Offset(1, 1)
    };

    public static int computeUnAccessibleCount(String diagram) {
        String currentDiagram = diagram;
        String nextDiagram = removeAccessible(diagram);

        while (currentDiagram != nextDiagram) {
            currentDiagram = nextDiagram;
            nextDiagram = removeAccessible(currentDiagram);
        }

        int originalCount = 0;
        int finalCount = 0;
        for (int i = 0; i < diagram.length(); i++) {
            if (diagram.charAt(i) == '@') {
                originalCount++;
            }
            if (currentDiagram.charAt(i) == '@') {
                finalCount++;
            }
        }
        return originalCount - finalCount;
    }

    public static String removeAccessible(String diagram) {
        int totalSize = diagram.length();
        int gridSize = (int) Math.sqrt(totalSize);

        int[] neighbors = new int[totalSize];

        String updatedDiagram = diagram;

        for (int i = 0; i < diagram.length(); i++) {
            if (diagram.charAt(i) != '@') {
                continue;
            }

            int x = i % gridSize;
            int y = i / gridSize;

            for (Offset offset : NEIGHBOR_OFFSETS) {
                int newX = x + offset.dx();
                int newY = y + offset.dy();
                if (newX >= 0 && newX < gridSize && newY >= 0 && newY < gridSize) {
                    if (diagram.charAt(newY * gridSize + newX) == '@') {
                        neighbors[i]++;
                    }
                }
            }
        }

        int itemsRemoved = 0;
        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i] < 4 && diagram.charAt(i) == '@') {
                updatedDiagram = updatedDiagram.substring(0, i) + '.' + diagram.substring(i + 1);
                itemsRemoved++;
            }
        }

        System.out.println("Items removed this pass: " + itemsRemoved);
        // System.out.println(diagram);
        return updatedDiagram;
    }
}

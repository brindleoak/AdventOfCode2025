package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day7Part2 {
    public static void main(String[] args) throws Exception {
        final InputStream is = Day6Part2.class.getResourceAsStream("/data/day-7-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-7-input.txt");
        }

        List<String> rules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            rules = br.lines().toList();
        }

        int rowCount = rules.size();
        int colCount = rules.get(0).length();
        char[][] grid = new char[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            grid[i] = rules.get(i).toCharArray();
        }

        long[] currentRow = new long[colCount];

        for (int i = 0; i < colCount; i++) {
            if (grid[0][i] == 'S')
                currentRow[i] = 1;
        }

        for (int r = 1; r < rowCount; r++) {
            long[] nextRow = new long[colCount];

            for (int c = 0; c < colCount; c++) {
                long currentColumnCount = currentRow[c];

                char cell = grid[r][c];
                if (cell == '.') {
                    nextRow[c] = nextRow[c] + currentColumnCount;
                } else if (cell == '^') {
                    nextRow[c - 1] = nextRow[c - 1] + currentColumnCount;
                    nextRow[c + 1] = nextRow[c + 1] + currentColumnCount;
                }
            }
            currentRow = nextRow;
        }

        long totalPaths = 0;
        for (long i : currentRow)
            totalPaths = totalPaths + i;

        System.out.println("Total paths - " + totalPaths);
    }
}

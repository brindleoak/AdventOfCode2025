package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day8Part2 {

    static class Point {
        private static int nextCircuit = 0;

        final int x;
        final int y;
        final int z;
        int circuit;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.circuit = nextCircuit++;
        }
    }

    record Pair(Point point1, Point point2, double distance) {
        static double distance(Point a, Point b) {
            return Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2);
        }

        static Pair of(Point a, Point b) {
            return new Pair(a, b, distance(a, b));
        }
    }

    public static void main(String[] args) throws Exception {

        final InputStream is = Day8.class.getResourceAsStream("/data/day-8-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-8-input.txt");
        }

        List<String> inputRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            inputRecords = br.lines().toList();
        }

        long result = solve(inputRecords);
        System.out.println("Result: " + result);
    }

    static long solve(List<String> rules) {
        List<Point> points = buildPoints(rules);
        List<Pair> pairs = buildPairs(points);

        int newCircuit = Point.nextCircuit;
        long res = 0;

        for (Pair pair : pairs) {

            int circuit1 = pair.point1.circuit;
            int circuit2 = pair.point2.circuit;

            if (circuit1 != circuit2) {
                mergeCircuits(points, circuit1, circuit2, newCircuit++);
            }

            int firstCircuit = points.getFirst().circuit;
            boolean allSameCircuit = true;

            for (Point p : points) {
                if (p.circuit != firstCircuit) {
                    allSameCircuit = false;
                    break;
                }
            }

            if (allSameCircuit) {
                res = (long) pair.point1.x * pair.point2.x;
                break;
            }
        }

        return res;
    }

    static List<Point> buildPoints(List<String> rules) {
        List<Point> points = new ArrayList<>();

        for (String rule : rules) {
            String[] parts = rule.split(",");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);

            points.add(new Point(x, y, z));
        }

        return points;
    }

    static List<Pair> buildPairs(List<Point> points) {
        List<Pair> pairs = new ArrayList<>();

        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                pairs.add(Pair.of(points.get(i), points.get(j)));
            }
        }

        pairs.sort(Comparator.comparingDouble(p -> p.distance));

        return pairs;
    }

    static List<Point> getPointsByCircuit(int circuit, List<Point> points) {
        List<Point> circuitPoints = new ArrayList<>();
        for (Point p : points) {
            if (p.circuit == circuit) {
                circuitPoints.add(p);
            }
        }
        return circuitPoints;
    }

    static void mergeCircuits(List<Point> points, int oldCircuit1, int oldCircuit2, int newCircuit) {
        for (Point p : points) {
            if (p.circuit == oldCircuit1 || p.circuit == oldCircuit2) {
                p.circuit = newCircuit;
            }
        }
    }
}

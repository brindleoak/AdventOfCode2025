package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day8 {

    record Point(int x, int y, int z, int circuit) {
    }

    record Pair(Point point1, Point point2, double distance) {
        static double distance(Point a, Point b) {
            return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2) + Math.pow(a.z - b.z, 2));
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

        List<String> rules = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            rules = br.lines().toList();
        }

        int result = solve(rules);
        System.out.println("Result: " + result);
    }

    static int solve(List<String> rules) {
        List<Point> points = buildPoints(rules);
        List<Pair> pairs = buildPairs(points);

        int newCircuit = 5000;

        int leftToProcess = 999;
        for (Pair pair : pairs) {
            System.out.println("Pairs left to process " + leftToProcess);

            int circuit1 = 0;
            for (Point p : points) {
                if (p.x == pair.point1.x && p.y == pair.point1.y && p.z == pair.point1.z) {
                    circuit1 = p.circuit;
                    break;
                }
            }
            int circuit2 = 0;
            for (Point p : points) {
                if (p.x == pair.point2.x && p.y == pair.point2.y && p.z == pair.point2.z) {
                    circuit2 = p.circuit;
                    break;
                }
            }

            System.out.println(
                    "Considering pair: " + pair.point1 + " <-> " + pair.point2 + " distance: " + pair.distance);

            System.out.println("  Circuits: " + circuit1 + " and " + circuit2);
            if (circuit1 == circuit2) {
                System.out.println("    Same circuit, skipping");
                continue;
            }

            else {
                System.out.println(" to be merged into " + newCircuit);
                points = updateCircuit(points, circuit1, circuit2, newCircuit++);

            }
            leftToProcess--;
            if (leftToProcess == 0) {
                break;
            }
        }

        Map<Integer, Integer> circuitSizes = new HashMap<>();
        for (Point p : points) {
            int circuit = p.circuit;

            Integer c = circuitSizes.get(circuit);
            if (c == null) {
                circuitSizes.put(circuit, 1);
            } else {
                circuitSizes.replace(circuit, c.intValue() + 1);
            }

        }

        int res = 0;
        var biggest = circuitSizes.values().stream().sorted(Comparator.reverseOrder()).toList().subList(0, 3);
        for (Integer size : biggest) {
            res += size;
            System.out.println(" Biggest circuit size: " + size);

        }
        return res;
    }

    static List<Point> buildPoints(List<String> rules) {
        List<Point> points = new ArrayList<>();
        int circuit = 0;

        for (String rule : rules) {
            String[] parts = rule.split(",");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);

            points.add(new Point(x, y, z, circuit++));
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

    static List<Point> updateCircuit(List<Point> points, int oldCircuit1, int oldCircuit2, int newCircuit) {
        List<Point> updatedPoints = new ArrayList<>();
        for (Point p : points) {
            if (p.circuit == oldCircuit1 || p.circuit == oldCircuit2) {
                updatedPoints.add(new Point(p.x, p.y, p.z, newCircuit));
                System.out.println(
                        "    Updating point " + p + " from circuit " + p.circuit + " to circuit " + newCircuit);
            } else {
                updatedPoints.add(p);
            }
        }
        return updatedPoints;
    }

}

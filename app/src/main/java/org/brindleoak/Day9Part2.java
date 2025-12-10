package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day9Part2 {

    public record Point(int x, int y, boolean isRed) {
        public static Point redPoint(String s) {
            String[] parts = s.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            return new Point(x, y, true);
        }

        public static Point greenPoint(String s) {
            String[] parts = s.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            return new Point(x, y, false);
        }
    }

    public static void main(String[] args) throws Exception {

        final InputStream is = Day9.class.getResourceAsStream("/data/day-9-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-9-input.txt");
        }

        List<String> inputRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            inputRecords = br.lines().toList();
        }

        long startTime = System.currentTimeMillis();

        long result = solve(inputRecords);
        System.out.println("Result: " + result);

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed time: " + elapsedTime + " ms");
    }

    static long solve(List<String> rules) {
        List<Point> points = new ArrayList<>();

        Point previousPoint = null;

        for (String rule : rules) {
            Point currentPoint = Point.redPoint(rule);
            points.add(currentPoint);
            if (previousPoint != null) {
                joinPoints(points, previousPoint, currentPoint);
            }
            previousPoint = currentPoint;
        }

        joinPoints(points, points.getFirst(), points.getLast());

        long maxArea = 0;

        for (int i = 0; i < points.size(); i++) {
            var point1 = points.get(i);

            for (int j = i + 1; j < points.size(); j++) {
                var point2 = points.get(j);

                int side1 = Math.abs(point1.x - point2.x) + 1;
                int side2 = Math.abs(point1.y - point2.y) + 1;

                long area = (long) side1 * side2;

                if (area > maxArea) {
                    maxArea = area;
                }
            }
        }

        return maxArea;
    }

    static void joinPoints(List<Point> points, Point previousPoint, Point currentPoint) {
        if (previousPoint.x == currentPoint.x) {
            int minY = Math.min(previousPoint.y, currentPoint.y);
            int maxY = Math.max(previousPoint.y, currentPoint.y);

            for (int i = minY + 1; i < maxY; i++) {
                points.add(new Point(previousPoint.x, i, false));
            }
        } else {
            int minX = Math.min(previousPoint.x, currentPoint.x);
            int maxX = Math.max(previousPoint.x, currentPoint.x);

            for (int i = minX + 1; i < maxX; i++) {
                points.add(new Point(i, previousPoint.y, false));
            }
        }
    }

}

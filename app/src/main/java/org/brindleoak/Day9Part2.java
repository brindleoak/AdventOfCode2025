package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day9Part2 {

    public record Point(int x, int y, char colour) {
        public static Point newRedPoint(String s) {
            String[] parts = s.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            return new Point(x, y, 'R');
        }

        public static Point newGreenPoint(String s) {
            String[] parts = s.split(",");
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            return new Point(x, y, 'G');
        }

        public static Point newInsidePoint(int x, int y) {
            return new Point(x, y, 'I');
        }

        public boolean doesPointExist(Point point, List<Point> points) {
            for (Point p : points) {
                if (p.x == point.x && p.y == point.y) {
                    return true;
                }
            }
            return false;
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

        Point firstPoint = null;
        Point previousPoint = null;

        for (String rule : rules) {
            Point currentPoint = Point.newRedPoint(rule);

            if (firstPoint == null) {
                firstPoint = currentPoint;
            }

            points.add(currentPoint);

            if (previousPoint != null) {
                joinPoints(points, previousPoint, currentPoint);
            }

            previousPoint = currentPoint;
        }

        joinPoints(points, firstPoint, previousPoint);

        long maxArea = 0;

        for (int i = 0; i < points.size(); i++) {
            var point1 = points.get(i);
            if (point1.colour != 'R') {
                continue;
            }

            for (int j = i + 1; j < points.size(); j++) {
                var point2 = points.get(j);
                if (point2.colour != 'R') {
                    continue;
                }

                int side1 = Math.abs(point1.x - point2.x) + 1;
                int side2 = Math.abs(point1.y - point2.y) + 1;

                long area = (long) side1 * side2;

                if (area > maxArea && isRectangleInside(point1, point2, points)) {
                    maxArea = area;
                    System.out.println("New max area: " + maxArea + " between points " + point1 + " and " + point2);
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
                points.add(new Point(previousPoint.x, i, 'G'));
            }
        } else {
            int minX = Math.min(previousPoint.x, currentPoint.x);
            int maxX = Math.max(previousPoint.x, currentPoint.x);

            for (int i = minX + 1; i < maxX; i++) {
                points.add(new Point(i, previousPoint.y, 'G'));
            }
        }
    }

    public static boolean isRectangleInside(Point p1, Point p2, Collection<Point> shapePoints) {

        int x1 = Math.min(p1.x(), p2.x());
        int y1 = Math.min(p1.y(), p2.y());
        int x2 = Math.max(p1.x(), p2.x());
        int y2 = Math.max(p1.y(), p2.y());

        // shrink
        int ix1 = x1 + 1, iy1 = y1 + 1;
        int ix2 = x2 - 1, iy2 = y2 - 1;

        if (ix1 > ix2 || iy1 > iy2)
            return false;

        // check if any shape point lies on the inner perimeter
        for (Point s : shapePoints) {
            int px = s.x();
            int py = s.y();

            // top or bottom edge?
            if ((py == iy1 || py == iy2) && px >= ix1 && px <= ix2) {
                return false;
            }

            // left or right edge?
            if ((px == ix1 || px == ix2) && py >= iy1 && py <= iy2) {
                return false;
            }
        }

        return true;
    }
}

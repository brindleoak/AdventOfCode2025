package org.brindleoak;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Day11Part2 {

    static class Node {
        String name;
        String[] child;
        long solution;

        Node(String node, String[] child, long solution) {
            this.name = node;
            this.child = child;
            this.solution = solution;
        }
    }

    public static void main(String[] args) throws Exception {

        final InputStream is = Day11.class.getResourceAsStream("/data/day-11-input.txt");

        if (is == null) {
            throw new IllegalStateException("Required resource missing: /data/day-11-input.txt");
        }

        List<String> inputRecords = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            inputRecords = br.lines().toList();
        }

        List<Node> nodes = setUpNodes(inputRecords);

        long startTime = System.currentTimeMillis();

        solve(nodes);
        for (Node m : nodes) {
            if (m.name.equals("svr") || m.name.equals("dac") || m.name.equals("rrk")) {
                System.out.println(m.name + " " + Arrays.toString(m.child) + " " + m.solution);
            }
        }

        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("Elapsed time: " + elapsedTime + " ms");
    }

    static List<Node> setUpNodes(List<String> inputRecords) {
        List<Node> nodes = new ArrayList<>();
        for (String record : inputRecords) {
            String[] parts = record.split(" ");

            if (parts[0].equals("dac:")) {
                nodes.add(new Node(parts[0].replace(":", ""),
                        new String[] {}, 3651));
                continue;
            } else {
                nodes.add(new Node(parts[0].replace(":", ""),
                        Arrays.copyOfRange(parts, 1, parts.length), 0));
            }
        }

        // nodes.add(new Node("out", new String[] {}, 1));

        return nodes;
    }

    static void solve(List<Node> nodes) {

        boolean continueProcessing = true;

        while (continueProcessing) {
            continueProcessing = false;
            for (Node node : nodes) {
                if (node.child.length == 0 && !node.name.equals("processed")) {
                    for (Node parent : getParents(node.name, nodes)) {
                        parent.solution += node.solution;

                        List<String> newChildren = new ArrayList<>(Arrays.asList(parent.child));
                        newChildren.remove(node.name);
                        parent.child = newChildren.toArray(String[]::new);

                    }
                    if (node.name.equals("svr")) {
                        continueProcessing = false;
                        break;
                    } else {
                        node.name = "processed";
                        continueProcessing = true;
                    }

                }
            }
        }
        return;
    }

    static List<Node> getParents(String childNode, List<Node> nodes) {
        List<Node> parents = new ArrayList<>();
        for (Node node : nodes) {
            for (String child : node.child) {
                if (child.equals(childNode)) {
                    parents.add(node);
                }
            }
        }
        return parents;
    }

}

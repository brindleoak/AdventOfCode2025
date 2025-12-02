package org.brindleoak;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

record State(int position, int zeroCount) {
}

public class Day1 {
  public static void main(String[] args) throws IOException {

    List<String> rules = Files
        .readAllLines(Path.of("/home/simon/GitHub/AdventOfCode2025/app/src/main/java/org/brindleoak/day-1-input.txt"));

    int position = 50;
    int cycles = 0;

    State initialState = new State(position, cycles);
    State finalState = rules.stream()
        .reduce(initialState, Day1::apply, (s1, s2) -> s2);
    System.out
        .println("Final position: " + finalState.position() + ", number of times at 0: " + finalState.zeroCount());

  }

  public static State apply(State oldState, String rule) {
    int move = Integer.parseInt(rule.substring(1));

    int position = oldState.position();
    int cycles = oldState.zeroCount();
    int startPos;

    cycles += move / 100;
    move = move % 100;

    startPos = position;

    if (rule.substring(0, 1).equals("L"))
      position -= move;
    else
      position += move;

    if (position == 0)
      cycles += 1;
    else if (startPos != 0)
      if (position < 0 || position > 99)
        cycles += 1;

    position = ((position % 100) + 100) % 100;

    System.out
        .println(String.valueOf(startPos) + " -> " + rule + " -> " + position + " (numOfZeros: " + cycles + ")");

    return new State(position, cycles);
  }
}

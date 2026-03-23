package com.snakesladders.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The game board of size n×n.
 *
 * Cells are numbered 1 to n².
 * Internally maintains two lookup maps for O(1) snake/ladder resolution.
 *
 *   snakeMap  : head  → tail   (landing on a head sends you to the tail)
 *   ladderMap : start → end    (landing on a start sends you to the end)
 */
public class Board {

    private final int n;               // board dimension
    private final int totalCells;      // n²

    private final Map<Integer, Integer> snakeMap  = new HashMap<>();
    private final Map<Integer, Integer> ladderMap = new HashMap<>();

    private final List<Snake>  snakes;
    private final List<Ladder> ladders;

    public Board(int n, List<Snake> snakes, List<Ladder> ladders) {
        this.n = n;
        this.totalCells = n * n;
        this.snakes  = Collections.unmodifiableList(snakes);
        this.ladders = Collections.unmodifiableList(ladders);

        snakes.forEach(s  -> snakeMap.put(s.getHead(),  s.getTail()));
        ladders.forEach(l -> ladderMap.put(l.getStart(), l.getEnd()));
    }

    /**
     * Resolves the final cell after landing on {@code cell}.
     * - If the cell has a snake head  → returns the snake's tail.
     * - If the cell has a ladder start → returns the ladder's end.
     * - Otherwise                      → returns the cell unchanged.
     */
    public int resolve(int cell) {
        if (snakeMap.containsKey(cell)) {
            System.out.printf("    🐍  Snake! %d → %d%n", cell, snakeMap.get(cell));
            return snakeMap.get(cell);
        }
        if (ladderMap.containsKey(cell)) {
            System.out.printf("    🪜  Ladder! %d → %d%n", cell, ladderMap.get(cell));
            return ladderMap.get(cell);
        }
        return cell;
    }

    public boolean isWinningCell(int cell) {
        return cell == totalCells;
    }

    public boolean isOutOfBounds(int cell) {
        return cell > totalCells;
    }

    public int getTotalCells() { return totalCells; }
    public int getN()          { return n; }
    public List<Snake>  getSnakes()  { return snakes; }
    public List<Ladder> getLadders() { return ladders; }

    public void printLayout() {
        System.out.println("\n── Board Layout (" + n + "×" + n + " = " + totalCells + " cells) ───────────────");
        System.out.println("  Snakes  (" + snakes.size() + "):");
        snakes.forEach(s  -> System.out.println("    " + s));
        System.out.println("  Ladders (" + ladders.size() + "):");
        ladders.forEach(l -> System.out.println("    " + l));
        System.out.println("──────────────────────────────────────────────────────");
    }
}

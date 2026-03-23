package com.snakesladders.services;

import com.snakesladders.enums.DifficultyLevel;
import com.snakesladders.models.Board;
import com.snakesladders.models.Ladder;
import com.snakesladders.models.Snake;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Randomly generates n snakes and n ladders and builds a Board.
 *
 * Placement rules enforced:
 *   - No snake or ladder starts/ends on cell 1 or cell n².
 *   - No two snakes or ladders share the same trigger cell.
 *   - Snakes and ladders do not share trigger cells with each other.
 *   - No cycles: the destination of a snake/ladder is not itself a trigger.
 *   - Difficulty controls the minimum gap of snakes:
 *       EASY: gap ≤ 20% of total cells
 *       HARD: gap ≥ 40% of total cells
 */
public class BoardFactory {

    private static final int MAX_ATTEMPTS = 10_000;

    private final Random random;

    public BoardFactory() {
        this.random = new Random();
    }

    public BoardFactory(long seed) {
        this.random = new Random(seed);
    }

    public Board create(int n, DifficultyLevel difficulty) {
        int total = n * n;

        // Cells that are already occupied as a trigger or destination
        Set<Integer> occupiedTriggers     = new HashSet<>();
        Set<Integer> occupiedDestinations = new HashSet<>();

        // Cell 1 and last cell cannot be triggers or destinations
        occupiedTriggers.add(1);
        occupiedTriggers.add(total);
        occupiedDestinations.add(1);
        occupiedDestinations.add(total);

        List<Snake>  snakes  = new ArrayList<>();
        List<Ladder> ladders = new ArrayList<>();

        // ── Place n ladders ───────────────────────────────────────────────────
        int placed = 0;
        int attempts = 0;
        while (placed < n && attempts < MAX_ATTEMPTS) {
            attempts++;
            int start = randomCell(2, total - 1);
            int end   = randomCell(start + 1, total - 1);

            if (occupiedTriggers.contains(start) || occupiedDestinations.contains(end)) continue;
            // No cycle: destination of this ladder must not itself be a trigger
            if (occupiedTriggers.contains(end)) continue;

            ladders.add(new Ladder(start, end));
            occupiedTriggers.add(start);
            occupiedDestinations.add(end);
            placed++;
        }

        // ── Place n snakes ────────────────────────────────────────────────────
        int minGap = difficulty == DifficultyLevel.HARD
            ? Math.max(1, (int) (total * 0.40))
            : 1;
        int maxGap = difficulty == DifficultyLevel.EASY
            ? Math.max(1, (int) (total * 0.20))
            : total;

        placed   = 0;
        attempts = 0;
        while (placed < n && attempts < MAX_ATTEMPTS) {
            attempts++;
            int head = randomCell(2, total - 1);
            int gap  = minGap + random.nextInt(Math.max(1, maxGap - minGap + 1));
            int tail = head - gap;

            if (tail < 1) continue;
            if (occupiedTriggers.contains(head) || occupiedDestinations.contains(tail)) continue;
            if (occupiedTriggers.contains(tail)) continue;

            snakes.add(new Snake(head, tail));
            occupiedTriggers.add(head);
            occupiedDestinations.add(tail);
            placed++;
        }

        return new Board(n, snakes, ladders);
    }

    private int randomCell(int min, int max) {
        if (max < min) return min;
        return min + random.nextInt(max - min + 1);
    }
}

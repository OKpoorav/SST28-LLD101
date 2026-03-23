package com.snakesladders;

import com.snakesladders.enums.DifficultyLevel;
import com.snakesladders.enums.GameStatus;
import com.snakesladders.models.Board;
import com.snakesladders.models.Dice;
import com.snakesladders.models.Player;
import com.snakesladders.services.BoardFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │                          Game                                       │
 * │─────────────────────────────────────────────────────────────────────│
 * │  Orchestrates a full Snakes & Ladders session.                      │
 * │                                                                     │
 * │  - Players take turns in round-robin order (Queue).                 │
 * │  - A player is removed from the queue once they win.               │
 * │  - The game ends when only one player remains (no winner yet).      │
 * │  - Roll is ignored if the resulting position exceeds the last cell. │
 * └─────────────────────────────────────────────────────────────────────┘
 */
public class Game {

    private final Board  board;
    private final Dice   dice;
    private final Queue<Player> turnQueue = new LinkedList<>();
    private final List<Player>  allPlayers;
    private final List<Player>  winners   = new ArrayList<>();
    private GameStatus status = GameStatus.IN_PROGRESS;

    public Game(int n, List<String> playerNames, DifficultyLevel difficulty) {
        this(n, playerNames, difficulty, new BoardFactory(), new Dice());
    }

    /** Testable constructor — inject BoardFactory and Dice. */
    public Game(int n, List<String> playerNames, DifficultyLevel difficulty,
                BoardFactory factory, Dice dice) {
        this.board = factory.create(n, difficulty);
        this.dice  = dice;

        allPlayers = new ArrayList<>();
        for (int i = 0; i < playerNames.size(); i++) {
            Player p = new Player("P" + (i + 1), playerNames.get(i));
            allPlayers.add(p);
            turnQueue.add(p);
        }
    }

    // ── Main API ───────────────────────────────────────────────────────────────

    /**
     * Plays the game to completion and prints a running log.
     */
    public void play() {
        board.printLayout();
        System.out.println("\n── Game Start ────────────────────────────────────────");
        printPositions();

        while (!isOver()) {
            playOneTurn();
        }

        System.out.println("\n══════════════════════════════════════════════════════");
        System.out.println("  GAME OVER");
        System.out.println("══════════════════════════════════════════════════════");
        System.out.println("  Winners (in order of finish):");
        for (int i = 0; i < winners.size(); i++) {
            System.out.printf("    %d. %s%n", i + 1, winners.get(i).getName());
        }
        if (!turnQueue.isEmpty()) {
            System.out.println("  Last player still playing (game stopped):");
            turnQueue.forEach(p -> System.out.println("    - " + p.getName() + " at position " + p.getPosition()));
        }
        System.out.println("══════════════════════════════════════════════════════");
    }

    /**
     * Advances exactly one player's turn.
     * Exposed for step-by-step / test use.
     */
    public void playOneTurn() {
        if (isOver()) return;

        Player current = turnQueue.poll();

        int roll = dice.roll();
        int newPos = current.getPosition() + roll;

        System.out.printf("%n  %s rolls %d  (was at %d)%n",
            current.getName(), roll, current.getPosition());

        // Rule: do not move if result exceeds the last cell
        if (board.isOutOfBounds(newPos)) {
            System.out.printf("    → Exceeds board limit (%d). Stay at %d.%n",
                board.getTotalCells(), current.getPosition());
            turnQueue.add(current);
            return;
        }

        // Resolve snakes / ladders
        int finalPos = board.resolve(newPos);
        current.moveTo(finalPos);
        System.out.printf("    → Moves to %d%n", finalPos);

        // Check for win
        if (board.isWinningCell(finalPos)) {
            current.markAsWinner();
            winners.add(current);
            System.out.printf("  *** %s WINS! (rank %d) ***%n", current.getName(), winners.size());
            // Do NOT re-add to queue
        } else {
            turnQueue.add(current);
        }

        // Game ends when only one player remains unfinished
        if (turnQueue.size() <= 1) {
            status = GameStatus.FINISHED;
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    public boolean isOver() {
        return status == GameStatus.FINISHED;
    }

    public List<Player> getWinners()    { return winners; }
    public List<Player> getAllPlayers() { return allPlayers; }
    public Board        getBoard()      { return board; }
    public GameStatus   getStatus()     { return status; }

    private void printPositions() {
        System.out.println("  Starting positions:");
        allPlayers.forEach(p ->
            System.out.printf("    %-12s → %d%n", p.getName(), p.getPosition()));
    }
}

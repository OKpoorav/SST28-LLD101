package com.snakesladders;

import com.snakesladders.enums.DifficultyLevel;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Entry point — reads n, number of players, and difficulty from stdin,
 * then runs the game to completion.
 *
 * Example interaction:
 *   Enter board size (n): 10
 *   Enter number of players: 3
 *   Enter player name 1: Alice
 *   Enter player name 2: Bob
 *   Enter player name 3: Charlie
 *   Enter difficulty (easy/hard): easy
 */
public class Main {

    public static void main(String[] args) {

        // ── Demo mode: skip Scanner if arguments are passed directly ──────────
        if (args.length >= 3) {
            runWithArgs(args);
            return;
        }

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter board size (n): ");
        int n = sc.nextInt();

        System.out.print("Enter number of players: ");
        int numPlayers = sc.nextInt();
        sc.nextLine(); // consume newline

        List<String> playerNames = new java.util.ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter player name " + i + ": ");
            playerNames.add(sc.nextLine().trim());
        }

        System.out.print("Enter difficulty (easy/hard): ");
        String diffInput = sc.nextLine().trim().toUpperCase();
        DifficultyLevel difficulty = DifficultyLevel.valueOf(diffInput);

        sc.close();

        Game game = new Game(n, playerNames, difficulty);
        game.play();
    }

    /** Runs a quick demo without stdin: args = [n, difficulty, player1, player2, ...] */
    private static void runWithArgs(String[] args) {
        int n                   = Integer.parseInt(args[0]);
        DifficultyLevel diff    = DifficultyLevel.valueOf(args[1].toUpperCase());
        List<String> players    = Arrays.asList(args).subList(2, args.length);

        System.out.println("=== Snakes & Ladders (" + n + "×" + n + " | " + diff + ") ===");
        Game game = new Game(n, players, diff);
        game.play();
    }
}

package com.snakesladders.models;

/**
 * Represents a player in the game.
 * Position 0 means the player has not yet entered the board.
 */
public class Player {

    private final String id;
    private final String name;
    private int position;   // 0 = outside board
    private boolean winner; // true once this player reaches the final cell

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.position = 0;
        this.winner = false;
    }

    public void moveTo(int newPosition) {
        this.position = newPosition;
    }

    public void markAsWinner() {
        this.winner = true;
    }

    public String getId()       { return id; }
    public String getName()     { return name; }
    public int getPosition()    { return position; }
    public boolean isWinner()   { return winner; }

    @Override
    public String toString() {
        return String.format("Player[%s | pos=%d | %s]",
            name, position, winner ? "WINNER" : "playing");
    }
}

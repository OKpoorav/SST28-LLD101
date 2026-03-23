package com.snakesladders.models;

/**
 * A snake bites the player at its head and slides them down to its tail.
 *
 * Invariant: head > tail  (always sends the player backwards)
 */
public class Snake {

    private final int head;  // cell where the snake's mouth is
    private final int tail;  // cell where the snake ends (smaller number)

    public Snake(int head, int tail) {
        if (head <= tail) throw new IllegalArgumentException(
            "Snake head (" + head + ") must be greater than tail (" + tail + ")");
        this.head = head;
        this.tail = tail;
    }

    public int getHead() { return head; }
    public int getTail() { return tail; }

    @Override
    public String toString() {
        return String.format("Snake[%d → %d  (-%d)]", head, tail, head - tail);
    }
}

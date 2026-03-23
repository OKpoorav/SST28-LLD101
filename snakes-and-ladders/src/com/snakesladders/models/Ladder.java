package com.snakesladders.models;

/**
 * A ladder boosts the player from its start up to its end.
 *
 * Invariant: end > start  (always sends the player forward)
 */
public class Ladder {

    private final int start;  // cell at the bottom of the ladder
    private final int end;    // cell at the top of the ladder (larger number)

    public Ladder(int start, int end) {
        if (end <= start) throw new IllegalArgumentException(
            "Ladder end (" + end + ") must be greater than start (" + start + ")");
        this.start = start;
        this.end = end;
    }

    public int getStart() { return start; }
    public int getEnd()   { return end; }

    @Override
    public String toString() {
        return String.format("Ladder[%d → %d  (+%d)]", start, end, end - start);
    }
}

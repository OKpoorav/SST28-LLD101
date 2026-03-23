package com.snakesladders.models;

import java.util.Random;

/**
 * A fair six-sided dice numbered 1–6.
 */
public class Dice {

    private final Random random;

    public Dice() {
        this.random = new Random();
    }

    /** For deterministic testing, seed can be injected. */
    public Dice(long seed) {
        this.random = new Random(seed);
    }

    /** Returns a random value in [1, 6]. */
    public int roll() {
        return random.nextInt(6) + 1;
    }
}

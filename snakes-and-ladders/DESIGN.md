# Snakes & Ladders — Design

## Problem Summary

Build a configurable Snakes & Ladders game that takes board size `n` (n×n board),
number of players, and a difficulty level (easy/hard) as input.
Place `n` snakes and `n` ladders randomly, then run the game turn-by-turn until
only one player has not yet finished.

---

## File Structure

```
snakes-and-ladders/
└── src/com/snakesladders/
    ├── enums/
    │   ├── DifficultyLevel.java    EASY | HARD
    │   └── GameStatus.java         IN_PROGRESS | FINISHED
    ├── models/
    │   ├── Snake.java              head > tail  (sends player backward)
    │   ├── Ladder.java             end  > start (sends player forward)
    │   ├── Player.java             position, winner flag
    │   ├── Dice.java               fair 6-sided, injectable seed for tests
    │   └── Board.java              n×n grid, O(1) snake/ladder lookup maps
    ├── services/
    │   └── BoardFactory.java       random placement of n snakes + n ladders
    ├── Game.java                   turn engine — round-robin Queue of players
    └── Main.java                   stdin driver + demo (args) mode
```

---

## Class Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                         Game                                 │
│──────────────────────────────────────────────────────────────│
│ - board       : Board                                        │
│ - dice        : Dice                                         │
│ - turnQueue   : Queue<Player>   (round-robin turns)          │
│ - allPlayers  : List<Player>                                 │
│ - winners     : List<Player>    (ordered by finish time)     │
│ - status      : GameStatus                                   │
│──────────────────────────────────────────────────────────────│
│ + play()           runs game to completion                   │
│ + playOneTurn()    advances one player's turn                │
│ + isOver()         true when ≤1 player left in queue         │
└──────────────┬───────────────────────┬───────────────────────┘
               │ has-a                 │ has-a
     ┌─────────┴──────────┐   ┌────────┴──────────────────┐
     │       Board        │   │          Dice              │
     │────────────────────│   │───────────────────────────│
     │ n, totalCells      │   │ random: Random            │
     │ snakeMap  Map<I,I> │   │───────────────────────────│
     │ ladderMap Map<I,I> │   │ + roll() → int [1..6]     │
     │ snakes  List<Snake>│   └───────────────────────────┘
     │ ladders List<Ladder│
     │────────────────────│
     │ + resolve(cell)    │   ┌────────────────────────────┐
     │ + isWinningCell()  │   │         Player             │
     │ + isOutOfBounds()  │   │───────────────────────────│
     └─────────┬──────────┘   │ id, name                  │
               │ has          │ position  (0 = outside)   │
       ┌───────┴──────┐       │ winner: boolean           │
       │    Snake     │       │───────────────────────────│
       │──────────────│       │ + moveTo(pos)             │
       │ head > tail  │       │ + markAsWinner()          │
       └──────────────┘       └────────────────────────────┘
       ┌──────────────┐
       │    Ladder    │       ┌────────────────────────────┐
       │──────────────│       │      BoardFactory          │
       │ end > start  │       │───────────────────────────│
       └──────────────┘       │ + create(n, difficulty)   │
                              │       → Board              │
                              └────────────────────────────┘
```

---

## Core Algorithms

### Turn Engine (Game.java)

```
while active players > 1:
    current = turnQueue.poll()
    roll    = dice.roll()
    newPos  = current.position + roll

    if newPos > totalCells:
        skip (do not move)          ← Rule: no overshoot
        re-add current to queue
        continue

    finalPos = board.resolve(newPos) ← applies snake or ladder if any
    current.moveTo(finalPos)

    if finalPos == totalCells:
        mark as winner              ← do NOT re-add to queue
    else:
        re-add to queue

game ends when turnQueue.size() <= 1
```

### Board Resolution (Board.java)

```
resolve(cell):
    if snakeMap.contains(cell)  → return snakeMap[cell]   (slide down)
    if ladderMap.contains(cell) → return ladderMap[cell]  (climb up)
    return cell                                            (no effect)
```
O(1) lookup using HashMap.

### Random Placement (BoardFactory.java)

```
Shared constraint set: occupiedTriggers, occupiedDestinations
  (cell 1 and cell n² are pre-blocked)

Place n ladders:
  pick random start ∈ [2, n²-1], end ∈ [start+1, n²-1]
  reject if start or end already occupied
  reject if end is already a trigger (prevents cycles)

Place n snakes:
  EASY: gap = random in [1, 20% of n²]
  HARD: gap = random in [40% of n², n²]
  head = random ∈ [2, n²-1], tail = head - gap
  reject if tail < 1, or head/tail already occupied
  reject if tail is already a trigger (prevents cycles)
```

---

## Game Rules Implemented

| Rule | Where |
|---|---|
| Board cells numbered 1 to n² | `Board.totalCells = n*n` |
| Players start outside the board (position 0) | `Player.position = 0` |
| Round-robin turns | `Queue<Player>` in `Game` |
| Six-sided dice, values 1–6 | `Dice.roll()` → `random.nextInt(6) + 1` |
| Overshoot = no move | `if newPos > totalCells: skip` |
| Snake → slide to tail | `snakeMap` lookup in `Board.resolve()` |
| Ladder → climb to end | `ladderMap` lookup in `Board.resolve()` |
| Win = reach last cell exactly | `board.isWinningCell(pos)` |
| Game ends when 1 player left | `turnQueue.size() <= 1` |
| No cycles | Destination checked against `occupiedTriggers` in `BoardFactory` |
| Snakes do not create cycles | Tail cell cannot itself be a snake head or ladder start |

---

## Difficulty Levels

| Level | Snake gap size | Effect |
|---|---|---|
| EASY | 1 – 20% of n² | Short snakes; players recover quickly |
| HARD | 40% – 100% of n² | Long snakes; one bite sets you back far |

---

## Design Decisions

| Decision | Rationale |
|---|---|
| **Queue for turn order** | Natural FIFO rotation; won players are simply not re-enqueued |
| **HashMap for snake/ladder lookup** | O(1) resolution per cell instead of O(n) linear scan |
| **BoardFactory separate from Board** | Board is a pure data structure; placement logic is isolated |
| **Dice injectable seed** | Enables deterministic unit tests without changing production code |
| **Cycle prevention via shared occupation sets** | A snake tail / ladder end being someone else's trigger would chain resolves; prevented at placement time |
| **Game ends at ≤1 remaining (not 0)** | Requirement: "game continues till at least 2 players still playing"; last un-finished player is not forced to complete |
| **Winners ordered by finish time** | `winners` list preserves insertion order → rank 1 = first to reach cell n² |

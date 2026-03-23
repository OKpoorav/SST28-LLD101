package com.parkingsystem.models;

/**
 * Represents the `entry_gates` entity.
 * A physical gate through which vehicles enter the lot.
 *
 * Schema:
 *   id           string PK
 *   name         string
 *   floor_number int
 *   position     int    (lateral index, used for nearest-slot calculation)
 */
public class EntryGate {

    private final String id;
    private final String name;
    private final int floorNumber;
    private final int position;

    public EntryGate(String id, String name, int floorNumber, int position) {
        this.id = id;
        this.name = name;
        this.floorNumber = floorNumber;
        this.position = position;
    }

    public String getId()        { return id; }
    public String getName()      { return name; }
    public int getFloorNumber()  { return floorNumber; }
    public int getPosition()     { return position; }

    @Override
    public String toString() {
        return String.format("EntryGate[%s | %s | Floor %d | pos=%d]",
                id, name, floorNumber, position);
    }
}

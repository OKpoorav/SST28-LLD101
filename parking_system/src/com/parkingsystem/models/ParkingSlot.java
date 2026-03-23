package com.parkingsystem.models;

import com.parkingsystem.enums.SlotType;

/**
 * Represents the `parking_slots` entity.
 * A single physical spot in the lot identified by floor + slot_number.
 *
 * Schema:
 *   id           string  PK
 *   slot_type_id string  FK -> slot_types.id
 *   floor_number int
 *   slot_number  string  (e.g. "A1", "B3")
 *   is_occupied  boolean
 *
 * Position model:
 *   Each slot has a lateral `position` index within its floor.
 *   Gates sit at position 0..N on floor 0 (ground).
 *   Distance = floorNumber * LEVEL_PENALTY + |gate.position - slot.position|
 */
public class ParkingSlot {

    private static final int LEVEL_PENALTY = 50;

    private final String id;
    private final String slotTypeId;
    private final SlotType slotType;       // denormalised for convenience
    private final int floorNumber;
    private final String slotNumber;
    private final int position;            // lateral index within the floor
    private boolean isOccupied;

    public ParkingSlot(String id, String slotTypeId, SlotType slotType,
                       int floorNumber, String slotNumber, int position) {
        this.id = id;
        this.slotTypeId = slotTypeId;
        this.slotType = slotType;
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
        this.position = position;
        this.isOccupied = false;
    }

    /** Manhattan-style distance from an entry gate. */
    public int distanceFrom(int gatePosition) {
        return floorNumber * LEVEL_PENALTY + Math.abs(gatePosition - position);
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String getId()          { return id; }
    public String getSlotTypeId()  { return slotTypeId; }
    public SlotType getSlotType()  { return slotType; }
    public int getFloorNumber()    { return floorNumber; }
    public String getSlotNumber()  { return slotNumber; }
    public int getPosition()       { return position; }
    public boolean isOccupied()    { return isOccupied; }

    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    @Override
    public String toString() {
        return String.format("ParkingSlot[%s | %s | Floor %d | %s]",
                id, slotType, floorNumber, isOccupied ? "OCCUPIED" : "FREE");
    }
}

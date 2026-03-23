package com.parkingsystem.models;

import com.parkingsystem.enums.SlotType;

/**
 * Represents the `slot_types` entity.
 * Stores the slot category and its hourly rate.
 *
 * Schema:
 *   id          string  PK
 *   name        string  (SMALL | MEDIUM | LARGE)
 *   hourly_rate decimal
 */
public class SlotTypeInfo {

    private final String id;
    private final SlotType name;
    private final double hourlyRate;

    public SlotTypeInfo(String id, SlotType name, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.hourlyRate = hourlyRate;
    }

    public String getId()          { return id; }
    public SlotType getName()      { return name; }
    public double getHourlyRate()  { return hourlyRate; }

    @Override
    public String toString() {
        return String.format("SlotTypeInfo[%s | %s | Rs.%.2f/hr]", id, name, hourlyRate);
    }
}

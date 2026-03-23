package com.parkingsystem;

import com.parkingsystem.enums.SlotType;
import com.parkingsystem.models.EntryGate;
import com.parkingsystem.models.ParkingSlot;
import com.parkingsystem.services.PricingService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Convenience builder that constructs the ParkingLot from a high-level config.
 *
 * Usage:
 *   ParkingLot lot = new ParkingLotBuilder("City Parking")
 *       .floors(3)
 *       .slotsPerFloor(Map.of(SlotType.SMALL, 5, SlotType.MEDIUM, 5, SlotType.LARGE, 3))
 *       .addGate("G1", "North Gate", 0, 0)
 *       .addGate("G2", "South Gate", 0, 10)
 *       .build();
 */
public class ParkingLotBuilder {

    private final String name;
    private int floors = 1;
    private Map<SlotType, Integer> slotsPerFloor = Map.of(
        SlotType.SMALL, 5, SlotType.MEDIUM, 5, SlotType.LARGE, 3);
    private final List<EntryGate> gates = new ArrayList<>();
    private PricingService pricingService = new PricingService();

    public ParkingLotBuilder(String name) { this.name = name; }

    public ParkingLotBuilder floors(int n)             { this.floors = n; return this; }
    public ParkingLotBuilder slotsPerFloor(Map<SlotType, Integer> config) {
        this.slotsPerFloor = config; return this;
    }
    public ParkingLotBuilder pricingService(PricingService ps) {
        this.pricingService = ps; return this;
    }
    public ParkingLotBuilder addGate(String id, String name, int floor, int position) {
        gates.add(new EntryGate(id, name, floor, position));
        return this;
    }

    public ParkingLot build() {
        ParkingLot.reset();   // allow fresh instance per build call

        List<ParkingSlot> slots = new ArrayList<>();
        // SlotType layout: within each floor, slot types are placed at offset positions
        // SMALL at positions 0–(n-1), MEDIUM at n–(2n-1), LARGE at 2n–(2n+m-1)
        for (int floor = 0; floor < floors; floor++) {
            int posOffset = 0;
            for (SlotType type : SlotType.values()) {
                int count = slotsPerFloor.getOrDefault(type, 0);
                for (int i = 0; i < count; i++) {
                    String slotId     = String.format("F%d-%s-%02d", floor, type.name().charAt(0), i + 1);
                    String slotNumber = String.format("%s%02d", type.name().charAt(0), i + 1);
                    String slotTypeId = "ST-" + (type.ordinal() + 1);
                    slots.add(new ParkingSlot(slotId, slotTypeId, type, floor, slotNumber, posOffset + i));
                }
                posOffset += count;
            }
        }

        return ParkingLot.getInstance(name, slots, gates, pricingService);
    }
}

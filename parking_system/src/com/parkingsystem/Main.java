package com.parkingsystem;

import com.parkingsystem.enums.SlotType;
import com.parkingsystem.enums.VehicleType;
import com.parkingsystem.models.Bill;
import com.parkingsystem.models.ParkingTicket;
import com.parkingsystem.models.Vehicle;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Demonstrates the three public APIs:
 *   1. park(vehicle, entryTime, requestedSlotType, entryGateId)
 *   2. status()
 *   3. exit(ticket, exitTime)
 */
public class Main {

    public static void main(String[] args) {

        // ── 1. Build the parking lot ───────────────────────────────────────────
        ParkingLot lot = new ParkingLotBuilder("City Centre Parking")
            .floors(3)
            .slotsPerFloor(Map.of(
                SlotType.SMALL,  5,
                SlotType.MEDIUM, 5,
                SlotType.LARGE,  3))
            .addGate("G1", "North Gate",  0,  0)
            .addGate("G2", "East Gate",   0, 10)
            .addGate("G3", "South Gate",  0,  5)
            .build();

        System.out.println("=== " + lot.getName() + " initialized ===");
        lot.printStatus();

        // ── 2. Park vehicles ───────────────────────────────────────────────────
        LocalDateTime t0 = LocalDateTime.of(2026, 3, 23, 9, 0, 0);

        // Bike enters via G1 – no preferred slot type → nearest SMALL assigned
        Vehicle bike = new Vehicle("V001", "KA01AB1234", VehicleType.TWO_WHEELER);
        ParkingTicket t1 = lot.park(bike, t0, null, "G1");
        System.out.println("\n[ENTRY] " + t1);

        // Car enters via G2 – no preferred slot type → nearest MEDIUM assigned
        Vehicle car = new Vehicle("V002", "KA02CD5678", VehicleType.CAR);
        ParkingTicket t2 = lot.park(car, t0.plusMinutes(5), null, "G2");
        System.out.println("\n[ENTRY] " + t2);

        // Bus enters via G3 – only LARGE is compatible
        Vehicle bus = new Vehicle("V003", "KA03EF9012", VehicleType.BUS);
        ParkingTicket t3 = lot.park(bus, t0.plusMinutes(10), null, "G3");
        System.out.println("\n[ENTRY] " + t3);

        // Bike parked in MEDIUM slot explicitly (larger slot, billed at MEDIUM rate)
        Vehicle bike2 = new Vehicle("V004", "MH04GH3456", VehicleType.TWO_WHEELER);
        ParkingTicket t4 = lot.park(bike2, t0.plusMinutes(15), SlotType.MEDIUM, "G1");
        System.out.println("\n[ENTRY – bike in MEDIUM slot] " + t4);

        // ── 3. Status after parkings ───────────────────────────────────────────
        lot.printStatus();

        // ── 4. Exit vehicles ───────────────────────────────────────────────────
        // Bike1: stayed 2h 30m in SMALL slot  → billed 3 hrs × Rs.20  = Rs.60
        LocalDateTime exitBike1 = t0.plusHours(2).plusMinutes(30);
        Bill b1 = lot.exit(t1, exitBike1);
        System.out.println("\n[EXIT] " + b1);

        // Car: stayed 1h 00m in MEDIUM slot   → billed 1 hr  × Rs.40  = Rs.40
        LocalDateTime exitCar = t0.plusHours(1);
        Bill b2 = lot.exit(t2, exitCar);
        System.out.println("\n[EXIT] " + b2);

        // Bus: stayed 3h 10m in LARGE slot    → billed 4 hrs × Rs.80  = Rs.320
        LocalDateTime exitBus = t0.plusHours(3).plusMinutes(10);
        Bill b3 = lot.exit(t3, exitBus);
        System.out.println("\n[EXIT] " + b3);

        // Bike2 in MEDIUM slot: 45 min        → billed 1 hr  × Rs.40  = Rs.40
        LocalDateTime exitBike2 = t0.plusMinutes(60);
        Bill b4 = lot.exit(t4, exitBike2);
        System.out.println("\n[EXIT – bike billed at MEDIUM rate] " + b4);

        // ── 5. Final status ────────────────────────────────────────────────────
        lot.printStatus();

        // ── 6. Edge case: no slot available ───────────────────────────────────
        System.out.println("\n── Edge-case: bus trying to park in SMALL slot ──");
        try {
            Vehicle badBus = new Vehicle("V005", "KA05ZZ0000", VehicleType.BUS);
            lot.park(badBus, t0.plusHours(5), SlotType.SMALL, "G1");
        } catch (IllegalArgumentException e) {
            System.out.println("  Caught expected error: " + e.getMessage());
        }
    }
}

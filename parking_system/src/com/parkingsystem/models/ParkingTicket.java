package com.parkingsystem.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the `parking_tickets` entity.
 * Created when a vehicle enters; closed (is_active=false) on exit.
 *
 * Schema:
 *   id             string    PK
 *   vehicle_id     string    FK -> vehicles.id
 *   slot_id        string    FK -> parking_slots.id
 *   entry_gate_id  string    FK -> entry_gates.id
 *   entry_time     timestamp
 *   exit_time      timestamp (null until vehicle exits)
 *   is_active      boolean
 */
public class ParkingTicket {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String id;
    private final String vehicleId;
    private final String slotId;
    private final String entryGateId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private boolean isActive;

    // Denormalised references for easy in-memory use
    private final Vehicle vehicle;
    private final ParkingSlot slot;

    public ParkingTicket(String id, Vehicle vehicle, ParkingSlot slot,
                         String entryGateId, LocalDateTime entryTime) {
        this.id = id;
        this.vehicle = vehicle;
        this.vehicleId = vehicle.getId();
        this.slot = slot;
        this.slotId = slot.getId();
        this.entryGateId = entryGateId;
        this.entryTime = entryTime;
        this.isActive = true;
    }

    public void close(LocalDateTime exitTime) {
        this.exitTime = exitTime;
        this.isActive = false;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String getId()              { return id; }
    public String getVehicleId()       { return vehicleId; }
    public String getSlotId()          { return slotId; }
    public String getEntryGateId()     { return entryGateId; }
    public LocalDateTime getEntryTime(){ return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public boolean isActive()          { return isActive; }
    public Vehicle getVehicle()        { return vehicle; }
    public ParkingSlot getSlot()       { return slot; }

    @Override
    public String toString() {
        return String.join("\n",
            "========================================",
            "           PARKING TICKET               ",
            "========================================",
            "  Ticket ID   : " + id,
            "  Vehicle     : " + vehicle.getLicensePlate() + " (" + vehicle.getVehicleType() + ")",
            "  Slot        : " + slot.getId() + " [" + slot.getSlotType() + "]",
            "  Floor       : " + slot.getFloorNumber(),
            "  Entry Gate  : " + entryGateId,
            "  Entry Time  : " + entryTime.format(FMT),
            "========================================"
        );
    }
}

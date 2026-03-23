package com.parkingsystem.models;

import com.parkingsystem.enums.SlotType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the `bills` entity.
 * Generated when a vehicle exits; charged by slot type (not vehicle type).
 *
 * Schema:
 *   id             string    PK
 *   ticket_id      string    FK -> parking_tickets.id  (1-to-1)
 *   duration_hours decimal
 *   slot_type_id   string    FK -> slot_types.id
 *   total_amount   decimal
 *   created_at     timestamp
 */
public class Bill {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String id;
    private final String ticketId;
    private final double durationHours;
    private final String slotTypeId;
    private final SlotType slotType;       // denormalised
    private final double totalAmount;
    private final LocalDateTime createdAt;

    // Extra context for display
    private final String vehicleLicensePlate;
    private final String slotId;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;

    public Bill(String id, String ticketId, double durationHours,
                String slotTypeId, SlotType slotType, double totalAmount,
                LocalDateTime createdAt, String vehicleLicensePlate,
                String slotId, LocalDateTime entryTime, LocalDateTime exitTime) {
        this.id = id;
        this.ticketId = ticketId;
        this.durationHours = durationHours;
        this.slotTypeId = slotTypeId;
        this.slotType = slotType;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.vehicleLicensePlate = vehicleLicensePlate;
        this.slotId = slotId;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
    }

    // ── Getters ────────────────────────────────────────────────────────────────

    public String getId()                  { return id; }
    public String getTicketId()            { return ticketId; }
    public double getDurationHours()       { return durationHours; }
    public String getSlotTypeId()          { return slotTypeId; }
    public SlotType getSlotType()          { return slotType; }
    public double getTotalAmount()         { return totalAmount; }
    public LocalDateTime getCreatedAt()    { return createdAt; }

    @Override
    public String toString() {
        return String.join("\n",
            "========================================",
            "             PARKING BILL               ",
            "========================================",
            "  Bill ID       : " + id,
            "  Ticket ID     : " + ticketId,
            "  Vehicle       : " + vehicleLicensePlate,
            "  Slot          : " + slotId + " [" + slotType + "]",
            "  Entry         : " + entryTime.format(FMT),
            "  Exit          : " + exitTime.format(FMT),
            String.format("  Duration      : %.2f hrs (billed as %d hr(s))", durationHours, (int) Math.ceil(durationHours)),
            String.format("  Total Amount  : Rs. %.2f", totalAmount),
            "  Generated At  : " + createdAt.format(FMT),
            "========================================"
        );
    }
}

package com.parkingsystem;

import com.parkingsystem.enums.SlotType;
import com.parkingsystem.enums.VehicleType;
import com.parkingsystem.models.*;
import com.parkingsystem.services.PricingService;
import com.parkingsystem.services.SlotAssignmentService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * ┌─────────────────────────────────────────────────────────────────────┐
 * │                       ParkingLot  (Singleton)                       │
 * │                                                                     │
 * │  Public API                                                         │
 * │  ──────────────────────────────────────────────────────────────     │
 * │  park(vehicle, entryTime, requestedSlotType, entryGateId)           │
 * │      → ParkingTicket                                                │
 * │                                                                     │
 * │  status()                                                           │
 * │      → Map<SlotType, SlotStatus>  (total / available per type)      │
 * │                                                                     │
 * │  exit(ticket, exitTime)                                             │
 * │      → Bill                                                         │
 * └─────────────────────────────────────────────────────────────────────┘
 *
 * Class Diagram (simplified):
 *
 *   ParkingLot  ──uses──►  SlotAssignmentService
 *               ──uses──►  PricingService
 *               ──has──►   List<ParkingSlot>
 *               ──has──►   Map<String, EntryGate>
 *               ──has──►   Map<String, ParkingTicket>  (active tickets)
 *
 *   ParkingTicket  ──has──►  Vehicle
 *                  ──has──►  ParkingSlot
 *
 *   Bill  ──references──►  ParkingTicket (via ticketId)
 *         ──references──►  SlotTypeInfo  (via slotTypeId)
 */
public class ParkingLot {

    // ── Singleton ──────────────────────────────────────────────────────────────

    private static ParkingLot instance;

    public static synchronized ParkingLot getInstance(
            String name,
            List<ParkingSlot> slots,
            List<EntryGate> gates,
            PricingService pricingService) {
        if (instance == null) {
            instance = new ParkingLot(name, slots, gates, pricingService);
        }
        return instance;
    }

    /** Reset singleton – useful for testing. */
    public static synchronized void reset() { instance = null; }

    // ── State ──────────────────────────────────────────────────────────────────

    private final String name;
    private final List<ParkingSlot> slots;
    private final Map<String, EntryGate> gates;
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();

    private final PricingService pricingService;
    private final SlotAssignmentService assignmentService = new SlotAssignmentService();

    private int ticketCounter = 1;
    private int billCounter   = 1;

    private ParkingLot(String name, List<ParkingSlot> slots,
                       List<EntryGate> gates, PricingService pricingService) {
        this.name = name;
        this.slots = new ArrayList<>(slots);
        this.gates = new HashMap<>();
        gates.forEach(g -> this.gates.put(g.getId(), g));
        this.pricingService = pricingService;
    }

    // ── API ────────────────────────────────────────────────────────────────────

    /**
     * Parks a vehicle and returns a ticket.
     *
     * @param vehicle           vehicle details
     * @param entryTime         time of entry
     * @param requestedSlotType preferred slot type (null = any compatible)
     * @param entryGateId       gate ID through which the vehicle is entering
     * @return ParkingTicket
     */
    public ParkingTicket park(Vehicle vehicle, LocalDateTime entryTime,
                              SlotType requestedSlotType, String entryGateId) {
        EntryGate gate = gates.get(entryGateId);
        if (gate == null) {
            throw new IllegalArgumentException("Unknown entry gate: " + entryGateId);
        }

        ParkingSlot slot = assignmentService
            .findNearestSlot(vehicle.getVehicleType(), gate, slots, requestedSlotType)
            .orElseThrow(() -> new IllegalStateException(
                "No available slot for vehicle type: " + vehicle.getVehicleType()));

        slot.setOccupied(true);

        String ticketId = String.format("TKT-%04d", ticketCounter++);
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, slot, entryGateId, entryTime);
        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    /**
     * Returns availability counts per slot type.
     *
     * @return map of SlotType → SlotStatus(total, available)
     */
    public Map<SlotType, SlotStatus> status() {
        Map<SlotType, SlotStatus> result = new LinkedHashMap<>();
        for (SlotType type : SlotType.values()) {
            result.put(type, new SlotStatus(0, 0));
        }
        for (ParkingSlot s : slots) {
            SlotStatus st = result.get(s.getSlotType());
            st.total++;
            if (!s.isOccupied()) st.available++;
        }
        return result;
    }

    /**
     * Processes vehicle exit, frees the slot, and returns the bill.
     *
     * @param ticket   ticket issued at entry
     * @param exitTime time of exit
     * @return Bill with total amount due
     */
    public Bill exit(ParkingTicket ticket, LocalDateTime exitTime) {
        ParkingTicket stored = activeTickets.get(ticket.getId());
        if (stored == null || !stored.isActive()) {
            throw new IllegalArgumentException("Ticket not found or already closed: " + ticket.getId());
        }

        // Duration in fractional hours
        double durationHours = stored.getEntryTime().until(exitTime, ChronoUnit.SECONDS) / 3600.0;
        SlotType slotType    = stored.getSlot().getSlotType();
        double amount        = pricingService.calculateCharge(slotType, durationHours);
        SlotTypeInfo typeInfo = pricingService.getSlotTypeInfo(slotType);

        // Free the slot
        stored.getSlot().setOccupied(false);
        stored.close(exitTime);
        activeTickets.remove(ticket.getId());

        String billId = String.format("BILL-%04d", billCounter++);
        return new Bill(
            billId,
            stored.getId(),
            durationHours,
            typeInfo.getId(),
            slotType,
            amount,
            exitTime,
            stored.getVehicle().getLicensePlate(),
            stored.getSlot().getId(),
            stored.getEntryTime(),
            exitTime
        );
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    public String getName() { return name; }

    public void printStatus() {
        System.out.println("\n── Parking Status: " + name + " ─────────────────────");
        status().forEach((type, st) ->
            System.out.printf("  %-8s  available: %d / %d%n", type, st.available, st.total));
        System.out.println("─────────────────────────────────────────────────────");
    }

    // ── Inner class ────────────────────────────────────────────────────────────

    /** Availability snapshot for a single slot type. */
    public static class SlotStatus {
        public int total;
        public int available;

        public SlotStatus(int total, int available) {
            this.total = total;
            this.available = available;
        }

        @Override
        public String toString() {
            return available + "/" + total;
        }
    }
}

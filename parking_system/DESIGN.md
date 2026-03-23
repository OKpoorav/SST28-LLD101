# Multilevel Parking Lot System — Design

## Problem Summary

Design a multilevel parking lot that supports three vehicle types (2-wheeler, car, bus),
assigns the nearest compatible slot from the entry gate, and bills based on the allocated
slot type (not the vehicle type).

---

## Entity Schema

```
slot_types         parking_slots          entry_gates
──────────────     ──────────────────     ──────────────
id         PK      id            PK       id          PK
name               slot_type_id  FK       name
hourly_rate        floor_number           floor_number
                   slot_number            position
                   is_occupied

vehicles           parking_tickets        bills
──────────────     ──────────────────     ──────────────────
id         PK      id            PK       id           PK
license_plate      vehicle_id    FK       ticket_id    FK (1-to-1)
vehicle_type       slot_id       FK       duration_hours
                   entry_gate_id FK       slot_type_id FK
                   entry_time             total_amount
                   exit_time              created_at
                   is_active

Relationships:
  parking_slots.slot_type_id  → slot_types.id
  parking_tickets.vehicle_id  → vehicles.id
  parking_tickets.slot_id     → parking_slots.id
  parking_tickets.entry_gate_id → entry_gates.id
  bills.ticket_id             — parking_tickets.id
  bills.slot_type_id          → slot_types.id
```

---

## File Structure

```
parking_system/
└── src/com/parkingsystem/
    ├── enums/
    │   ├── VehicleType.java            TWO_WHEELER | CAR | BUS
    │   └── SlotType.java               SMALL | MEDIUM | LARGE
    ├── models/
    │   ├── SlotTypeInfo.java           maps to slot_types
    │   ├── ParkingSlot.java            maps to parking_slots
    │   ├── EntryGate.java              maps to entry_gates
    │   ├── Vehicle.java                maps to vehicles
    │   ├── ParkingTicket.java          maps to parking_tickets
    │   └── Bill.java                   maps to bills
    ├── services/
    │   ├── PricingService.java         hourly rate management + charge calculation
    │   └── SlotAssignmentService.java  nearest-slot selection logic
    ├── ParkingLot.java                 Singleton — exposes park / status / exit
    ├── ParkingLotBuilder.java          fluent builder for lot construction
    └── Main.java                       demo driver
```

---

## Class Diagram

```
                   ┌──────────────────────────────────┐
                   │       ParkingLot  (Singleton)     │
                   │──────────────────────────────────│
                   │ - name: String                    │
                   │ - slots: List<ParkingSlot>        │
                   │ - gates: Map<String, EntryGate>   │
                   │ - activeTickets: Map<String,      │
                   │                  ParkingTicket>   │
                   │──────────────────────────────────│
                   │ + park(vehicle, entryTime,        │
                   │        slotType, gateId)          │
                   │        → ParkingTicket            │
                   │ + status()                        │
                   │        → Map<SlotType, SlotStatus>│
                   │ + exit(ticket, exitTime)          │
                   │        → Bill                     │
                   └────────┬──────────────┬───────────┘
                            │ uses         │ uses
           ┌────────────────┘              └──────────────────┐
┌──────────────────────────┐         ┌───────────────────────┐
│  SlotAssignmentService   │         │    PricingService      │
│──────────────────────────│         │───────────────────────│
│ COMPATIBLE_SLOTS:        │         │ rateCard:             │
│  TWO_WHEELER→S,M,L       │         │  Map<SlotType,        │
│  CAR        →M,L         │         │     SlotTypeInfo>     │
│  BUS        →L           │         │───────────────────────│
│──────────────────────────│         │ + calculateCharge(    │
│ + findNearestSlot(       │         │     slotType, hours)  │
│     vehicleType,         │         │     → double          │
│     gate,                │         │ + getSlotTypeInfo(    │
│     allSlots,            │         │     slotType)         │
│     requestedSlotType)   │         │     → SlotTypeInfo    │
│     → Optional<Slot>     │         └───────────────────────┘
└──────────────────────────┘

┌─────────────────┐      ┌─────────────────────────┐
│    Vehicle      │      │       ParkingSlot        │
│─────────────────│      │─────────────────────────│
│ id              │      │ id                      │
│ licensePlate    │      │ slotTypeId  (FK)        │
│ vehicleType     │      │ slotType                │
└─────────────────┘      │ floorNumber             │
        ▲ has            │ slotNumber              │
        │                │ position                │
┌───────┴─────────────┐  │ isOccupied              │
│   ParkingTicket     │  │─────────────────────────│
│─────────────────────│  │ + distanceFrom(gatePos) │
│ id                  │  │     → int               │
│ vehicleId    (FK) ──┘  └─────────────────────────┘
│ slotId       (FK) ─────────────────▲
│ entryGateId  (FK)          has     │
│ entryTime           ───────────────┘
│ exitTime
│ isActive
└─────────────────────┐
        ▲ references  │
┌───────┴──────────┐  │
│      Bill        │  │
│──────────────────│  │
│ id               │  │
│ ticketId   (FK) ─┘  │
│ durationHours       │
│ slotTypeId   (FK)   │
│ totalAmount         │
│ createdAt           │
└─────────────────────┘
```

---

## API

### `park(vehicle, entryTime, requestedSlotType, entryGateId) → ParkingTicket`

1. Validate the entry gate exists.
2. Ask `SlotAssignmentService` for the nearest free compatible slot.
3. Mark slot as occupied.
4. Create and store a `ParkingTicket` (is_active = true).
5. Return the ticket.

### `status() → Map<SlotType, SlotStatus>`

Iterates all slots and counts total vs. available per slot type.

### `exit(ticket, exitTime) → Bill`

1. Look up the active ticket.
2. Compute duration = exitTime − entryTime (decimal hours).
3. Ask `PricingService` to calculate charge using the **slot type** (not vehicle type).
4. Free the slot (is_occupied = false).
5. Close the ticket (is_active = false, set exit_time).
6. Create and return a `Bill`.

---

## Slot Assignment Logic

```
Compatible types (in preference order):
  TWO_WHEELER  →  SMALL, MEDIUM, LARGE
  CAR          →  MEDIUM, LARGE
  BUS          →  LARGE

Selection:
  1. Filter: slot must be free AND slot type in compatible list
             (or exactly requestedSlotType if specified).
  2. Sort by distance (ascending):
       distance = floorNumber × 50 + |gate.position − slot.position|
  3. Tie-break: prefer smaller slot type to preserve large slots for buses.
  4. Return the first result.
```

---

## Billing Rules

| Rule | Detail |
|---|---|
| Charge based on slot type | A bike in a MEDIUM slot is billed at the MEDIUM rate |
| Duration rounding | Partial hours rounded **up** to the nearest full hour |
| Default rates | SMALL = Rs. 20/hr · MEDIUM = Rs. 40/hr · LARGE = Rs. 80/hr |

---

## Design Decisions

| Decision | Rationale |
|---|---|
| **Singleton ParkingLot** | One lot, one source of truth for slot state |
| **SlotAssignmentService** | Isolates nearest-slot strategy; easy to swap (e.g. prefer same floor) |
| **PricingService** | Decoupled from lot; supports runtime rate changes per slot type |
| **ParkingLotBuilder** | Separates lot construction from business logic |
| **Distance model** | Level penalty (×50) ensures ground floor preferred; lateral position models gate proximity within a floor |
| **Billing on slot type** | Core requirement — vehicle type is irrelevant to the charge |
| **ceil() for duration** | Standard parking industry practice; 2h 30m → 3 billable hours |
| **Tie-break: prefer smaller slot** | Avoids assigning LARGE slots to bikes when a SMALL is equidistant |

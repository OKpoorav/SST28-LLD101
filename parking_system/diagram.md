                        ┌──────────────────────────────┐
                        │       ParkingLot (Singleton)  │
                        │──────────────────────────────│
                        │ - slots: List<ParkingSlot>    │
                        │ - gates: Map<id,EntryGate>    │
                        │ - activeTickets: Map<id,Ticket│
                        │──────────────────────────────│
                        │ + park()  → ParkingTicket     │
                        │ + status()→ Map<SlotType,...> │
                        │ + exit()  → Bill              │
                        └──────┬───────────┬────────────┘
                               │uses       │uses
              ┌────────────────┘           └────────────────┐
 ┌────────────────────────┐       ┌──────────────────────────┐
 │  SlotAssignmentService │       │     PricingService        │
 │────────────────────────│       │──────────────────────────│
 │ COMPATIBLE_SLOTS map   │       │ rateCard: Map<SlotType,  │
 │────────────────────────│       │           SlotTypeInfo>   │
 │ + findNearestSlot()    │       │──────────────────────────│
 └────────────────────────┘       │ + calculateCharge()       │
                                  │ + getSlotTypeInfo()       │
                                  └──────────────────────────┘

  ┌──────────────┐        ┌──────────────────┐
  │   Vehicle    │        │   ParkingSlot     │
  │──────────────│        │──────────────────│
  │ id           │        │ id               │
  │ licensePlate │        │ slotTypeId  (FK) │
  │ vehicleType  │        │ slotType         │
  └──────────────┘        │ floorNumber      │
         ▲                │ slotNumber       │
         │has             │ position         │
  ┌──────┴─────────────┐  │ isOccupied       │
  │   ParkingTicket    │  │──────────────────│
  │────────────────────│  │ +distanceFrom()  │
  │ id                 │  └──────────────────┘
  │ vehicleId     (FK) │◄──────────▲
  │ slotId        (FK) │           │has
  │ entryGateId   (FK) │  ┌────────┴───────────┐
  │ entryTime          │  │       Bill          │
  │ exitTime           │  │───────────────────  │
  │ isActive           │  │ id                  │
  └────────────────────┘  │ ticketId       (FK) │
                          │ durationHours       │
                          │ slotTypeId     (FK) │
                          │ totalAmount         │
                          │ createdAt           │
                          └─────────────────────┘

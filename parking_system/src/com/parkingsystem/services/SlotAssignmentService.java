package com.parkingsystem.services;

import com.parkingsystem.enums.SlotType;
import com.parkingsystem.enums.VehicleType;
import com.parkingsystem.models.EntryGate;
import com.parkingsystem.models.ParkingSlot;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Determines which slot to assign to an incoming vehicle.
 *
 * Compatibility rules:
 *   TWO_WHEELER → SMALL, MEDIUM, LARGE  (prefers SMALL first, then by distance)
 *   CAR         → MEDIUM, LARGE         (prefers MEDIUM first, then by distance)
 *   BUS         → LARGE only
 *
 * A smaller vehicle MAY park in a larger slot only when no compatible
 * same-or-smaller slot is available (i.e. compatible types are tried in
 * ascending size order and the nearest free slot wins overall).
 *
 * Nearest slot = lowest value of:
 *     distance = floorNumber * LEVEL_PENALTY + |gate.position - slot.position|
 */
public class SlotAssignmentService {

    // Compatible slot types in preference order (smallest first)
    private static final Map<VehicleType, List<SlotType>> COMPATIBLE_SLOTS = Map.of(
        VehicleType.TWO_WHEELER, Arrays.asList(SlotType.SMALL, SlotType.MEDIUM, SlotType.LARGE),
        VehicleType.CAR,         Arrays.asList(SlotType.MEDIUM, SlotType.LARGE),
        VehicleType.BUS,         List.of(SlotType.LARGE)
    );

    /**
     * Finds the nearest available compatible slot.
     *
     * @param vehicleType       type of the incoming vehicle
     * @param gate              gate the vehicle is entering through
     * @param allSlots          full list of slots in the parking lot
     * @param requestedSlotType if non-null, restrict assignment to this type
     * @return Optional containing the best slot, or empty if none found
     */
    public Optional<ParkingSlot> findNearestSlot(
            VehicleType vehicleType,
            EntryGate gate,
            List<ParkingSlot> allSlots,
            SlotType requestedSlotType) {

        List<SlotType> compatibleTypes = COMPATIBLE_SLOTS.get(vehicleType);

        if (requestedSlotType != null) {
            if (!compatibleTypes.contains(requestedSlotType)) {
                throw new IllegalArgumentException(
                    vehicleType + " cannot be parked in a " + requestedSlotType + " slot.");
            }
            compatibleTypes = List.of(requestedSlotType);
        }

        final List<SlotType> allowed = compatibleTypes;

        return allSlots.stream()
            .filter(s -> !s.isOccupied() && allowed.contains(s.getSlotType()))
            .min(Comparator
                // Primary: distance from gate
                .<ParkingSlot, Integer>comparing(s -> s.distanceFrom(gate.getPosition()))
                // Tie-break: prefer smaller slot type (avoids wasting large slots)
                .thenComparingInt(s -> slotTypeOrdinal(s.getSlotType())));
    }

    private int slotTypeOrdinal(SlotType type) {
        return switch (type) {
            case SMALL  -> 0;
            case MEDIUM -> 1;
            case LARGE  -> 2;
        };
    }
}

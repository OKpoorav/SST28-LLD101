package com.parkingsystem.services;

import com.parkingsystem.enums.SlotType;
import com.parkingsystem.models.SlotTypeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages hourly rates per slot type and calculates parking charges.
 *
 * Billing rules:
 *  - Charge is based on the ALLOCATED slot type, NOT the vehicle type.
 *  - Duration is rounded UP to the nearest full hour.
 */
public class PricingService {

    private final Map<SlotType, SlotTypeInfo> rateCard = new HashMap<>();

    public PricingService() {
        // Default rates
        rateCard.put(SlotType.SMALL,  new SlotTypeInfo("ST-1", SlotType.SMALL,  20.0));
        rateCard.put(SlotType.MEDIUM, new SlotTypeInfo("ST-2", SlotType.MEDIUM, 40.0));
        rateCard.put(SlotType.LARGE,  new SlotTypeInfo("ST-3", SlotType.LARGE,  80.0));
    }

    /** Override or add a custom rate for a slot type. */
    public void setRate(SlotType slotType, double hourlyRate) {
        SlotTypeInfo existing = rateCard.get(slotType);
        rateCard.put(slotType, new SlotTypeInfo(existing.getId(), slotType, hourlyRate));
    }

    public SlotTypeInfo getSlotTypeInfo(SlotType slotType) {
        return rateCard.get(slotType);
    }

    /**
     * Calculates the total charge.
     * Duration is in decimal hours (e.g. 2.5 = 2h 30m).
     * Billing unit = 1 hour, rounded up.
     */
    public double calculateCharge(SlotType slotType, double durationHours) {
        long billedHours = (long) Math.ceil(durationHours);
        return rateCard.get(slotType).getHourlyRate() * billedHours;
    }

    /** Prints the current rate card. */
    public void printRateCard() {
        System.out.println("── Rate Card ────────────────────────────");
        rateCard.forEach((type, info) ->
            System.out.printf("  %-8s  Rs. %.2f / hr%n", type, info.getHourlyRate()));
        System.out.println("─────────────────────────────────────────");
    }
}

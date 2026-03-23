package com.parkingsystem.models;

import com.parkingsystem.enums.VehicleType;

/**
 * Represents the `vehicles` entity.
 *
 * Schema:
 *   id            string PK
 *   license_plate string
 *   vehicle_type  string (TWO_WHEELER | CAR | BUS)
 */
public class Vehicle {

    private final String id;
    private final String licensePlate;
    private final VehicleType vehicleType;

    public Vehicle(String id, String licensePlate, VehicleType vehicleType) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
    }

    public String getId()              { return id; }
    public String getLicensePlate()    { return licensePlate; }
    public VehicleType getVehicleType(){ return vehicleType; }

    @Override
    public String toString() {
        return String.format("Vehicle[%s | %s | %s]", id, licensePlate, vehicleType);
    }
}

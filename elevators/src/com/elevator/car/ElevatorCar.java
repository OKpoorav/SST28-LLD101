package com.elevator.car;

import java.util.ArrayList;
import java.util.List;

import com.elevator.emergency.EmergencyEvent;
import com.elevator.emergency.EmergencyEventListener;
import com.elevator.emergency.EmergencyHandler;
import com.elevator.enums.ElevatorStatus;
import com.elevator.model.Door;
import com.elevator.panel.InsidePanel;

public abstract class ElevatorCar implements EmergencyEventListener {

    private int currentFloor;
    private ElevatorStatus status;
    private double weightLimit;
    private InsidePanel insidePanel;
    private Door door;
    private List<EmergencyHandler> emergencyHandlers;

    public ElevatorCar(int totalFloors, double weightLimit) {
        this.currentFloor = 1;
        this.status = ElevatorStatus.STATIONARY;
        this.weightLimit = weightLimit;
        this.insidePanel = new InsidePanel(totalFloors);
        this.door = new Door();
        this.emergencyHandlers = new ArrayList<>();
    }

    public abstract boolean canServiceFloor(int floor);

    public void moveToFloor(int targetFloor) {
        if (status == ElevatorStatus.UNDER_MAINTENANCE) {
            System.out.println("Car is under maintenance, cannot move.");
            return;
        }

        if (door.isOpen()) {
            door.close();
        }

        int previousFloor = currentFloor;
        System.out.println("Car moving from floor " + currentFloor + " to floor " + targetFloor + ".");

        if (targetFloor > previousFloor) {
            status = ElevatorStatus.MOVING_UP;
        } else {
            status = ElevatorStatus.MOVING_DOWN;
        }

        currentFloor = targetFloor;
        status = ElevatorStatus.STATIONARY;

        System.out.println("Car arrived at floor " + targetFloor + ". Doors opening.");
        door.open();
    }

    @Override
    public void onEmergency(EmergencyEvent event) {
        status = ElevatorStatus.STATIONARY;
        door.open();
        System.out.println("Emergency received in elevator. Stopping and opening doors.");

        for (int i = 0; i < emergencyHandlers.size(); i++) {
            EmergencyHandler handler = emergencyHandlers.get(i);
            if (handler.canHandle(event)) {
                handler.handle(event);
            }
        }
    }

    @Override
    public void registerHandler(EmergencyHandler handler) {
        emergencyHandlers.add(handler);
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public ElevatorStatus getStatus() {
        return status;
    }

    public double getWeightLimit() {
        return weightLimit;
    }

    public InsidePanel getInsidePanel() {
        return insidePanel;
    }

    public Door getDoor() {
        return door;
    }
}

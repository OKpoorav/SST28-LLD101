package com.elevator.emergency;

public class BasicEmergencyHandler implements EmergencyHandler {

    @Override
    public boolean canHandle(EmergencyEvent event) {
        return true;
    }

    @Override
    public void handle(EmergencyEvent event) {
        System.out.println("Emergency handled: " + event.getDescription() + " at " + event.getTimestamp());
    }
}

package com.elevator.emergency;

public interface EmergencyHandler {

    void handle(EmergencyEvent event);

    boolean canHandle(EmergencyEvent event);
}

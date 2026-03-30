package com.elevator.emergency;

public interface EmergencyEventListener {

    void onEmergency(EmergencyEvent event);

    void registerHandler(EmergencyHandler handler);
}

package com.elevator.button;

import com.elevator.ElevatorSystem;
import com.elevator.emergency.EmergencyEvent;

public class AlarmButton extends Button {

    @Override
    public void onPress() {
        System.out.println("ALARM triggered!");
        EmergencyEvent event = new EmergencyEvent("Alarm button pressed");
        ElevatorSystem.getInstance().triggerEmergency(event);
    }
}

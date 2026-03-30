package com.elevator.emergency;

import java.time.LocalTime;

public class EmergencyEvent {

    private String description;
    private String timestamp;

    public EmergencyEvent(String description) {
        this.description = description;
        this.timestamp = LocalTime.now().toString();
    }

    public String getDescription() {
        return description;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

package com.elevator.panel;

import java.util.ArrayList;
import java.util.List;

import com.elevator.button.AlarmButton;
import com.elevator.button.CloseButton;
import com.elevator.button.FloorButton;
import com.elevator.button.OpenButton;
import com.elevator.exception.InvalidFloorException;

public class InsidePanel {

    private List<FloorButton> floorButtons;
    private AlarmButton alarmButton;
    private OpenButton openButton;
    private CloseButton closeButton;

    public InsidePanel(int totalFloors) {
        this.floorButtons = new ArrayList<>();
        for (int i = 1; i <= totalFloors; i++) {
            floorButtons.add(new FloorButton(i));
        }
        this.alarmButton = new AlarmButton();
        this.openButton = new OpenButton();
        this.closeButton = new CloseButton();
    }

    public void pressFloor(int floor) {
        for (int i = 0; i < floorButtons.size(); i++) {
            if (floorButtons.get(i).getTargetFloor() == floor) {
                floorButtons.get(i).press();
                return;
            }
        }
        throw new InvalidFloorException("Floor " + floor + " does not exist in this building.");
    }

    public void pressAlarm() {
        alarmButton.press();
    }

    public void pressOpen() {
        openButton.press();
    }

    public void pressClose() {
        closeButton.press();
    }
}

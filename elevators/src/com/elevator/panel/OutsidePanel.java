package com.elevator.panel;

import com.elevator.button.DownButton;
import com.elevator.button.UpButton;
import com.elevator.enums.Direction;
import com.elevator.enums.RequestType;
import com.elevator.model.Request;

public class OutsidePanel {

    private int floor;
    private UpButton upButton;
    private DownButton downButton;

    public OutsidePanel(int floor) {
        this.floor = floor;
        this.upButton = new UpButton(floor);
        this.downButton = new DownButton(floor);
    }

    public Request pressUp() {
        upButton.press();
        return new Request(floor, Direction.UP, RequestType.OUTSIDE);
    }

    public Request pressDown() {
        downButton.press();
        return new Request(floor, Direction.DOWN, RequestType.OUTSIDE);
    }

    public int getFloor() {
        return floor;
    }
}

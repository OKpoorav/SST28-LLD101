package com.elevator.button;

public class DownButton extends Button {

    private int floor;

    public DownButton(int floor) {
        this.floor = floor;
    }

    @Override
    public void onPress() {
        System.out.println("Down button pressed on floor " + floor + ".");
    }
}

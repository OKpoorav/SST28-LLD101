package com.elevator.button;

public class UpButton extends Button {

    private int floor;

    public UpButton(int floor) {
        this.floor = floor;
    }

    @Override
    public void onPress() {
        System.out.println("Up button pressed on floor " + floor + ".");
    }
}

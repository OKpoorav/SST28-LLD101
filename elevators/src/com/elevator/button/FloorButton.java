package com.elevator.button;

public class FloorButton extends Button {

    private int targetFloor;

    public FloorButton(int targetFloor) {
        this.targetFloor = targetFloor;
    }

    @Override
    public void onPress() {
        System.out.println("Floor " + targetFloor + " button pressed inside elevator.");
    }

    public int getTargetFloor() {
        return targetFloor;
    }
}

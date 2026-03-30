package com.elevator.button;

public abstract class Button {

    private boolean isPressed;

    public Button() {
        this.isPressed = false;
    }

    public void press() {
        isPressed = true;
        onPress();
    }

    public void reset() {
        isPressed = false;
    }

    public abstract void onPress();

    public boolean isPressed() {
        return isPressed;
    }
}

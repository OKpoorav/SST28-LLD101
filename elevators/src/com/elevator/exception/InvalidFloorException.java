package com.elevator.exception;

public class InvalidFloorException extends ElevatorException {

    public InvalidFloorException(String message) {
        super(message);
    }

    public InvalidFloorException(String message, Throwable cause) {
        super(message, cause);
    }
}

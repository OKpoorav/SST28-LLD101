package com.elevator.exception;

public class NoCarAvailableException extends ElevatorException {

    public NoCarAvailableException(String message) {
        super(message);
    }

    public NoCarAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

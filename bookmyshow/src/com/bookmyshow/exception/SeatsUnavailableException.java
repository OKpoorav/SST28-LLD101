package com.bookmyshow.exception;

public class SeatsUnavailableException extends RuntimeException {

    public SeatsUnavailableException(String message) {
        super(message);
    }

    public SeatsUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}

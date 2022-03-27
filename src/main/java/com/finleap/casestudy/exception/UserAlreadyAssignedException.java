package com.finleap.casestudy.exception;

public class UserAlreadyAssignedException extends RuntimeException {

    public UserAlreadyAssignedException(final String message) {
        super(message);
    }
}

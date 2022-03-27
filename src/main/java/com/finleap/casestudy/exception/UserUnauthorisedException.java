package com.finleap.casestudy.exception;

public class UserUnauthorisedException extends RuntimeException{

    public UserUnauthorisedException(final String message) {
        super(message);
    }
}

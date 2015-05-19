package com.eleks.tesla.api.exception;

/**
 * Created by bogdan.melnychuk on 18.05.2015.
 */
public class AuthorizationFailedException extends Exception {
    public AuthorizationFailedException() {
    }

    public AuthorizationFailedException(String detailMessage) {
        super(detailMessage);
    }

    public AuthorizationFailedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AuthorizationFailedException(Throwable throwable) {
        super(throwable);
    }
}

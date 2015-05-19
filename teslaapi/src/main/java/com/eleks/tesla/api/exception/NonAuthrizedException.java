package com.eleks.tesla.api.exception;

/**
 * Created by bogdan.melnychuk on 18.05.2015.
 */
public class NonAuthrizedException extends TeslaApiException {
    public NonAuthrizedException() {
    }

    public NonAuthrizedException(String detailMessage) {
        super(detailMessage);
    }

    public NonAuthrizedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public NonAuthrizedException(Throwable throwable) {
        super(throwable);
    }
}

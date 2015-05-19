package com.eleks.tesla.api.exception;

import com.eleks.tesla.api.HttpStatus;

/**
 * Created by bogdan.melnychuk on 18.05.2015.
 */
public class TeslaApiException extends Exception {

    public static TeslaApiException fromCode(int code) {
        switch (code) {
            case HttpStatus.AUTH_CODE:
                return new NonAuthrizedException();
        }
        return new TeslaApiException();
    }

    public TeslaApiException() {
    }

    public TeslaApiException(String detailMessage) {
        super(detailMessage);
    }

    public TeslaApiException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public TeslaApiException(Throwable throwable) {
        super(throwable);
    }
}

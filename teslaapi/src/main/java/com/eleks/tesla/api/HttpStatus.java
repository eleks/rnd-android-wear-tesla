package com.eleks.tesla.api;

/**
 * Created by bogdan.melnychuk on 18.05.2015.
 */
public class HttpStatus {
    public static final int AUTH_CODE = 401;
    public static final int SUCCESS = 200;

    public static boolean isSuccessful(int code) {
        return code == SUCCESS;
    }
}

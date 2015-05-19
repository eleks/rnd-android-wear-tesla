package com.eleks.tesla.api.auth;

public class AuthCredentials {
    public final String secret;
    public final String username;

    public AuthCredentials(String userName, String secret) {
        this.username = userName;
        this.secret = secret;
    }
}

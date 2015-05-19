package com.eleks.tesla.api.auth;

public class AuthCredentials {
    public String secret;
    public String username;

    public AuthCredentials(String userName, String secret) {
        this.username = userName;
        this.secret = secret;
    }
}

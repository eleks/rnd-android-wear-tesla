package com.eleks.tesla.api.auth;

import ch.boye.httpclientandroidlib.client.methods.HttpUriRequest;

public abstract class AuthModule {
    public abstract void authorizeRequest(HttpUriRequest httpurirequest, AuthCredentials authcredentials);
}

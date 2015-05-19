package com.eleks.tesla.api.auth;

import ch.boye.httpclientandroidlib.client.methods.HttpUriRequest;
import ch.boye.httpclientandroidlib.message.BasicHeader;

public class OAuthModule extends AuthModule {
    public OAuthModule() {
    }

    public void authorizeRequest(HttpUriRequest httpurirequest, AuthCredentials authcredentials) {
        if (authcredentials != null) {
            httpurirequest.addHeader(new BasicHeader("Authorization", String.format("Bearer %s", authcredentials.secret)));
        }
    }
}

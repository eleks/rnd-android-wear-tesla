// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.eleks.tesla.api;


import com.eleks.tesla.api.auth.AuthModule;
import com.eleks.tesla.api.decoder.EntityDecoder;

class ApiSpec {
    public AuthModule authModule;
    public EntityDecoder decoder;
    public String host;
    public String path;
    public int port;
    public String scheme;
    public int socketTimeout;

    public ApiSpec(String scheme, String host, int port, int socketTimeout, String path, EntityDecoder entityDecoder, AuthModule authModule) {
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.socketTimeout = socketTimeout;
        this.path = path;
        this.decoder = entityDecoder;
        this.authModule = authModule;
    }

    public String getPathForRequestPath(String s) {
        return (new StringBuilder()).append(path).append("/").append(s).toString();
    }
}

package com.eleks.tesla.api.decoder;

import org.json.JSONException;

import ch.boye.httpclientandroidlib.HttpEntity;

public abstract class EntityDecoder {

    public EntityDecoder() {
    }

    public abstract Object decodeEntity(HttpEntity httpentity) throws JSONException;
}

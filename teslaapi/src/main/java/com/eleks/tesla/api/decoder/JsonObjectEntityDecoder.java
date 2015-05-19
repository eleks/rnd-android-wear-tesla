package com.eleks.tesla.api.decoder;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ch.boye.httpclientandroidlib.HttpEntity;
import ch.boye.httpclientandroidlib.util.EntityUtils;


public class JsonObjectEntityDecoder extends EntityDecoder {
    public Object decodeEntity(HttpEntity httpentity) throws JSONException {
        JSONObject jsonobject = null;
        String jsonString;
        if (httpentity == null) {
            return null;
        }
        try {
            jsonString = EntityUtils.toString(httpentity).trim();
        } catch (Exception e) {
            Log.e("JsonObjectEntityDecoder", "decodeEntity", e);
            return null;
        }
        if (!jsonString.isEmpty()) {
            jsonobject = new JSONObject(jsonString);
        }
        return jsonobject;
    }
}

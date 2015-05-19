package com.eleks.tesla.api;

import android.content.Context;
import android.os.Build;

import com.eleks.tesla.api.auth.AuthCredentials;
import com.eleks.tesla.api.auth.OAuthModule;
import com.eleks.tesla.api.decoder.JsonObjectEntityDecoder;
import com.eleks.tesla.api.exception.TeslaApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.KeyStore;
import java.util.Locale;
import java.util.Map;

/**
 * Created by bogdan.melnychuk on 18.05.2015.
 */
public class TeslaApi {
    private static String OAUTH_CLIENT_ID = BuildConfig.OAUTH_CLIENT_ID;
    private static String OAUTH_CLIENT_SECRET = BuildConfig.OAUTH_CLIENT_SECRET;
    private static String HOST = BuildConfig.TESLA_HOST;

    private ApiSpec jsonHashApiSpec;
    private ApiEngine apiEngine;

    public TeslaApi(KeyStore keystore, File file, boolean debug, String userAgent) {
        apiEngine = new ApiEngine(keystore, file, 15000, 60000, userAgent);
        jsonHashApiSpec = new ApiSpec("https", HOST, -1, -1, "", new JsonObjectEntityDecoder(), new OAuthModule());
    }

    //TODO JSONObject should be reworked to some king of AuthResult entity
    public JSONObject login(final String userName, final String password) throws TeslaApiException {
        apiEngine.getCookieStore().clear();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("grant_type", "password");
            jsonObject.put("client_id", OAUTH_CLIENT_ID);
            jsonObject.put("client_secret", OAUTH_CLIENT_SECRET);
            jsonObject.put("email", userName);
            jsonObject.put("password", password);
            return apiEngine.dispatchJSONPostRequest(jsonHashApiSpec, "oauth/token", jsonObject, null);
        } catch (Exception e) {
            throw new TeslaApiException("Can not perform authorization", e);
        }
    }

    //TODO JSONObject should be reworked to some king of AuthResult entity
    public void getVehicles(AuthCredentials authcredentials) throws TeslaApiException {
        apiEngine.dispatchGetRequest(jsonHashApiSpec, "api/1/vehicles", null, authcredentials);
    }

    public JSONObject getVehicleState(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleDataRequest(vehicleId, "vehicle_state", null, authcredentials);
    }

    public void clearState() {
        apiEngine.getCookieStore().clear();
        apiEngine.saveState();
    }

    public JSONObject honkHorn(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "honk_horn", null, authcredentials);
    }

    public JSONObject lockVehicle(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "door_lock", null, authcredentials);
    }

    public JSONObject getClimateState(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleDataRequest(vehicleId, "climate_state", null, authcredentials);
    }

    public JSONObject flashLights(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "flash_lights", null, authcredentials);
    }

    public JSONObject getChargeState(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleDataRequest(vehicleId, "charge_state", null, authcredentials);
    }

    public JSONObject setChargeLimit(long vehicleId, float chargeLimit, AuthCredentials authcredentials) throws TeslaApiException {
        try {
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("percent", chargeLimit);
            return sendVehicleCommand(vehicleId, "set_charge_limit", jsonobject, authcredentials);
        } catch (JSONException e) {
            throw new TeslaApiException(e);
        }
    }

    public JSONObject setTemps(long vehicleId, float driverTemp, float passangerTemp, AuthCredentials authcredentials) throws TeslaApiException {
        try {
            JSONObject jsonobject = new JSONObject();
            jsonobject.put("driver_temp", String.valueOf(driverTemp));
            jsonobject.put("passenger_temp", String.valueOf(passangerTemp));
            return sendVehicleCommand(vehicleId, "set_temps", jsonobject, authcredentials);
        } catch (JSONException e) {
            throw new TeslaApiException(e);
        }
    }

    public JSONObject unlockVehicle(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "door_unlock", null, authcredentials);
    }

    public JSONObject wakeVehicle(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, String.format(Locale.US, "api/1/vehicles/%d/wake_up", vehicleId), null, authcredentials);
    }

    public JSONObject startCharging(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "charge_start", null, authcredentials);
    }

    public JSONObject startConditioning(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "auto_conditioning_start", null, authcredentials);
    }

    public JSONObject stopCharging(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "charge_stop", null, authcredentials);
    }

    public JSONObject stopConditioning(long vehicleId, AuthCredentials authcredentials) throws TeslaApiException {
        return sendVehicleCommand(vehicleId, "auto_conditioning_stop", null, authcredentials);
    }

    private JSONObject sendVehicleCommand(long vehicleId, String command, JSONObject jsonObject, AuthCredentials authcredentials) throws TeslaApiException {
        command = String.format(Locale.US, "api/1/vehicles/%d/command/%s", vehicleId, command);
        return apiEngine.dispatchJSONPostRequest(jsonHashApiSpec, command, jsonObject, authcredentials);
    }

    private JSONObject sendVehicleDataRequest(long vehicleId, String command, Map<String, String> hashMap, AuthCredentials authcredentials) throws TeslaApiException {
        command = String.format(Locale.US, "api/1/vehicles/%d/data_request/%s", vehicleId, command);
        return apiEngine.dispatchGetRequest(jsonHashApiSpec, command, hashMap, authcredentials);
    }

    // version chage to match read TeslaApp version
    public static String getUserAgentString() {
        //String packageName = "com.teslamotors.tesla";
        //String versionCode = "21";
        //Default value "Unknown";
        String versionName = "5.0.1-1624448";
        return String.format("Model S %s (%s; Android %s %s; %s)", versionName, Build.MODEL, versionName, android.os.Build.VERSION.RELEASE, Locale.getDefault());
    }
}

package com.eleks.tesla.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class PreferencesManager {
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USER_NAME = "user_name";
    private static final String CAR_ID = "car_id";

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getAccessToken(Context context) {
        return getPreferences(context).getString(ACCESS_TOKEN, null);
    }

    public static void putAccessToken(Context context, String value) {
        getPreferences(context).edit().putString(ACCESS_TOKEN, value).commit();
    }

    public static String getUserName(Context context) {
        return getPreferences(context).getString(USER_NAME, null);
    }

    public static void putUserName(Context context, String value) {
        getPreferences(context).edit().putString(USER_NAME, value).commit();
    }

    public static long getCarId(Context context) {
        return getPreferences(context).getLong(CAR_ID, 0);
    }

    public static void putCarId(Context context, long value) {
        getPreferences(context).edit().putLong(CAR_ID, value).commit();
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear().commit();
    }

    public static boolean isLoggedIn(Context context) {
        return !TextUtils.isEmpty(getAccessToken(context));
    }
}

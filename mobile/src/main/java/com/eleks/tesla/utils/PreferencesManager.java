package com.eleks.tesla.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesManager {
    public static final String ACCESS_TOKEN = "has_demo_account";

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getAccessToken(Context context) {
        return getPreferences(context).getString(ACCESS_TOKEN, null);
    }

    public static void putAccessToken(Context context, String value) {
        getPreferences(context).edit().putString(ACCESS_TOKEN, value).commit();
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.clear().commit();
    }
}

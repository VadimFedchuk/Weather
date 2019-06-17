package com.vadimfedchuk1994gmail.weather.tools;

import android.content.Context;
import android.preference.PreferenceManager;

public class WeatherPreferences {

    private static final String PREF_FIRST_START = "first_start";
    private static final String PREF_CONNECTION_CHANGED = "connection_changed";
    private static final String PREF_LANGUAGE = "current_language";

    public static Boolean isFirstStart(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(PREF_FIRST_START, true);
    }

    public static void setStoredStart(Context context, boolean isStartedFirst) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_FIRST_START, isStartedFirst)
                .apply();
    }

    public static Boolean getStoredConnectionChanged(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_CONNECTION_CHANGED, false);
    }

    public static void setStoredConnectionChanged(Context context, boolean isConnection) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_CONNECTION_CHANGED, isConnection)
                .apply();
    }

    public static String getStoredLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_LANGUAGE, "en");
    }

    public static void setStoredLanguage(Context context, String lang) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LANGUAGE, lang)
                .apply();
    }
}

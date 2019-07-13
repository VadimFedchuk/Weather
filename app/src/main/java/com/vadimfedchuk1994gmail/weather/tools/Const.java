package com.vadimfedchuk1994gmail.weather.tools;

import com.vadimfedchuk1994gmail.weather.BuildConfig;

public class Const {

    public static final String LOG = "TESTTEST";
    public static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    public static final String API_KEY = BuildConfig.ApiKey;
    public static final String BASE_URL = "http://autocomplete.wunderground.com/aq";

    public static final String PREF_FIRST_START = "first_start";
    public static final String PREF_CONNECTION_CHANGED = "connection_changed";
    public static final String PREF_LANGUAGE = "current_language";

    public class ImageConst {
        public static final int TYPE_VIEW_CLEAR = 1;
        public static final int TYPE_VIEW_PARTIALLY_CLOUDY = 2;
        public static final int TYPE_VIEW_CLOUDY = 3;
        public static final int TYPE_VIEW_RAIN = 4;
        public static final int TYPE_VIEW_THUNDERSTORM = 5;
        public static final int TYPE_VIEW_MIST = 6;
        public static final int TYPE_VIEW_SNOW = 7;
    }
}

package com.vadimfedchuk1994gmail.weather;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vadimfedchuk1994gmail.weather.tools.WeatherPreferences;

public class Weather extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo WiFiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileInfo != null & mobileInfo.isConnectedOrConnecting() || WiFiInfo != null & WiFiInfo.isConnectedOrConnecting()) {
            WeatherPreferences.setStoredConnectionChanged(context, true);
        } else {
            WeatherPreferences.setStoredConnectionChanged(context, false);
        }
    }
}

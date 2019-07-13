package com.vadimfedchuk1994gmail.weather.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionChangeReceiver extends BroadcastReceiver {
    private onConnectionChangeCallback mOnConnectionChangeCallback;

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting()) {
            WeatherPreferences.setStoredConnectionChanged(context, true);
            if (mOnConnectionChangeCallback != null) {
                mOnConnectionChangeCallback.onConnectionChanged(true);
            }
        } else {
            WeatherPreferences.setStoredConnectionChanged(context, false);
        }
    }

    public void setOnConnectionChangeCallback(onConnectionChangeCallback onConnectionChangeCallback) {
        mOnConnectionChangeCallback = onConnectionChangeCallback;
    }

    public interface onConnectionChangeCallback {
        void onConnectionChanged(boolean isConnected);
    }
}

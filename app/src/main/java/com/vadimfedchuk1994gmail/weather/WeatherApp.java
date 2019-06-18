package com.vadimfedchuk1994gmail.weather;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.tools.WeatherPreferences;

import androidx.room.Room;

public class WeatherApp extends Application {

    public static WeatherApp instance;
    private Context context;
    private AppDatabase database;

    public static WeatherApp getInstance() {
        return instance;
    }

    public Context getContext() {
        return context;
    }

//    private void initDb() {
//        mDatabase = Room.databaseBuilder(this, AppDatabase.class, "database_weather")
//                .build();
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        //initDb();
        ConnectivityManager connectivityManager =
                (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo WiFiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileInfo != null & mobileInfo.isConnectedOrConnecting() || WiFiInfo != null & WiFiInfo.isConnectedOrConnecting()) {
            WeatherPreferences.setStoredConnectionChanged(instance, true);
        } else {
            WeatherPreferences.setStoredConnectionChanged(instance, false);
        }
    }

    public AppDatabase getDatabase() {

        if (database == null) {
            synchronized (WeatherApp.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database_weather")
                            .build();
                }
            }
        }
        return database;
    }
}

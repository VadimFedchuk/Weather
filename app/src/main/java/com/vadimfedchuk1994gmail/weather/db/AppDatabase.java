package com.vadimfedchuk1994gmail.weather.db;

import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Weather.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}

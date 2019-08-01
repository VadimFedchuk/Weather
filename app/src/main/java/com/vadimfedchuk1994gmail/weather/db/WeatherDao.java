package com.vadimfedchuk1994gmail.weather.db;

import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface WeatherDao {

    @Insert
    Completable insert(List<Weather> data);

    @Query("SELECT * FROM Weather")
    Observable<List<Weather>> getAllData();

    @Query("SELECT * FROM Weather WHERE name = :name")
    Single<List<Weather>> getAllDataByCity(String name);

    @Query("SELECT name FROM Weather")
    Observable<List<String>> getCities();

    @Query("SELECT name FROM Weather")
    Single<List<String>> getCity();

    @Query("DELETE FROM Weather WHERE name = :name")
    Completable deleteDataByName(String name);

}

package com.vadimfedchuk1994gmail.weather.db;

import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface WeatherDao {

    @Insert
    Completable insert(List<Weather> data);

    @Query("SELECT * FROM Weather WHERE name = :name")
    Single<Weather> getDataByName(String name);

    @Query("SELECT * FROM Weather")
    Observable<List<Weather>> getAllData();

    @Query("SELECT name FROM Weather")
    Observable<List<String>> getCities();

    @Update
    Completable update(List<Weather> data);

    @Query("DELETE FROM Weather WHERE name = :name")
    Completable deleteDataByName(String name);

}

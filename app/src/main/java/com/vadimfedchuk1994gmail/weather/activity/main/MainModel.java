package com.vadimfedchuk1994gmail.weather.activity.main;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainModel {

    AppDatabase mDatabase;
    WeatherDataSource mWeatherDataSource;
    OnCompleteReadCallback callback;

    public MainModel(AppDatabase database, WeatherDataSource weatherDataSource) {
        mDatabase = database;
        mWeatherDataSource = weatherDataSource;
    }

    public void readData() {
        mDatabase.getWeatherDao().getAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Weather>>() {
                    @Override
                    public void onSuccess(List<Weather> weathers) {
                        callback.onCompleteRead(weathers);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    public void setOnCompleteReadCallback(OnCompleteReadCallback callback) {
        this.callback = callback;
    }

    interface OnCompleteReadCallback {
        void onCompleteRead(List<Weather> weathers);
    }
}

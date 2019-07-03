package com.vadimfedchuk1994gmail.weather.activity.main;

import android.util.Log;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.network.cities_response.CityResponse;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainModel {

    OnCompleteLoadCities callbackLoadCities;
    private AppDatabase mDatabase;
    OnCompleteReadCallback callback;
    private WeatherDataSource mWeatherDataSource;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MainModel(AppDatabase database, WeatherDataSource weatherDataSource) {
        mDatabase = database;
        mWeatherDataSource = weatherDataSource;
    }

    public void readData() {
        List<Weather> list = new ArrayList<>();
        mCompositeDisposable.add(mDatabase.getWeatherDao().getAllData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Weather>, ObservableSource<Weather>>() {
                    @Override
                    public ObservableSource<Weather> apply(List<Weather> weathers) throws Exception {
                        return Observable.fromIterable(weathers);
                    }
                })
                .distinct()
                .subscribeWith(new DisposableObserver<Weather>() {
                    @Override
                    public void onNext(Weather weather) {
                        Log.i(Const.LOG, weather.name + " weather name");
                        list.add(weather);
                        callback.onCompleteRead(list);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        Log.i(Const.LOG, list.size() + "MainModel readData weathers.size()");
                        callback.onCompleteRead(list);
                    }
                }));
    }


    public void setOnCompleteReadCallback(OnCompleteReadCallback callback) {
        this.callback = callback;
    }

    public void setOnCompleteLoadCitiesCallback(OnCompleteLoadCities callback) {
        this.callbackLoadCities = callback;
    }

    public void loadCities(String query) {
        mCompositeDisposable.add(mWeatherDataSource.getCities(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CityResponse>() {
                    @Override
                    public void onSuccess(CityResponse cityResponses) {
                        List<String> cities = new ArrayList<>();
                        if (cityResponses != null) {
                            for (int i = 0; i < cityResponses.getResults().size(); i++) {
                                cities.add(cityResponses.getResults().get(i).getName());
                            }
                            if (!cities.isEmpty()) {
                                callbackLoadCities.onCompleteLoadCities(cities);
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(Const.LOG, e.getMessage() + " loadCities");
                    }
                }));
    }

    interface OnCompleteReadCallback {
        void onCompleteRead(List<Weather> weathers);
    }

    public void closeDisposable() {
        mDatabase.close();
        mCompositeDisposable.dispose();
    }

    interface OnCompleteLoadCities {
        void onCompleteLoadCities(List<String> cityResponse);
    }
}

package com.vadimfedchuk1994gmail.weather.activity.main;

import android.util.Log;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.network.cities_response.CityResponse;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainModel {

    OnCompleteLoadCities callbackLoadCities;
    private AppDatabase mDatabase;
    OnCompleteReadCallback callback;
    OnFailureLoadCallback mFailureLoadCallback;
    private WeatherDataSource mWeatherDataSource;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MainModel(AppDatabase database, WeatherDataSource weatherDataSource) {
        mDatabase = database;
        mWeatherDataSource = weatherDataSource;
    }

    public void loadData(String city) {
        Log.i("TESTTEST", "loadData " + city);
        Single<WeatherResponse> weatherResponseObservable = getLoadObservable(city);
        mCompositeDisposable.add(weatherResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        Log.i(Const.LOG, "loadData " + weatherResponse.getCityName());
                        Log.i(Const.LOG, "loadData " + weatherResponse.getData().size());
                        if (weatherResponse.getData().isEmpty()) {
                            mFailureLoadCallback.onFailureLoad();
                        } else {
                            insertData(weatherResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(Const.LOG, "loadData " + "onError " + e.getMessage());
                        mFailureLoadCallback.onFailureLoad();
                    }
                }));
    }

    private Single<WeatherResponse> getLoadObservable(String city) {
        return mWeatherDataSource.getWeatherData(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void insertData(WeatherResponse weatherResponse) {
        List<Weather> data = Weather.cloneList(weatherResponse.getData(), weatherResponse.getCityName());

        mCompositeDisposable.add(mDatabase.getWeatherDao().insert(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        readData();

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(Const.LOG, "main updateOrInsertData " + e.getMessage());
                        mFailureLoadCallback.onFailureLoad();

                    }
                }));
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
                        Log.i(Const.LOG, weather.getName() + " weather name");
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

    public void setOnFailureLoadCallback(OnFailureLoadCallback callback) {
        this.mFailureLoadCallback = callback;
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

    public void getDeleteObservable(String city) {
        mCompositeDisposable.add(mDatabase.getWeatherDao().deleteDataByName(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.i(Const.LOG, " delete complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(Const.LOG, " delete error " + e.getMessage());
                    }
                }));
    }

    public void closeDisposable() {
        mDatabase.close();
        mCompositeDisposable.dispose();
    }

    interface OnCompleteLoadCities {
        void onCompleteLoadCities(List<String> cityResponse);
    }

    interface OnFailureLoadCallback {
        void onFailureLoad();
    }
}

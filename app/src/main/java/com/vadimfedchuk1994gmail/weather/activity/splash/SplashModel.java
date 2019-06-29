package com.vadimfedchuk1994gmail.weather.activity.splash;

import android.util.Log;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.network.weather_response.Datum;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashModel {


    private WeatherDataSource mWeatherDataSource;
    private OnCompleteCallback callback;
    private AppDatabase mAppDatabase;
    private int countFlag = 0;
    private int countCitiesForUpdate = 1;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    public SplashModel(WeatherDataSource weatherDataSource, AppDatabase database) {
        mWeatherDataSource = weatherDataSource;
        this.mAppDatabase = database;

    }

    public void loadData(String city) {
        mDisposable.add(mWeatherDataSource.getWeatherData(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        Log.i("TESTTEST", weatherResponse.getData().size() + " weatherResponses.size()");
                        ++countFlag;
                        saveData(weatherResponse.getData(), weatherResponse.getCityName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        ++countFlag;
                        Log.i("TESTTEST", "error" + e.getMessage() + " " + e.toString());
                        if (countCitiesForUpdate == countFlag) {
                            if (mAppDatabase != null) {
                                mAppDatabase.close();
                            }
                            callback.onComplete(true);
                        }
                    }
                }));
    }

    private void saveData(List<Datum> weatherResponses, String city) {
        List<Weather> data = Weather.cloneList(weatherResponses, city);
        mDisposable.add(mAppDatabase.getWeatherDao().insert(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.i("TESTTEST", "onComplete " + countCitiesForUpdate + " " + countFlag);
                        if (countCitiesForUpdate == countFlag) {
                            callback.onComplete(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("TESTTEST", " error insert " + e.getMessage());
                        if (countCitiesForUpdate == countFlag) {

                            callback.onComplete(true);
                        }
                    }
                }));
    }

//    private void saveData(List<Datum> weatherResponses, String city) {
//        List<Weather> data = Weather.cloneList(weatherResponses, city);
//        mAppDatabase.getWeatherDao().insert(data)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.i("TESTTEST", "onComplete " + countCitiesForUpdate + " " + countFlag);
//                        if (countCitiesForUpdate == countFlag) {
//                            callback.onComplete(true);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("TESTTEST", " error insert " + e.getMessage());
//                        if (countCitiesForUpdate == countFlag) {
//
//                            callback.onComplete(true);
//                        }
//                    }
//                });
//    }


    public void updateData() {
        mDisposable.add(mAppDatabase.getWeatherDao().getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(List<String> strings) {
                        Log.i("TESTTEST", strings.toString());
                        countCitiesForUpdate = strings.size();
                        for (String city : strings) {
                            loadData(city);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("TESTTEST", " error read " + e.getMessage());
                        callback.onComplete(false);
                    }
                }));
    }

    public void disposeDisposable() {
        mDisposable.dispose();
    }

    public void setOnCompleteCallback(OnCompleteCallback callback) {
        this.callback = callback;
    }

    interface OnCompleteCallback {
        void onComplete(boolean isUpdatedSuccessfully);
    }
}

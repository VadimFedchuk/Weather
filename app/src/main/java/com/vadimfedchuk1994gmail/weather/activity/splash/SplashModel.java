package com.vadimfedchuk1994gmail.weather.activity.splash;

import android.util.Log;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;

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

public class SplashModel {


    private WeatherDataSource mWeatherDataSource;
    private OnCompleteCallback callback;
    private AppDatabase mAppDatabase;
    private int countFlag = 0;
    private int countCitiesForUpdate = 1;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public SplashModel(WeatherDataSource weatherDataSource, AppDatabase database) {
        mWeatherDataSource = weatherDataSource;
        this.mAppDatabase = database;

    }

    public void loadData(String city) {
        Single<WeatherResponse> weatherResponseObservable = getLoadObservable(city);
        mCompositeDisposable.add(weatherResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        if (weatherResponse.getData().isEmpty()) {
                            if (countFlag == countCitiesForUpdate) {
                                callback.onComplete(false);
                            }
                        } else {
                            updateOrInsertData(weatherResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onComplete(false);
                    }
                }));
    }

    public void updateData() {
        countCitiesForUpdate = 0;
        mCompositeDisposable.add(mAppDatabase.getWeatherDao().getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<List<String>, ObservableSource<String>>) cities -> {
                    if (cities.isEmpty()) {
                        callback.onComplete(false);
                    }
                    return Observable.fromIterable(cities);
                })
                .distinct()
                .flatMap((Function<String, ObservableSource<WeatherResponse>>) s -> {
                    ++countCitiesForUpdate;
                    return getLoadObservable(s).toObservable();
                }).subscribeWith(new DisposableObserver<WeatherResponse>() {
                    @Override
                    public void onNext(WeatherResponse weatherResponse) {
                        if (weatherResponse != null) {
                            getDeleteObservable(weatherResponse.getCityName());
                            updateOrInsertData(weatherResponse);
                        } else {
                            if (countFlag == countCitiesForUpdate) {
                                callback.onComplete(false);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (countFlag == countCitiesForUpdate) {
                            callback.onComplete(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                })
        );
    }

    private Single<WeatherResponse> getLoadObservable(String city) {
        ++countFlag;
        return mWeatherDataSource.getWeatherData(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void getDeleteObservable(String city) {
        mCompositeDisposable.add(mAppDatabase.getWeatherDao().deleteDataByName(city)
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

    private void updateOrInsertData(WeatherResponse weatherResponse) {
        List<Weather> data = Weather.cloneList(weatherResponse.getData(), weatherResponse.getCityName());

        mCompositeDisposable.add(mAppDatabase.getWeatherDao().insert(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        if (countFlag == countCitiesForUpdate) {
                            callback.onComplete(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (countFlag == countCitiesForUpdate) {
                            callback.onComplete(false);
                        }
                    }
                }));
    }

    public void disposeDisposable() {
        mCompositeDisposable.dispose();
    }

    public void setOnCompleteCallback(OnCompleteCallback callback) {
        this.callback = callback;
    }

    interface OnCompleteCallback {
        void onComplete(boolean isUpdatedSuccessfully);
    }
}

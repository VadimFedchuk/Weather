package com.vadimfedchuk1994gmail.weather.activity.splash;

import android.util.Log;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class SplashModel {


    private WeatherDataSource mWeatherDataSource;
    private OnCompleteCallback callback;
    private AppDatabase mAppDatabase;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public SplashModel(WeatherDataSource weatherDataSource, AppDatabase database) {
        mWeatherDataSource = weatherDataSource;
        this.mAppDatabase = database;

    }

    public void loadData(String city) {
        Log.i(Const.LOG, "loadData splash model " + city);
        Single<WeatherResponse> weatherResponseObservable = getLoadObservable(city);
        mCompositeDisposable.add(weatherResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .toList()
                .flatMapCompletable(new Function<List<WeatherResponse>, CompletableSource>() {
                    @Override
                    public CompletableSource apply(List<WeatherResponse> weatherResponses) throws Exception {
                        return insertData(weatherResponses);
                    }
                })
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.i(Const.LOG, "onComplete()");
                        callback.onComplete(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(Const.LOG, "onError() " + e.getMessage());
                        callback.onComplete(false);
                    }
                }));
    }

    public void updateData() {
        mCompositeDisposable.add(getCities().toObservable()
                .flatMap(new Function<List<String>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(List<String> strings) throws Exception {
                        Log.i(Const.LOG, "city size" + strings.size());
                        return Observable.fromIterable(strings);
                    }
                })
                .distinct()
                .flatMap(new Function<String, ObservableSource<WeatherResponse>>() {
                    @Override
                    public ObservableSource<WeatherResponse> apply(String city) throws Exception {
                        Log.i(Const.LOG, "city " + city);
                        return getLoadObservable(city).toObservable();
                    }
                })
                .toList()
                .flatMapCompletable(new Function<List<WeatherResponse>, CompletableSource>() {
                    @Override
                    public CompletableSource apply(List<WeatherResponse> weatherResponses) throws Exception {
                        Log.i(Const.LOG, "apply " + weatherResponses.size());
                        deleteData();
                        return insertData(weatherResponses);
                    }
                })
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Log.i(Const.LOG, "onComplete()");
                        callback.onComplete(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(Const.LOG, "onError() " + e.getMessage());
                        callback.onComplete(false);
                    }
                }));
    }

    private Single<WeatherResponse> getLoadObservable(String city) {
        return mWeatherDataSource.getWeatherData(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Completable insertData(List<WeatherResponse> weatherResponses) {
        for (WeatherResponse obj : weatherResponses) {
            Log.i(Const.LOG, "в цикле город " + obj.getCityName());
            List<Weather> data = Weather.cloneList(obj.getData(), obj.getCityName());
            Log.i(Const.LOG, data.size() + " data.size");
            mAppDatabase.getWeatherDao().insert(data)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableSingleObserver<List<Long>>() {
                        @Override
                        public void onSuccess(List<Long> list) {
                            for (Long i : list) {
                                Log.i(Const.LOG, "id " + i);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }
                    });
        }
        Log.i(Const.LOG, "перед Completable.complete() ");
        return Completable.complete();
    }

    private Single<List<String>> getCities() {
        return mAppDatabase.getWeatherDao().getCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Completable deleteData() {
        Log.i(Const.LOG, "deleteData ");
        mCompositeDisposable.add(Observable.fromCallable(new CallableLongAction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                    }
                }));

        return Completable.complete();
    }

    class CallableLongAction implements Callable<Integer> {

        @Override
        public Integer call() throws Exception {
            List<Weather> listToDelete = mAppDatabase.getWeatherDao().getAllData();
            return mAppDatabase.getWeatherDao().deleteAllData(listToDelete);
        }
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


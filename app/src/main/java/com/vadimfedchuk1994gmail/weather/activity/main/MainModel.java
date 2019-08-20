package com.vadimfedchuk1994gmail.weather.activity.main;

import android.util.Log;

import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.network.cities_response.CityResponse;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

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
    private static Date currentDate;
    OnFailureLoadCallback mFailureLoadCallback;
    private WeatherDataSource mWeatherDataSource;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private static DateFormat dateFormat;

    static {
        currentDate = new Date();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(currentDate);
        try {
            currentDate = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public MainModel(AppDatabase database, WeatherDataSource weatherDataSource) {
        mDatabase = database;
        mWeatherDataSource = weatherDataSource;
    }

    OnCompleteReadCallback callbackReadComplete;

    public void loadData(String city) {
        //Log.i("TESTTEST", "loadData " + city);
        Single<WeatherResponse> weatherResponseObservable = getLoadObservable(city);
        mCompositeDisposable.add(weatherResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
//                        Log.i(Const.LOG, "loadData " + weatherResponse.getCityName());
//                        Log.i(Const.LOG, "loadData " + weatherResponse.getData().size());
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
//        List<Weather> data = Weather.cloneList(weatherResponse.getData(), weatherResponse.getCityName());
//
//        mCompositeDisposable.add(mDatabase.getWeatherDao().insert(data)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableCompletableObserver() {
//                    @Override
//                    public void onComplete() {
//                        readData();
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(Const.LOG, "main updateOrInsertData " + e.getMessage());
//                        mFailureLoadCallback.onFailureLoad();
//
//                    }
//                }));
    }

//    public void readData() {
//        getCountCities();
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        Date date = new Date();
//        List<Weather> list = new ArrayList<>();
//        mCompositeDisposable.add(mDatabase.getWeatherDao().getData()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<List<Weather>>() {
//                    @Override
//                    public void onNext(List<Weather> weathers) {
//                        Log.i(Const.LOG, weathers.size() + " weathers.size() main");
//                        for (Weather obj : weathers) {
//                            Log.i(Const.LOG, obj.getName() + " name + date " + obj.getDate());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i(Const.LOG, e.getMessage() + " error");
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                })
//            );
//    }

    public void readData() {
        mCompositeDisposable.add(mDatabase.getWeatherDao().getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .flatMap(new Function<List<Weather>, ObservableSource<ArrayList<List<Weather>>>>() {
                    @Override
                    public ObservableSource<ArrayList<List<Weather>>> apply(List<Weather> weathers) throws Exception {
                        Set<String> list = new HashSet<>();
                        for (Weather city : weathers) {
                            list.add(city.getName());
                        }
                        return adaptionData(weathers, list.size()).toObservable();
                    }
                })
                .subscribeWith(new DisposableObserver<ArrayList<List<Weather>>>() {
                    @Override
                    public void onNext(ArrayList<List<Weather>> lists) {
                        callbackReadComplete.onCompleteRead(lists);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private Single<ArrayList<List<Weather>>> adaptionData(List<Weather> weathers, int size) {
        return Single.fromCallable(new CallableLongAction(weathers, size))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
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

    public void setOnCompleteReadCallback(OnCompleteReadCallback callback) {
        this.callbackReadComplete = callback;
    }

    interface OnCompleteReadCallback {
        void onCompleteRead(ArrayList<List<Weather>> weathers);
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

    class CallableLongAction implements Callable<ArrayList<List<Weather>>> {

        private List<Weather> arrayToChunk;
        private int chunkSize;

        public CallableLongAction(List<Weather> arrayToChunk, int chunkSize) {
            this.arrayToChunk = arrayToChunk;
            this.chunkSize = chunkSize;
        }

        @Override
        public ArrayList<List<Weather>> call() throws Exception {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(new Date());
            Date date = dateFormat.parse(dateString);
            List<Weather> arrayToChunk1 = new ArrayList<>(arrayToChunk);
            arrayToChunk.clear();
            for (Weather weather : arrayToChunk1) {
                Date date1 = dateFormat.parse(weather.getDate());
                if (date1.compareTo(date) >= 0) {
                    arrayToChunk.add(weather);
                }
            }
            ArrayList<List<Weather>> chunkList = new ArrayList<>();
            int guide = arrayToChunk.size();
            int index = 0;
            int tale = arrayToChunk.size() / chunkSize;
            for (int i = 0; i < chunkSize; i++) {
                chunkList.add(arrayToChunk.subList(index, tale));
                index = index + guide / chunkSize;
                tale = tale + guide / chunkSize;
            }
            date = null;
            dateFormat = null;
            dateString = null;
            arrayToChunk1 = null;
            return chunkList;
        }
    }
}

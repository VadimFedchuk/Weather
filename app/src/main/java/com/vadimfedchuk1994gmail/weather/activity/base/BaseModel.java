package com.vadimfedchuk1994gmail.weather.activity.base;

public abstract class BaseModel {

//    public void loadData(String city) {
//        mWeatherDataSource.getWeatherData(city)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
//                    @Override
//                    public void onSuccess(WeatherResponse weatherResponse) {
//                        Log.i("TESTTEST", weatherResponse.getData().size() + " weatherResponses.size()");
//                        saveData(weatherResponse.getData(), weatherResponse.getCityName());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("TESTTEST", "error" + e.getMessage() + " " + e.toString());
//                    }
//                });
//    }
//
//    private void saveData(List<Datum> weatherResponses, String city) {
//        List<Weather> data = Weather.cloneList(weatherResponses, city);
//        Log.i("TESTTEST",  " save " + data.size());
//        mAppDatabase.getWeatherDao().insert(data)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(Disposable d) {}
//
//                    @Override
//                    public void onComplete() {
//                        callback.onComplete(true);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("TESTTEST",  " error insert " + e.getMessage());
//                        callback.onComplete(false);
//                    }
//                });
//    }
}

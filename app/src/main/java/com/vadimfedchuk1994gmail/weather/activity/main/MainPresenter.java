package com.vadimfedchuk1994gmail.weather.activity.main;

import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import java.util.List;

public class MainPresenter implements MainModel.OnCompleteReadCallback,
        MainModel.OnCompleteLoadCities, MainModel.OnFailureLoadCallback {

    private MainActivity view;
    private MainModel model;

    public MainPresenter(MainModel mainModel) {
        this.model = mainModel;
        model.setOnCompleteReadCallback(this);
        model.setOnCompleteLoadCitiesCallback(this);
        model.setOnFailureLoadCallback(this);
    }

    public void attachView(MainActivity activity) {
        this.view = activity;
    }

    public void detachView() {
        view = null;
        model.closeDisposable();
    }

    public void viewIsReady() {
        model.readData();
    }

    @Override
    public void onCompleteRead(List<Weather> weathers) {
        view.showData(weathers);
    }

    public void loadCities(String query) {
        model.loadCities(query);
    }

    public void deleteData(String city) {
        model.getDeleteObservable(city);
    }

    @Override
    public void onCompleteLoadCities(List<String> cityResponse) {
        view.onCompleteLoadCities(cityResponse);
    }

    @Override
    public void onFailureLoad() {
        view.showAlertDialog(MainActivity.TypeStart.NO_UPDATE);
    }

    public void loadData(String city) {
        model.loadData(city);
    }
}

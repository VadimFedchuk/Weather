package com.vadimfedchuk1994gmail.weather.activity.main;

import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import java.util.List;

public class MainPresenter implements MainModel.OnCompleteReadCallback {

    private MainActivity view;
    private MainModel model;

    public MainPresenter(MainModel mainModel) {
        this.model = mainModel;
        model.setOnCompleteReadCallback(this);
    }

    public void attachView(MainActivity activity) {
        this.view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {
        model.readData();
    }

    @Override
    public void onCompleteRead(List<Weather> weathers) {
        view.showData(weathers);
    }
}

package com.vadimfedchuk1994gmail.weather.activity.main;

public class MainPresenter {

    private MainActivity view;
    private MainModel model;

    public MainPresenter(MainModel mainModel) {
        this.model = mainModel;
    }

    public void attachView(MainActivity activity) {
        this.view = activity;
    }

    public void detachView() {
        view = null;
    }

    public void viewIsReady() {

    }
}

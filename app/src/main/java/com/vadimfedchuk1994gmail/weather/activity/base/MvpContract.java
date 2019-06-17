package com.vadimfedchuk1994gmail.weather.activity.base;

public interface MvpContract {

    interface View {
        void init();
        void showProgressBar();
        void hideProgressBar();
    }

    interface Presenter {

    }
}

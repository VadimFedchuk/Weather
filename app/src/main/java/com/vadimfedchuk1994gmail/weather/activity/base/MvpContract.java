package com.vadimfedchuk1994gmail.weather.activity.base;

public interface MvpContract {

    interface View {
        void init();

        void initBars();
        void showProgressBar();
        void hideProgressBar();

        void showDialogFragment();

    }

    interface Presenter {
        void viewIsReady();

        void attachView();

        void detachView();
    }
}

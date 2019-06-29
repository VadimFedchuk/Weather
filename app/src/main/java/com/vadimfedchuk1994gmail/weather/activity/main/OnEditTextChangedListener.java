package com.vadimfedchuk1994gmail.weather.activity.main;

public interface OnEditTextChangedListener {

    void onEditTextChanged(String query, OnCompleteLoad callback);

    public interface OnCompleteLoad {
        void onCompleteLoad(String q);
    }
}

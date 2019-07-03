package com.vadimfedchuk1994gmail.weather.activity.main;

import java.util.List;

public interface OnEditTextChangedListener {

    void onEditTextChanged(String query, OnCompleteLoad callback);

    interface OnCompleteLoad {
        void onCompleteLoad(List<String> list);
    }
}

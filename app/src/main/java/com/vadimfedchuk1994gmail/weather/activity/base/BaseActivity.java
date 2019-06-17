package com.vadimfedchuk1994gmail.weather.activity.base;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

public abstract class BaseActivity extends AppCompatActivity implements MvpContract.View{

    public abstract void initBars();

    public void showSnackBar(CoordinatorLayout coordinatorLayout, FloatingActionButton fab, String text) {

        Snackbar snackbar = Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) snackBarView.getLayoutParams();
        params.setMargins(
                params.leftMargin,
                params.topMargin,
                params.rightMargin,
                params.bottomMargin + 100);
        snackBarView.setLayoutParams(params);
        snackbar.setAnchorView(fab);
        snackbar.show();

    }
}

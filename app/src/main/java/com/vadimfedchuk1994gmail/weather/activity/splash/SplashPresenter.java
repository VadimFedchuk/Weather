package com.vadimfedchuk1994gmail.weather.activity.splash;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.vadimfedchuk1994gmail.weather.activity.main.MainActivity;
import com.vadimfedchuk1994gmail.weather.tools.Const;
import com.vadimfedchuk1994gmail.weather.tools.WeatherPreferences;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SplashPresenter implements SplashModel.OnCompleteCallback {

    private SplashModel model;
    private SplashActivity mActivity;
    private Context mContext;

    public SplashPresenter(SplashModel model) {
        this.model = model;
        model.setOnCompleteCallback(this);
    }

    public void attachView(SplashActivity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
    }

    public void detachView() {
        model.disposeDisposable();
        mActivity = null;

    }

    public void viewIsReady() {
        mActivity.showProgressBar();
        if (WeatherPreferences.isFirstStart(mContext) && WeatherPreferences.getStoredConnectionChanged(mContext)) {
            WeatherPreferences.setStoredStart(mContext, false);
            checkCurrentLocation();
            Log.i(Const.LOG, "view is ready 1");
        } else if (WeatherPreferences.getStoredConnectionChanged(mContext)) {
            Log.i(Const.LOG, "view is ready 2");
            model.updateData();
        } else {
            Log.i(Const.LOG, "view is ready 3");
            mActivity.startActivity(MainActivity.TypeStart.NO_UPDATE);
        }
    }


    private void checkCurrentLocation() {
        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mActivity);
        Locale myLocale = new Locale("en");
        if (errorCode == ConnectionResult.SUCCESS) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);

            if (ActivityCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(mActivity, location -> {
                Geocoder gcd = new Geocoder(mActivity, myLocale);
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                } catch (IOException e) {
                    Toast.makeText(mActivity, "failed splashpresenter " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                if (addresses != null && addresses.size() != 0) {
                    downloadData(addresses.get(0).getLocality());
                } else {
                    mActivity.startActivity(MainActivity.TypeStart.NO_UPDATE);
                }
            });
        } else {
            mActivity.startActivity(MainActivity.TypeStart.NO_UPDATE);
        }
    }

    private void downloadData(String locality) {
        model.loadData(locality);
    }

    @Override
    public void onComplete(boolean isUpdatedSuccessfully) {
        if (isUpdatedSuccessfully) {
            mActivity.startActivity(MainActivity.TypeStart.UPDATE);
        } else {
            mActivity.startActivity(MainActivity.TypeStart.NO_UPDATE);
        }
    }
}


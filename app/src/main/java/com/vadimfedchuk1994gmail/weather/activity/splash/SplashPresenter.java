package com.vadimfedchuk1994gmail.weather.activity.splash;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.vadimfedchuk1994gmail.weather.activity.main.MainActivity;
import com.vadimfedchuk1994gmail.weather.tools.WeatherPreferences;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;

public class SplashPresenter {

    private SplashModel model;
    private SplashActivity mActivity;
    private Context mContext;
    public SplashPresenter(SplashModel model) {
        this.model = model;
    }

    public void attachView(SplashActivity activity) {
        mActivity = activity;
        mContext = activity.getApplicationContext();
    }

    public void detachView() {
        mActivity = null;
    }

    public void viewIsReady() {
        mActivity.showProgressBar();
        if (WeatherPreferences.isFirstStart(mContext) && WeatherPreferences.getStoredConnectionChanged(mContext)) {
            WeatherPreferences.setStoredStart(mContext, false);
            checkCurrentLocation();
        } else if (WeatherPreferences.getStoredConnectionChanged(mContext)) {
            // читать города с базы
            downloadData("123", "456");
        } else {
            mActivity.startActivity(MainActivity.TypeStart.NO_UPDATE);
        }
    }

    private void checkCurrentLocation() {
        int errorCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(mActivity);
        Locale myLocale = new Locale(WeatherPreferences.getStoredLanguage(mActivity));
        if (errorCode == ConnectionResult.SUCCESS) {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
            if (ActivityCompat.checkSelfPermission(mActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //показать снекбар с текстом разрешите приложению в настройках использовать местополождение
            }
            fusedLocationClient.getLastLocation().addOnSuccessListener(mActivity, location -> {
                Geocoder gcd = new Geocoder(mActivity, myLocale);
                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses.size() != 0) {
                    downloadData(addresses.get(0).getLocality(), addresses.get(0).getCountryCode());
                }
            });
        } else {
            mActivity.startActivity(MainActivity.TypeStart.NO_UPDATE);
        }
    }

    private void downloadData(String locality, String countryCode) {
        model.loadSaveData(locality, countryCode, mActivity, new SplashModel.OnCompleteCallback() {
            @Override
            public void onComplete() {
                mActivity.startActivity(MainActivity.TypeStart.UPDATE);
            }
        });
    }
}


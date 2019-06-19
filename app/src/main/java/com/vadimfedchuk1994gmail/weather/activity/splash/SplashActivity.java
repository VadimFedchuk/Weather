package com.vadimfedchuk1994gmail.weather.activity.splash;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.WeatherApp;
import com.vadimfedchuk1994gmail.weather.activity.main.MainActivity;
import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.tools.ConnectionChangeReceiver;
import com.vadimfedchuk1994gmail.weather.tools.WeatherPreferences;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private DilatingDotsProgressBar mDilatingDotsProgressBar;
    private SplashPresenter presenter;
    GoogleApiClient mClient;
    ConnectionChangeReceiver mConnectionChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mConnectionChangeReceiver = new ConnectionChangeReceiver();
        registerReceiver(mConnectionChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        setCurrentLanguage();

        if(WeatherPreferences.isFirstStart(this)) {
            mClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
            LocationRequest request = LocationRequest.create();
            request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            request.setNumUpdates(1);
            request.setInterval(0);
        }
        init();
    }

    private void setCurrentLanguage() {
        Locale currentLocale = getResources().getConfiguration().locale;
        switch (currentLocale.getLanguage()) {
            case "ru" :
            case "be" :
            case "uk": WeatherPreferences.setStoredLanguage(this, "ru");
                break;
            default: WeatherPreferences.setStoredLanguage(this, "en");
                break;
        }
    }

    private void init() {
        AppDatabase database = WeatherApp.getInstance().getDatabase();
        WeatherDataSource source = new WeatherDataSource(this);
        SplashModel model = new SplashModel(source, database);
        presenter = new SplashPresenter(model);
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    public void onStart() {
        super.onStart();
        if(WeatherPreferences.isFirstStart(this)){
            mClient.connect();
        }
    }

    public void onStop() {
        super.onStop();
        if(mClient != null) {
            mClient.disconnect();
        }
    }

    public void showProgressBar() {
        mDilatingDotsProgressBar = findViewById(R.id.progress);
        mDilatingDotsProgressBar.showNow();
    }

    public void hideProgressBar() {
        if(mDilatingDotsProgressBar != null) {
            mDilatingDotsProgressBar.hideNow();
        }
    }

    public void startActivity(MainActivity.TypeStart type) {
        hideProgressBar();
        startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("type", type).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        Animatoo.animateCard(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
        presenter.detachView();
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mConnectionChangeReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}

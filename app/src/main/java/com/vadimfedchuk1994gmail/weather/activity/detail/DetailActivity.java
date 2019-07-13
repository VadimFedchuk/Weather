package com.vadimfedchuk1994gmail.weather.activity.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.WeatherApp;
import com.vadimfedchuk1994gmail.weather.activity.SettingsActivity;
import com.vadimfedchuk1994gmail.weather.adapters.CardPagerAdapter;
import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends AppCompatActivity {

    SharedPreferences sp;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private AppDatabase mAppDatabase;
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private String city;
    private TextView mTextViewToolbar;
    private boolean isUnitCelsius = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_detail);
        city = getIntent().getStringExtra("city");
        mViewPager = findViewById(R.id.viewPager);
        mAppDatabase = WeatherApp.instance.getDatabase();
        setTitle("");
        initBars();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void initBars() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTextViewToolbar = toolbar.findViewById(R.id.toolbar_title);
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        FloatingActionButton myFab = findViewById(R.id.fab);
        myFab.setColorFilter(Color.WHITE);
        myFab.setOnClickListener(v -> {
            Animatoo.animateSlideRight(this);
            finish();
        });
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (item.getItemId() == R.id.action_ads) {

            }
            return true;
        });
    }

    public void readData() {
        List<Weather> weathers = new ArrayList<>();
        Date date = new Date();
        mAppDatabase.getWeatherDao().getAllDatabyCity(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Weather>, ObservableSource<Weather>>() {
                    @Override
                    public ObservableSource<Weather> apply(List<Weather> weathers) throws Exception {
                        return Observable.fromIterable(weathers);
                    }
                })
                .filter(new Predicate<Weather>() {
                    @Override
                    public boolean test(Weather weather) throws Exception {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(weather.getDate());
                        Log.i(Const.LOG, "weathers.size " + date1.after(date));
                        return date1.after(date);
                    }
                })
                .toList()
                .subscribe(new SingleObserver<List<Weather>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Weather> weathers) {
                        Log.i(Const.LOG, "weathers.size " + weathers.size());
                        initialAdapter(weathers);
                        mTextViewToolbar.setText(weathers.get(0).getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(Const.LOG, "error detail " + e.getMessage());
                    }
                });
    }

    private void initialAdapter(List<Weather> weathers) {
        mCardAdapter = new CardPagerAdapter(this, weathers, isUnitCelsius);
        mViewPager.setAdapter(mCardAdapter);
    }

    @Override
    protected void onResume() {
        String scale = sp.getString("key_temperature_units", "1");
        isUnitCelsius = scale.equals("1");
        if (mCardAdapter != null) {
            mCardAdapter = null;
        }
        readData();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}

package com.vadimfedchuk1994gmail.weather.activity.main;


import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tapadoo.alerter.Alerter;
import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.SearchCityDialogFragment;
import com.vadimfedchuk1994gmail.weather.WeatherApp;
import com.vadimfedchuk1994gmail.weather.activity.base.BaseActivity;
import com.vadimfedchuk1994gmail.weather.adapters.MainAdapter;
import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends BaseActivity implements
        SearchCityDialogFragment.AddCityDialogListener,
        MainAdapter.ClickListener,
        OnEditTextChangedListener {

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager layout;
    private MainPresenter presenter;
    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton myFab;
    AppDatabase database;
    private LinearLayout layoutEmpty;
    private OnCompleteLoad callbackOnCompleteLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutEmpty = findViewById(R.id.empty_view_layout);
        layoutEmpty.setVisibility(View.VISIBLE);
        TypeStart type = (TypeStart) getIntent().getSerializableExtra("type");
        showAlertDialog(type);
        initBars();
        init();
        setTitle("");

    }

    private void showAlertDialog(TypeStart type) {
        Alerter alerter = Alerter.create(this);
        alerter.setDuration(2000);
        alerter.setEnterAnimation(R.anim.alerter_slide_in_from_left);
        alerter.setExitAnimation(R.anim.alerter_slide_out_to_right);
        alerter.enableSwipeToDismiss();
        if (type == TypeStart.UPDATE) {
            alerter.setText("Погода успешно обновлена");
            alerter.setBackgroundColorRes(R.color.colorSuccessfullyUpdate);
            alerter.setIcon(R.drawable.ic_success_upload);
        } else if (type == TypeStart.NO_UPDATE) {
            alerter.setText("Погода не обновлена");
            alerter.setBackgroundColorRes(R.color.colorFailureUpdate);
            alerter.setIcon(R.drawable.ic_fail_upload);
        }
        alerter.show();
    }

    @Override
    public void initBars() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mCoordinatorLayout = findViewById(R.id.main_coordinator_layout);
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        myFab = findViewById(R.id.fab);
        myFab.setColorFilter(Color.WHITE);
        myFab.setOnClickListener(v -> showDialogFragment());
        bottomAppBar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_settings) {
                showSnackBar(mCoordinatorLayout, myFab,"Settings");
            }
            return true;
        });
    }

    @Override
    public void showDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        SearchCityDialogFragment searchCityDialogFragment = SearchCityDialogFragment.newInstance("Some Title");
        searchCityDialogFragment.show(fm, "fragment_edit_name");
        searchCityDialogFragment.setOnEditTextChangedListener(this);

    }


    public void showData(List<Weather> data) {
        if (data.size() > 0) {
            layoutEmpty.setVisibility(View.GONE);
            Log.i(Const.LOG, data.get(0).date + " " + data.get(data.size() - 1).date);
        }

        mAdapter.setList(data);
    }

    @Override
    public void onCompleteAddCity(String city) {
        showSnackBar(mCoordinatorLayout, myFab, city);
    }

    @Override
    public void init() {
        mRecyclerView = findViewById(R.id.recyclerView);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout = new GridLayoutManager(this, 2);
        } else {
            layout = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(layout);

        mAdapter = new MainAdapter(this, new ArrayList<>());
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        database = WeatherApp.getInstance().getDatabase();
        WeatherDataSource source = new WeatherDataSource(this);
        MainModel model = new MainModel(database, source);
        presenter = new MainPresenter(model);
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    // from adapter
    @Override
    public void onClick(String name) {
//open detail activity
    }

    // from adapter
    @Override
    public void onLongClick(Weather data) {
//open dialog fragment
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (database != null) {
            database.close();
        }
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void onEditTextChanged(String query, OnCompleteLoad callback) {
        Log.i(Const.LOG, query);
        presenter.loadCities(query);
        callbackOnCompleteLoad = callback;
    }

    public void onCompleteLoadCities(List<String> cityResponse) {
        callbackOnCompleteLoad.onCompleteLoad(cityResponse);
    }

    public enum TypeStart {
        UPDATE, NO_UPDATE
    }
}

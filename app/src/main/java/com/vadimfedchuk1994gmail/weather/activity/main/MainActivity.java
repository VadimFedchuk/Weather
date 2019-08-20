package com.vadimfedchuk1994gmail.weather.activity.main;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.tapadoo.alerter.Alerter;
import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.SearchCityDialogFragment;
import com.vadimfedchuk1994gmail.weather.WeatherApp;
import com.vadimfedchuk1994gmail.weather.activity.SettingsActivity;
import com.vadimfedchuk1994gmail.weather.activity.base.BaseActivity;
import com.vadimfedchuk1994gmail.weather.activity.detail.DetailActivity;
import com.vadimfedchuk1994gmail.weather.adapters.MainAdapter;
import com.vadimfedchuk1994gmail.weather.db.AppDatabase;
import com.vadimfedchuk1994gmail.weather.network.WeatherDataSource;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.Const;
import com.vadimfedchuk1994gmail.weather.tools.RecyclerItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends BaseActivity implements
        SearchCityDialogFragment.AddCityDialogListener,
        MainAdapter.ClickListener,
        OnEditTextChangedListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager layout;
    private MainPresenter presenter;
    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton myFab;
    AppDatabase database;
    private LinearLayout layoutEmpty;
    private OnCompleteLoad callbackOnCompleteLoad;
    SharedPreferences sp;
    private List<Weather> data = new ArrayList<>();
    private boolean isUnitCelsius = true;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progressBar);
        showProgressBar();
        layoutEmpty = findViewById(R.id.empty_view_layout);

        TypeStart type = (TypeStart) getIntent().getSerializableExtra("type");
        showAlertDialog(type);
        initBars();
        init();
        setTitle("");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void showAlertDialog(TypeStart type) {
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
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            } else if (item.getItemId() == R.id.action_ads) {
                
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


    public void showData(ArrayList<List<Weather>> data) {
        if (data.size() > 0) {
            layoutEmpty.setVisibility(View.GONE);
            this.data.clear();
            //this.data.addAll(data);
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
        }
        hideProgressBar();
        //mAdapter.setList(data);
    }

    @Override
    public void onCompleteAddCity(String city) {
        showSnackBar(mCoordinatorLayout, myFab, city);
        int a = city.indexOf(",");
        city = city.substring(0, a);
        presenter.loadData(city);
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
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new MainAdapter(this, new ArrayList<>(), isUnitCelsius);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        database = WeatherApp.getInstance().getDatabase();
        WeatherDataSource source = new WeatherDataSource(this);
        MainModel model = new MainModel(database, source);
        presenter = new MainPresenter(model);
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    // from adapter
    @Override
    public void onClick(String name) {
        startActivity(new Intent(this, DetailActivity.class)
                .putExtra("city", name));
        Animatoo.animateSlideLeft(this);
    }

    // from adapter
    @Override
    public void onLongClick(Weather data, int position) {
        String name = data.getName();
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getResources().getString(R.string.title_dialog_delete, name))
                .setConfirmText(getResources().getString(R.string.title_agree_delete))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog
                                .setTitleText(getResources().getString(R.string.delete_data, name))
                                .setConfirmText("OK")
                                .setConfirmClickListener(null)
                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        mAdapter.removeItem(position);
                        presenter.deleteData(name);
                    }
                })
                .show();

    }

    @Override
    protected void onResume() {
        String scale = sp.getString("key_temperature_units", "1");
        isUnitCelsius = scale.equals("1");
        if (mAdapter != null) {
            mAdapter.setUnitCelsius(isUnitCelsius);
        }
        super.onResume();
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

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof MainAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = data.get(viewHolder.getAdapterPosition()).getName();

            // backup of removed item for undo purpose
            final Weather deletedItem = data.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = showSnackBar(mCoordinatorLayout, myFab, getResources().getString(R.string.delete_data, name));
            snackbar.setAction(getResources().getString(R.string.return_data), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    presenter.deleteData(name);

                }
            });
        }
    }

    public enum TypeStart {
        UPDATE, NO_UPDATE
    }
}

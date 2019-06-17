package com.vadimfedchuk1994gmail.weather.activity.main;


import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vadimfedchuk1994gmail.weather.activity.base.BaseActivity;
import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.SearchCityDialogFragment;
import com.vadimfedchuk1994gmail.weather.adapters.MainAdapter;


import java.util.Arrays;
import java.util.List;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends BaseActivity implements SearchCityDialogFragment.AddCityDialogListener {

    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private RecyclerView.LayoutManager layout;
    private MainPresenter presenter;
    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton myFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypeStart type = (TypeStart) getIntent().getSerializableExtra("type");
        initBars();
        setTitle("");
        MainModel model = new MainModel();
        presenter = new MainPresenter(model);
        presenter.attachView(this);
        presenter.viewIsReady();
        mRecyclerView = findViewById(R.id.recyclerView);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout = new GridLayoutManager(this, 2);
        } else {
            layout = new LinearLayoutManager(this);
        }
        mRecyclerView.setLayoutManager(layout);
        // Initialize a new List of Trees
        List<String> trees = Arrays.asList(
                "Alder",
                "Basswood",
                "Birch",
                "Buckeye",
                "Buckthorn",
                "Catalpa",
                "Cedar",
                "Chestnut",
                "Cypress",
                "Giant Sequoia",
                "Honeylocust"
        );

        mAdapter = new MainAdapter(this, trees);
        mRecyclerView.setAdapter(mAdapter);

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

    private void showDialogFragment() {
        FragmentManager fm = getSupportFragmentManager();
        SearchCityDialogFragment searchCityDialogFragment = SearchCityDialogFragment.newInstance("Some Title");
        searchCityDialogFragment.show(fm, "fragment_edit_name");

    }

    public void showData() {

    }

    @Override
    public void onCompleteAddCity(String city) {
        showSnackBar(mCoordinatorLayout, myFab, city);
    }

    @Override
    public void init() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    public static enum TypeStart {
        UPDATE, NO_UPDATE
    }
}

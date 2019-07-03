package com.vadimfedchuk1994gmail.weather;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vadimfedchuk1994gmail.weather.activity.main.OnEditTextChangedListener;
import com.vadimfedchuk1994gmail.weather.adapters.DialogFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchCityDialogFragment extends DialogFragment
        implements DialogFragmentAdapter.SingleDialogClickListener,
        OnEditTextChangedListener.OnCompleteLoad {

    @BindView(R.id.edit_text_search_city) MaterialEditText searchEditText;
    @BindView(R.id.recycler_view_search_cities) RecyclerView recyclerViewListCities;
    private DialogFragmentAdapter adapter;
    OnEditTextChangedListener mListener;
    private List<String> cities = new ArrayList<>();
    private int selectedItem = 0;
    

    public static SearchCityDialogFragment newInstance(String title) {
        SearchCityDialogFragment frag = new SearchCityDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onResume() {
//        // Store access variables for window and blank point
//        Window window = getDialog().getWindow();
//        Point size = new Point();
//        // Store dimensions of the screen in `size`
//        Display display = window.getWindowManager().getDefaultDisplay();
//        display.getSize(size);
//        // WindowManager.LayoutParams.WRAP_CONTENT
//        window.setLayout((int) (size.x * 0.8),(int) (size.x * 0.9));
//        window.setGravity(Gravity.CENTER);
//        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onCompleteLoad(List<String> list) {
        cities.clear();
        cities.addAll(list);
        adapter.setList(cities);
    }

    public interface AddCityDialogListener {
        void onCompleteAddCity(String city);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        ButterKnife.bind(this, view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        AddCityDialogListener listener = (AddCityDialogListener) getActivity();
        RecyclerView.LayoutManager layout  = new LinearLayoutManager(getActivity());
        recyclerViewListCities.setLayoutManager(layout);

        adapter = new DialogFragmentAdapter(getActivity(), new ArrayList<>());
        adapter.setOnItemClickListener(this);
        recyclerViewListCities.setAdapter(adapter);
        Button buttonAdd = view.findViewById(R.id.button_add_city);
        buttonAdd.setOnClickListener(v ->{
            if (cities.isEmpty()) {
                listener.onCompleteAddCity("");
            } else {
                listener.onCompleteAddCity(cities.get(selectedItem));
            }
            dismiss();
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    String cap = s.toString().substring(0, 1).toUpperCase() + s.toString().substring(1);
                    mListener.onEditTextChanged(cap, (SearchCityDialogFragment.this::onCompleteLoad));
                }
            }
        });
    }

    @Override
    public void onItemClickListener(int position, View view) {
        selectedItem = position;
        hideKeyboard(view);
        adapter.selectedItem();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void setOnEditTextChangedListener(OnEditTextChangedListener listener) {
        mListener = listener;
    }
}

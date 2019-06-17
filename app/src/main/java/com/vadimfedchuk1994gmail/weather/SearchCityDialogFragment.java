package com.vadimfedchuk1994gmail.weather;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vadimfedchuk1994gmail.weather.adapters.DialogFragmentAdapter;
import com.vadimfedchuk1994gmail.weather.adapters.MainAdapter;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchCityDialogFragment extends DialogFragment implements DialogFragmentAdapter.SingleDialogClickListener {

    @BindView(R.id.edit_text_search_city) MaterialEditText searchEditText;
    @BindView(R.id.recycler_view_search_cities) RecyclerView recyclerViewListCities;
    private DialogFragmentAdapter adapter;

    public static SearchCityDialogFragment newInstance(String title) {
        SearchCityDialogFragment frag = new SearchCityDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // WindowManager.LayoutParams.WRAP_CONTENT
        window.setLayout((int) (size.x * 0.8), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    public interface AddCityDialogListener {
        void onCompleteAddCity(String city);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment, container, false);
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
        adapter = new DialogFragmentAdapter(getActivity(), trees);
        adapter.setOnItemClickListener(this);
        recyclerViewListCities.setAdapter(adapter);
        Button buttonAdd = view.findViewById(R.id.button_add_city);
        buttonAdd.setOnClickListener(v ->{
            listener.onCompleteAddCity("Kharkov");
            dismiss();
        });
    }
    @Override
    public void onItemClickListener(int position, View view) {
        hideKeyboard(view);
        adapter.selectedItem();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

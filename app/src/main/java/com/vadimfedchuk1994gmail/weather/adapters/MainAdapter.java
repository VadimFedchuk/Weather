package com.vadimfedchuk1994gmail.weather.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private static final int TYPE_VIEW_CLEAR = 1;
    private ClickListener clicklistener;
    private static final int TYPE_VIEW_PARTIALLY_CLOUDY = 2;
    private static final int TYPE_VIEW_CLOUDY = 3;
    private static final int TYPE_VIEW_RAIN = 4;
    private static final int TYPE_VIEW_THUNDERSTORM = 5;
    private static final int TYPE_VIEW_MIST = 6;
    private static final int TYPE_VIEW_SNOW = 7;
    private List<Weather> mDataSet;


    public MainAdapter(Context context, List<Weather> dataSet) {
        mContext = context;
        mDataSet = dataSet;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resourceId;
        switch (viewType) {
            case TYPE_VIEW_MIST:
                resourceId = R.layout.custom_view;
            case TYPE_VIEW_PARTIALLY_CLOUDY:
                resourceId = R.layout.custom_view;
            case TYPE_VIEW_CLOUDY:
                resourceId = R.layout.custom_view;
            case TYPE_VIEW_RAIN:
                resourceId = R.layout.custom_view;
            case TYPE_VIEW_SNOW:
                resourceId = R.layout.custom_view;
            case TYPE_VIEW_THUNDERSTORM:
                resourceId = R.layout.custom_view;
            default:
                resourceId = R.layout.custom_view;  //CLEAR
        }
        View view = LayoutInflater.from(mContext).inflate(resourceId, parent, false);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        Weather data = mDataSet.get(position);
        holder.bind(data, mContext);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setList(List<Weather> dataSet) {
        this.mDataSet = dataSet;
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mDataSet.get(position).icon) {
            case "c01d":
            case "c01n":
                return TYPE_VIEW_CLEAR;
            case "u00d":
            case "u00n":
            case "s04d":
            case "s04n":
            case "r06d":
            case "r06n":
            case "r05d":
            case "r05n":
            case "r04d":
            case "r04n":
            case "f01d":
            case "f01n":
            case "r03d":
            case "r03n":
            case "r02d":
            case "r02n":
            case "r01d":
            case "d01n":
            case "d01d":
            case "d02n":
            case "d02d":
            case "d03n":
            case "d03d":
            case "r01n":
                return TYPE_VIEW_RAIN;
            case "t01d":
            case "t01n":
            case "t02d":
            case "t02n":
            case "t03d":
            case "t03n":
            case "t04d":
            case "t04n":
            case "t05d":
            case "t05n":
                return TYPE_VIEW_THUNDERSTORM;
            case "s05d":
            case "s05n":
            case "c04d":
            case "c04n":
                return TYPE_VIEW_CLOUDY;
            case "c02d":
            case "c02n":
            case "c03d":
            case "c03n":
                return TYPE_VIEW_PARTIALLY_CLOUDY;
            case "a01d":
            case "a01n":
            case "a02d":
            case "a02n":
            case "a03d":
            case "a03n":
            case "a04d":
            case "a04n":
            case "a05d":
            case "a05n":
            case "a06d":
            case "a06n":
                return TYPE_VIEW_MIST;


            default:
                return TYPE_VIEW_SNOW;
        }

    }

    public interface ClickListener {
        void onClick(String name);

        void onLongClick(Weather data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view_main)
        CardView mCardView;
        @BindView(R.id.cityCountryName)
        TextView mNameTextView;
        @BindView(R.id.icon_current_weather)
        ImageView mIconImageView;
        @BindView(R.id.description_weather)
        TextView mDescriptionTextView;
        @BindView(R.id.current_temperature)
        TextView mTemperatureTextView;
        @BindView(R.id.max_temperature)
        TextView mTempMaxTextView;
        @BindView(R.id.min_temperature)
        TextView mTempMinTextView;
        @BindView(R.id.wind_speed_destination)
        TextView mWindTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        private void bind(Weather data, Context mContext) {

            mNameTextView.setText(data.name);
            // load image
            try {
                // get input stream
                InputStream ims = mContext.getAssets().open("image_" + data.icon + ".png");
                // load image as Drawable
                Drawable d = Drawable.createFromStream(ims, null);
                // set image to ImageView
                mIconImageView.setImageDrawable(d);
            } catch (IOException ex) {
                return;
            }
            mDescriptionTextView.setText(data.description);
            mTemperatureTextView.setText(mContext.getResources().getString(R.string.current_temperature, (int) data.temperature));
            mTempMaxTextView.setText(mContext.getResources().getString(R.string.max_temperature, (int) data.max_temp));
            mTempMinTextView.setText(mContext.getResources().getString(R.string.max_temperature, (int) data.min_temp));
            mWindTextView.setText(mContext.getResources().getString(R.string.wind_speed, data.wind_dir, (int) data.wind_speed));

            mCardView.setOnClickListener(v -> clicklistener.onClick(data.name));
            mCardView.setOnLongClickListener(v -> {
                clicklistener.onLongClick(data);
                return true;
            });
        }
    }
}
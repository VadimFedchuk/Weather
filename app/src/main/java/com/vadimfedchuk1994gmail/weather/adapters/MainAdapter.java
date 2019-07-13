package com.vadimfedchuk1994gmail.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.pojo.TypeWeather;
import com.vadimfedchuk1994gmail.weather.pojo.Weather;
import com.vadimfedchuk1994gmail.weather.tools.PictureHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vadimfedchuk1994gmail.weather.tools.ConverterHelper.convertCelsiusToFahrenheit;
import static com.vadimfedchuk1994gmail.weather.tools.ConverterHelper.convertTimeStampToDate;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private Context mContext;
    private ClickListener clicklistener;
    private List<Weather> mDataSet;
    private boolean isUnitCelsius;

    public MainAdapter(Context context, List<Weather> dataSet, boolean unit) {
        mContext = context;
        mDataSet = dataSet;
        this.isUnitCelsius = unit;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_view, parent, false);
        return new MainAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        Weather data = mDataSet.get(position);
        holder.bind(data, mContext, getItemViewType(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setList(List<Weather> dataSet) {
        mDataSet.clear();
        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener listener) {
        this.clicklistener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return PictureHelper.choosePicture(mDataSet.get(position).getIcon());
    }

    public void removeItem(int position) {
        mDataSet.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Weather item, int position) {
        mDataSet.add(position, item);
        notifyItemInserted(position);
    }

    public void setUnitCelsius(boolean unitCelsius) {
        isUnitCelsius = unitCelsius;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(String name);

        void onLongClick(Weather data, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_cityName)
        TextView mNameTextView;
        @BindView(R.id.image_weather_icon)
        ImageView mIconImageView;
        @BindView(R.id.text_description_weather)
        TextView mDescriptionTextView;
        @BindView(R.id.text_current_temperature)
        TextView mCurrentTemperatureTextView;
        @BindView(R.id.text_max_min_temperature)
        TextView mMaxMinTemperatureTextView;
        @BindView(R.id.view_foreground)
        public ConstraintLayout viewForeground;
        @BindView(R.id.text_precipitation)
        TextView mPrecipitationTextView;
        @BindView(R.id.text_sunrise)
        TextView mSunriseTextView;
        @BindView(R.id.text_sunset)
        TextView mSunsetTextView;
        @BindView(R.id.text_wind)
        TextView mWindTextView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> clicklistener.onClick(mDataSet.get(getAdapterPosition()).getName()));
            itemView.setOnLongClickListener(v -> {
                clicklistener.onLongClick(mDataSet.get(getAdapterPosition()), getAdapterPosition());
                return true;
            });
        }

        private void bind(Weather data, Context mContext, int viewType) {
            TypeWeather obj = PictureHelper.generateObject(mContext, viewType);

            mNameTextView.setText(data.getName());
            mIconImageView.setImageResource(obj.getResourceIdIcon());
            mDescriptionTextView.setText(obj.getDescription());
            mCurrentTemperatureTextView.setText(isUnitCelsius ?
                    mContext.getResources().getString(R.string.current_temperature_celsius, data.getTemperature()) :
                    mContext.getResources().getString(R.string.current_temperature_fahrenheit, convertCelsiusToFahrenheit(data.getTemperature())));
            mMaxMinTemperatureTextView.setText(isUnitCelsius ?
                    mContext.getResources().getString(R.string.max_min_temperature_celsius, data.getMax_temp(), data.getMin_temp()) :
                    mContext.getResources().getString(R.string.max_min_temperature_fahrenheit, convertCelsiusToFahrenheit(data.getMax_temp()), convertCelsiusToFahrenheit(data.getMin_temp())));
            mPrecipitationTextView.setText(mContext.getResources().getString(R.string.precipitation, data.getPop()));
            mSunriseTextView.setText(convertTimeStampToDate(data.getSunrise(), mContext));
            mSunsetTextView.setText(convertTimeStampToDate(data.getSunset(), mContext));
            mWindTextView.setText(mContext.getResources().getString(R.string.wind_condition, data.getWind_dir(), data.getWind_speed()));
        }
    }
}
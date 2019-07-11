package com.vadimfedchuk1994gmail.weather.adapters;

import android.content.Context;
import android.content.res.Resources;
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

import androidx.viewpager.widget.PagerAdapter;

import static com.vadimfedchuk1994gmail.weather.tools.ConverterHelper.convertCelsiusToFahrenheit;
import static com.vadimfedchuk1994gmail.weather.tools.ConverterHelper.convertMbToMm;
import static com.vadimfedchuk1994gmail.weather.tools.ConverterHelper.convertTimeStampToDate;
import static com.vadimfedchuk1994gmail.weather.tools.ConverterHelper.convertToDate;

public class CardPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    private List<Weather> mData;
    private Context mContext;
    private boolean isUnitCelsius = true;
    private TextView maxminTempTextView;
    private TextView temperatureTextView;

    public CardPagerAdapter(Context context, List<Weather> data, boolean unit) {
        mData = data;
        mContext = context;
        isUnitCelsius = unit;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_detail, container, false);
        container.addView(view);
        bind(mData.get(position), view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    private void bind(Weather item, View view) {
        TypeWeather obj = PictureHelper.generateObject(mContext, PictureHelper.choosePicture(item.getIcon()));
        temperatureTextView = view.findViewById(R.id.text_temperature);
        maxminTempTextView = view.findViewById(R.id.text_max_min_temperature_detail);

        TextView dateTextView = view.findViewById(R.id.text_date);
        ImageView iconView = view.findViewById(R.id.icon_image);
        TextView descriptionTextView = view.findViewById(R.id.text_description);
        TextView text_wind_directionTextView = view.findViewById(R.id.text_wind_direction);
        TextView precipitationTextView = view.findViewById(R.id.text_precipitation);
        TextView pressureTextView = view.findViewById(R.id.text_pressure);
        TextView sunriseTextView = view.findViewById(R.id.text_sunrise);
        TextView sunsetTextView = view.findViewById(R.id.text_sunset);

        Resources resources = mContext.getResources();

        dateTextView.setText(item.getDate());
        convertToDate(item.getDate());
        iconView.setImageResource(obj.getResourceIdIcon());
        temperatureTextView.setText(isUnitCelsius ?
                mContext.getResources().getString(R.string.current_temperature, (int) item.getTemperature()) :
                convertCelsiusToFahrenheit(item.getTemperature()));
        maxminTempTextView.setText(isUnitCelsius ?
                mContext.getResources().getString(R.string.max_min_temperature_2, (int) item.getMax_temp() + "\u00B0c", (int) item.getMin_temp() + "\u00B0c") :
                mContext.getResources().getString(R.string.max_min_temperature_2, convertCelsiusToFahrenheit(item.getMax_temp()), convertCelsiusToFahrenheit(item.getMin_temp())));
        descriptionTextView.setText(obj.getDescription());
        text_wind_directionTextView.setText(resources.getString(R.string.wind_speed_direction,
                item.getWind_dir(), (int) item.getWind_speed()));
        precipitationTextView.setText(mContext.getResources().getString(R.string.precipitation_detail, (int) (item.getPop())));
        pressureTextView.setText(mContext.getResources().getString(R.string.pressure_detail, convertMbToMm(item.getPres())));

        sunriseTextView.setText(convertTimeStampToDate(item.getSunrise()));
        sunsetTextView.setText(convertTimeStampToDate(item.getSunset()));
    }
}

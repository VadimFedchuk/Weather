package com.vadimfedchuk1994gmail.weather.tools;

import android.content.Context;

import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.pojo.TypeWeather;

public class PictureHelper {

    private PictureHelper() {
    }

    public static int choosePicture(String nameIcon) {
        switch (nameIcon) {
            case "c01d":
            case "c01n":
                return Const.ImageConst.TYPE_VIEW_CLEAR;
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
                return Const.ImageConst.TYPE_VIEW_RAIN;
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
                return Const.ImageConst.TYPE_VIEW_THUNDERSTORM;
            case "s05d":
            case "s05n":
            case "c04d":
            case "c04n":
                return Const.ImageConst.TYPE_VIEW_CLOUDY;
            case "c02d":
            case "c02n":
            case "c03d":
            case "c03n":
                return Const.ImageConst.TYPE_VIEW_PARTIALLY_CLOUDY;
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
                return Const.ImageConst.TYPE_VIEW_MIST;
            default:
                return Const.ImageConst.TYPE_VIEW_SNOW;
        }
    }

    public static TypeWeather generateObject(Context mContext, int type) {
        TypeWeather obj;
        String description;
        int resourceIdIcon = 0;
        switch (type) {
            case Const.ImageConst.TYPE_VIEW_CLEAR:
                description = mContext.getString(R.string.clear_weather);
                resourceIdIcon = R.drawable.clear_weather_icon;
                break;
            case Const.ImageConst.TYPE_VIEW_PARTIALLY_CLOUDY:
                description = mContext.getString(R.string.partially_cloudy_weather);
                resourceIdIcon = R.drawable.partially_cloudy_weather_icon;
                break;
            case Const.ImageConst.TYPE_VIEW_CLOUDY:
                description = mContext.getString(R.string.cloudy_weather);
                resourceIdIcon = R.drawable.cloudy_weather_icon;
                break;
            case Const.ImageConst.TYPE_VIEW_RAIN:
                description = mContext.getString(R.string.rain_weather);
                resourceIdIcon = R.drawable.rain_weather_icon;
                break;
            case Const.ImageConst.TYPE_VIEW_SNOW:
                description = mContext.getString(R.string.snow_weather);
                resourceIdIcon = R.drawable.snow_weather_icon;
                break;
            case Const.ImageConst.TYPE_VIEW_THUNDERSTORM:
                description = mContext.getString(R.string.thunderstorm_weather);
                resourceIdIcon = R.drawable.thundersthorm_weather_icon;
                break;
            default:
                description = mContext.getString(R.string.mist_weather);
                resourceIdIcon = R.drawable.foggy_weather_icon;
                break;
        }
        obj = new TypeWeather(description, resourceIdIcon);
        return obj;
    }
}

package com.vadimfedchuk1994gmail.weather.network;

import android.content.Context;

import com.vadimfedchuk1994gmail.weather.BuildConfig;
import com.vadimfedchuk1994gmail.weather.R;
import com.vadimfedchuk1994gmail.weather.network.cities_response.CitiesResponse;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;
import com.vadimfedchuk1994gmail.weather.tools.WeatherPreferences;

import java.util.List;

import io.reactivex.Single;

public class WeatherDataSource {
    private final String API_KEY = BuildConfig.ApiKey;
    private Context mContext;
    private ApiService mApiService;
    private String language;

    public WeatherDataSource(Context context) {
        mContext = context;
        mApiService = ApiClient.getClient(mContext)
                .create(ApiService.class);
        language = WeatherPreferences.getStoredLanguage(mContext);
    }

    public Single<WeatherResponse> getWeatherData(String city) {
        return mApiService.getDataWeather(city, API_KEY, language);
    }

    public Single<List<CitiesResponse>> getCities(String query) {
        return mApiService.getCities(mContext.getString(R.string.base_url_upload_cities_list), query);
    }
}

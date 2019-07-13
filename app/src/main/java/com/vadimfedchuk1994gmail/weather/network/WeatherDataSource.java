package com.vadimfedchuk1994gmail.weather.network;

import android.content.Context;

import com.vadimfedchuk1994gmail.weather.network.cities_response.CityResponse;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;
import com.vadimfedchuk1994gmail.weather.tools.WeatherPreferences;

import io.reactivex.Single;

import static com.vadimfedchuk1994gmail.weather.tools.Const.API_KEY;
import static com.vadimfedchuk1994gmail.weather.tools.Const.BASE_URL;

public class WeatherDataSource {

    private ApiService mApiService;
    private String language;

    public WeatherDataSource(Context context) {
        mApiService = ApiClient.getClient(context)
                .create(ApiService.class);
        language = WeatherPreferences.getStoredLanguage(context);
    }

    public Single<WeatherResponse> getWeatherData(String city) {
        return mApiService.getDataWeather(city, API_KEY, language);
    }

    public Single<CityResponse> getCities(String query) {
        return mApiService.getCities(BASE_URL, query, language);
    }
}

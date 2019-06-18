package com.vadimfedchuk1994gmail.weather.network;

import com.vadimfedchuk1994gmail.weather.network.cities_response.CitiesResponse;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @GET("forecast/daily")
    Single<WeatherResponse> getDataWeather(@Query("city") String cityName,
                                           @Query("key") String api_key,
                                           @Query("lang") String language);

    @GET("aq")
    Single<List<CitiesResponse>> getCities(@Url String url,
                                           @Query("query") String query);
}

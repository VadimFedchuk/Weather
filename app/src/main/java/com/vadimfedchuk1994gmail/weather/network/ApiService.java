package com.vadimfedchuk1994gmail.weather.network;

import com.vadimfedchuk1994gmail.weather.network.cities_response.CityResponse;
import com.vadimfedchuk1994gmail.weather.network.weather_response.WeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {

    @GET("forecast/daily")
    Single<WeatherResponse> getDataWeather(@Query("city") String cityName,
                                           @Query("key") String api_key,
                                           @Query("lang") String language);

    @GET
    Single<CityResponse> getCities(@Url String url,
                                   @Query("query") String query,
                                   @Query("lang") String lang);
}

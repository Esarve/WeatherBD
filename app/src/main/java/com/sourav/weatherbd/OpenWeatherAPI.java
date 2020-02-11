package com.sourav.weatherbd;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherAPI {
    //@GET("data/2.5/weather")
    //Call<Data> getWeatherData(@QueryMap Map<String[],String> params);
    @GET("data/2.5/weather")
    Call<Data> getWeatherData(@Query("q") String[] params,
                              @Query("appid") String api,
                              @Query("units") String unit);
}

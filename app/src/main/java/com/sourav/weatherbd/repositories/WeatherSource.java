package com.sourav.weatherbd.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.sourav.weatherbd.Interfaces.OpenWeatherAPI;
import com.sourav.weatherbd.Models.Structures.WeatherObjectForJson;

import org.jetbrains.annotations.NotNull;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherSource {

    private static final String TAG = "WeatherSource CLASS";
    private MutableLiveData<WeatherObjectForJson> received;
    private final String api = "6ef31b54f38ce5a3e5496e7ae5c7654f";
    private static WeatherSource instance;

    //Singleton
    public static WeatherSource getInstance(){
        if (instance == null)
            instance = new WeatherSource();
        return instance;
    }

    //Get weather form retrofit
    public void fetchWeather(String location, String unit){
        Log.d(TAG, "fetchWeather: Called");
        Log.d(TAG, "fetchWeather: Received Location: "+location);
        Log.d(TAG, "fetchWeather: Received Units: "+unit);

        //Initialization
        RealmCache realmCache = new RealmCache();
        realmCache.initReal();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        if (received == null){
            received = new MutableLiveData<>();
        }

        OpenWeatherAPI openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
        Log.d(TAG, "fetchWeather: Initialization finished");

        //END

        Call<WeatherObjectForJson> call = openWeatherAPI.getWeatherData(location,api,unit);

        // START CALLBACK
        call.enqueue(new Callback<WeatherObjectForJson>() {
            @Override
            public void onResponse(@NotNull Call<WeatherObjectForJson> call, @NotNull Response<WeatherObjectForJson> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: Response Successful");
                    Log.d(TAG, "onResponse: Status "+ response.code());

                    if (response.body()!= null){
                        received.setValue(response.body());
                        realmCache.cacheToRealm(response.body());
                        Log.d(TAG, "onResponse: Data Received!");
                    }else
                        Log.d(TAG, "onResponse: NULL data received");
                }
            }
            @Override
            public void onFailure(@NotNull Call<WeatherObjectForJson> call, @NotNull Throwable t) {
                Log.d(TAG, "onFailure: FAILED TO GET DATA");
                Log.e(TAG, "onFailure: ",t );
                received.setValue(realmCache.getData());
            }
        });

    }

    public MutableLiveData<WeatherObjectForJson> getReceived() {
        return received;
    }
}

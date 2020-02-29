package com.sourav.weatherbd.Models;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sourav.weatherbd.Interfaces.OpenWeatherAPI;
import com.sourav.weatherbd.Models.Structures.Data;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherSource {

    private static final String TAG = "WeatherSource CLASS";
    private MutableLiveData<Data> recived;
    private final String api = "6ef31b54f38ce5a3e5496e7ae5c7654f";
    private static WeatherSource instance;

    public static WeatherSource getInstance(){
        if (instance == null)
            instance = new WeatherSource();
        return instance;
    }


    public void fetchWeather(String location, String unit){
        Log.d(TAG, "fetchWeather: Called");
        Log.d(TAG, "fetchWeather: Received Location: "+location);
        Log.d(TAG, "fetchWeather: Received Units: "+unit);
        //Initialization
        //Gson gson = new Gson();
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
        if (recived== null){
            recived = new MutableLiveData<>();
        }

        OpenWeatherAPI openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
        Log.d(TAG, "fetchWeather: Initialization finished");

        //END

        Call<Data> call = openWeatherAPI.getWeatherData(location,api,unit);

        // START CALLBACK
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: Response Successful");
                    Log.d(TAG, "onResponse: Status "+ response.code());

                    if (response.body()!= null){
                        recived.setValue(response.body());
                        Log.d(TAG, "onResponse: Data Received!");
                    }else
                        Log.d(TAG, "onResponse: NULL data received");
                        //String jsonWD = gson.toJson(recived);

                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });

    }

    public MutableLiveData<Data> getRecived() {
        return recived;
    }
}

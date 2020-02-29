package com.sourav.weatherbd;

import androidx.lifecycle.MutableLiveData;

import com.sourav.weatherbd.Models.Structures.Data;
import com.sourav.weatherbd.Models.WeatherSource;

public class WeatherRepositoryAPI {

    WeatherSource weatherSource;
    MutableLiveData<Data> mLiveData;

    public MutableLiveData<Data> getData(String location, String api, String unit){
        if (mLiveData == null){
            mLiveData = new MutableLiveData<>();
            fetchWeather(location,api,unit);
        }
        return mLiveData;
    }
    private void fetchWeather(String location, String api, String unit){
        weatherSource = new WeatherSource();
        weatherSource.fetchWeather(location,unit);
    }
}

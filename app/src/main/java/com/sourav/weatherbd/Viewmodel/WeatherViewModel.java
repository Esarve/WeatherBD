package com.sourav.weatherbd.Viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.sourav.weatherbd.Models.Structures.WeatherObjectForJson;
import com.sourav.weatherbd.repositories.WeatherSource;

public class WeatherViewModel extends AndroidViewModel {
    private static final String TAG = "ViewModel";
    private String location, unit;
    private WeatherSource weatherSource;
    private SharedPreferences sharedPreferences;
    private MutableLiveData<WeatherObjectForJson> mLiveData;

    public WeatherViewModel(Application application) {
        super(application);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        getSettings();
        weatherSource = WeatherSource.getInstance();
    }

    public MutableLiveData<WeatherObjectForJson> getDataFromServer(){
        Log.d(TAG, "getDataFromServer: Called");
        if (mLiveData == null){
            mLiveData = new MutableLiveData<>();
            fetchWeather();
        }
        return mLiveData;
    }
    public void fetchWeather(){
        Log.d(TAG, "fetchWeather: RUN");
        getSettings();
        weatherSource.fetchWeather(location,unit);
        mLiveData = weatherSource.getReceived();
    }

    // Fetches settings from shared preference
    private void getSettings() {
        String city;
        city = sharedPreferences.getString("loc","dhaka");
        unit = sharedPreferences.getString("unit","metric");
        location = city+","+"BD";
        Log.d(TAG, "getSettings: Loaded City: "+city);
        Log.d(TAG, "getSettings: Loaded Unit: "+unit);
    }


    public String getUnit() {
        return unit;
    }
}

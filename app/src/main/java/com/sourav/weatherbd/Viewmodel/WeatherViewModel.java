package com.sourav.weatherbd.Viewmodel;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.preference.PreferenceManager;

import com.sourav.weatherbd.Models.Structures.Data;
import com.sourav.weatherbd.Models.Structures.SimplifiedWeatherModel;
import com.sourav.weatherbd.Models.WeatherSource;
import com.sourav.weatherbd.repositories.WeatherRepositoryDB;

public class WeatherViewModel extends AndroidViewModel {
    private static final String TAG = "ViewModel";
    private WeatherRepositoryDB weatherRepositoryDB;
    private LiveData<SimplifiedWeatherModel> weatherdata;
    private String location, unit;
    private WeatherSource weatherSource;
    private SharedPreferences sharedPreferences;
    private MutableLiveData<Data> mLiveData;

    public WeatherViewModel(Application application) {
        super(application);
        weatherRepositoryDB = new WeatherRepositoryDB(application);
        weatherdata = weatherRepositoryDB.getWeather();
        if(sharedPreferences == null){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
        }
        getSettings();
        weatherSource = WeatherSource.getInstance();
    }

    public MutableLiveData<Data> getDataFromServer(){
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

    public void insert(SimplifiedWeatherModel weatherModel){
        weatherRepositoryDB.insert(weatherModel);
    }

    public void delete(SimplifiedWeatherModel weatherModel){
        weatherRepositoryDB.delete(weatherModel);
    }

    public LiveData<SimplifiedWeatherModel> getWeatherdata(){
        return weatherdata;
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

    // Setters
    public void setLocation(String location) {
        this.location = location;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}

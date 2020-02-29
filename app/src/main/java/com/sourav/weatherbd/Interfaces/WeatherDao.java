package com.sourav.weatherbd.Interfaces;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.sourav.weatherbd.Models.Structures.SimplifiedWeatherModel;

@Dao
public interface WeatherDao {

    @Insert
    void insert(SimplifiedWeatherModel weatherModel);

    @Delete
    void delete(SimplifiedWeatherModel weatherModel);

    @Query("SELECT * FROM weather ORDER BY id DESC LIMIT 1")
    LiveData<SimplifiedWeatherModel> getWeather();
}

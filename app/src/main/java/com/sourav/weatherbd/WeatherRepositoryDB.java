package com.sourav.weatherbd;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.sourav.weatherbd.Interfaces.WeatherDao;
import com.sourav.weatherbd.Models.Structures.SimplifiedWeatherModel;
import com.sourav.weatherbd.Models.Structures.Weather;
import com.sourav.weatherbd.Models.WeatherDB;

public class WeatherRepositoryDB {
    private WeatherDao weatherDao;
    private LiveData<SimplifiedWeatherModel> weatherdata;

    public WeatherRepositoryDB(Application application){
        WeatherDB weatherDB = WeatherDB.getInstance(application);
        weatherDao = weatherDB.weatherDao();
        weatherdata = weatherDao.getWeather();
    }

    public void insert (SimplifiedWeatherModel weatherModel){
        new OperationAsyncTask(1,weatherDao).execute(weatherModel);
    }

    public void delete (SimplifiedWeatherModel weatherModel){
        new OperationAsyncTask(2,weatherDao).execute(weatherModel);
    }

    public LiveData<SimplifiedWeatherModel> getWeather (){
        return weatherdata;
    }

    public static class OperationAsyncTask extends AsyncTask<SimplifiedWeatherModel, Void, Void>{
        private int key;
        private WeatherDao weatherDao;

        public OperationAsyncTask(int key, WeatherDao weatherDao) {
            this.key = key;
            this.weatherDao = weatherDao;
        }

        @Override
        protected Void doInBackground(SimplifiedWeatherModel... simplifiedWeatherModels) {
            switch (key){
                case 1:
                    weatherDao.insert(simplifiedWeatherModels[0]);
                    break;
                case 2:
                    weatherDao.delete(simplifiedWeatherModels[0]);
                    break;

            }
            return null;
        }
    }
}

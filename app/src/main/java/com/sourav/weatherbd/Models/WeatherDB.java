package com.sourav.weatherbd.Models;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sourav.weatherbd.Interfaces.WeatherDao;
import com.sourav.weatherbd.Models.Structures.SimplifiedWeatherModel;

@Database(entities = SimplifiedWeatherModel.class, version = 1)
public abstract class WeatherDB extends RoomDatabase {

    private static WeatherDB instance;

    public abstract WeatherDao weatherDao();

    public static synchronized WeatherDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    WeatherDB.class, "weather_db")
                    .addCallback(roomCallBack)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };



}

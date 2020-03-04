package com.sourav.weatherbd.Handlers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsManager {
    private boolean isDark;
    private SharedPreferences sharedPreferences;

    public SettingsManager(Context context) {
        if(sharedPreferences == null){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public boolean getDarkMode(){
        return sharedPreferences.getBoolean("darkmode",false);
    }


}

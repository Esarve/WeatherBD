package com.sourav.weatherbd.Views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.sourav.weatherbd.Handlers.SettingsManager;
import com.sourav.weatherbd.Handlers.StatusNavBarColorHandler;
import com.sourav.weatherbd.Models.Structures.WeatherObjectForJson;
import com.sourav.weatherbd.Models.Structures.Weather;
import com.sourav.weatherbd.R;
import com.sourav.weatherbd.Viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather Log";
    private LiveData<WeatherObjectForJson> weatherLiveData;
    private WeatherViewModel weatherViewModel;
    private TextView location;
    private TextView temp;
    private TextView humid;
    private TextView pressure;
    private TextView condition;
    private TextView visibility;
    private TextView windSpeed;
    private ImageView settingsIcon;
    private StatusNavBarColorHandler statusNavBarColorHandler;
    private CoordinatorLayout coordinatorLayout;
    private SettingsManager settingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsManager = new SettingsManager(this);
        initializeViews();
        setThemeConfigs();
        weatherViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(this.getApplication()))
                .get(WeatherViewModel.class);
        weatherLiveData = weatherViewModel.getDataFromServer();
        weatherLiveData.observe(this, weatherObjectForJson -> {
            Log.d(TAG, "onChanged: RUN");
            populate(weatherObjectForJson);
        });
    }

    // Set Dark Mode & Other color stuffs
    private void setThemeConfigs() {
        boolean darkMode = settingsManager.getDarkMode();

        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            settingsIcon.setImageResource(R.drawable.ic_settings_white_24dp);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            settingsIcon.setImageResource(R.drawable.ic_settings_black_24dp);
            statusNavBarColorHandler = StatusNavBarColorHandler.getInstance();
            statusNavBarColorHandler.setLightStatusNavBar(getWindow().getDecorView(), this);
        }

    }

    // Populate Views
    private void populate(WeatherObjectForJson weatherObjectForJson) {
        if (weatherObjectForJson != null) {
            String unit = weatherViewModel.getUnit();
            String degreeUnit = "C";
            String unitSpeed = "Km/h";
            String unitVisi = "Meters";
            String pressureUnit = "Pa";
            String humidUnit = "%";
            if (unit.equals("imperial")) {
                degreeUnit = "F";
                pressureUnit = "PSI";
            }
            String tempValue = String.valueOf((int) weatherObjectForJson.getMain().getTemp()) + (char) 0x00B0 + degreeUnit;
            String visibilityVal = weatherObjectForJson.getVisibility() + unitVisi;
            String windSpeedVal =weatherObjectForJson.getWind().getSpeed() + unitSpeed;
            String humidValue = (weatherObjectForJson.getMain().getHumidity()) + " " + humidUnit;
            String pressureValue = (weatherObjectForJson.getMain().getPressure()) + " " + pressureUnit;


            String loc = weatherObjectForJson.getName();
            Weather weii = weatherObjectForJson.getWeather().get(0);
            String cond = weii.getMain();

            Log.d(TAG, "populate: Temp: " + tempValue);
            Log.d(TAG, "populate: Humid: " + humidValue);
            Log.d(TAG, "populate: pressure: " + pressureValue);
            Log.d(TAG, "populate: location: " + loc);
            Log.d(TAG, "populate: Condition: " + cond);

            try {
                location.setText(loc);
                temp.setText(tempValue);
                humid.setText(humidValue);
                pressure.setText(pressureValue);
                location.setText(loc);
                condition.setText(cond);
                visibility.setText(visibilityVal);
                windSpeed.setText(windSpeedVal);

            } catch (NullPointerException e) {
                Toast.makeText(this, "Received NULL OBJECT", Toast.LENGTH_LONG).show();
            }
            showSnackBar("Loaded Weather Successfully for "+ loc);
        } else {
            Log.d(TAG, "populate: Data STILL NULL");
        }

    }

    private void initializeViews() {
        location = findViewById(R.id.tvLocation);
        temp = findViewById(R.id.tvTemp);
        humid = findViewById(R.id.tvHumid);
        pressure = findViewById(R.id.tvPress);
        condition = findViewById(R.id.tvCondition);
        visibility = findViewById(R.id.tvVisibility);
        windSpeed = findViewById(R.id.tvWindSpeed);
        coordinatorLayout = findViewById(R.id.parentLayout);
        settingsIcon = findViewById(R.id.settingsIcon);
    }

    //Extended Floating Action Button Click method
    public void onclickxfab(View view) {
        //currentConfig();
        weatherViewModel.fetchWeather();
    }

    public void openSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void showSnackBar(String message){
        Snackbar snackbar = Snackbar.make(
                coordinatorLayout, message, BaseTransientBottomBar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setThemeConfigs();
    }
}

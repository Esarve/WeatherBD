package com.sourav.weatherbd.Views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.google.android.material.snackbar.Snackbar;
import com.sourav.weatherbd.Handlers.SettingsManager;
import com.sourav.weatherbd.Handlers.StatusNavBarColorHandler;
import com.sourav.weatherbd.Models.Structures.Weather;
import com.sourav.weatherbd.Models.Structures.WeatherObjectForJson;
import com.sourav.weatherbd.R;
import com.sourav.weatherbd.Viewmodel.WeatherViewModel;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather Log";
    private static final int DARK = 1;
    private static final int LIGHT = 2;
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

        Realm.init(this);

        settingsManager = new SettingsManager(this);

        statusNavBarColorHandler = StatusNavBarColorHandler.getInstance();
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
            statusNavBarColorHandler.changeNavBarColor(getWindow().getDecorView(), this, R.color.navDark,false);
            settingsIcon.setImageResource(R.drawable.ic_settings_white_24dp);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            statusNavBarColorHandler.changeStatusBarColor(getWindow().getDecorView(), this, R.color.grey_3);
            statusNavBarColorHandler.changeNavBarColor(getWindow().getDecorView(), this, R.color.grey_3,true);
        }

    }

    // Populate Views
    private void populate(WeatherObjectForJson weatherObjectForJson) {
        if (weatherObjectForJson != null) {
            String unit = weatherViewModel.getUnit();
            String degreeUnit = "C";
            String unitSpeed = " Km/h";
            String unitVisi = " Meters";
            String pressureUnit = " Pa";
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

            if (isConnected())
                showSnackBar("Loaded Weather Successfully for " + loc, -1);
            else
                showSnackBar("No Connection, Using previous cached data for " + loc, 0);
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

    // Open settings activity from image view button click
    public void openSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    // Snackbar stuffs
    private void showSnackBar(String message, int duration) {
        Snackbar snackbar = Snackbar.make(
                coordinatorLayout, message, duration);
        snackbar.show();
    }

    @SuppressWarnings("deprecation")
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setThemeConfigs();
    }
}

package com.sourav.weatherbd.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.sourav.weatherbd.Models.Structures.Data;
import com.sourav.weatherbd.Models.Structures.Weather;
import com.sourav.weatherbd.R;
import com.sourav.weatherbd.Viewmodel.WeatherViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather Log";
    private LiveData<Data> weatherLiveData;
    private WeatherViewModel weatherViewModel;
    private TextView location;
    private TextView temp;
    private TextView humid;
    private TextView pressure;
    private TextView condition;
    private TextView max;
    private TextView min;
    private StatusNavBarColorHandler statusNavBarColorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusNavBarColorHandler = StatusNavBarColorHandler.getInstance();
        statusNavBarColorHandler.setLightStatusNavBar(getWindow().getDecorView(),this);;
        initializeViews();
        weatherViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(this.getApplication()))
                .get(WeatherViewModel.class);
        weatherLiveData = weatherViewModel.getDataFromServer();
        weatherLiveData.observe(this, data -> {
            Log.d(TAG, "onChanged: RUN");
            populate(data);
        });
        //getWeather(location);
    }

    private void populate(Data data) {
        if (data != null) {
            String unit = weatherViewModel.getUnit();
            String degreeUnit = "C";
            String pressureUnit = "Pa";
            String humidUnit = "%";
            if (unit.equals("imperial")) {
                degreeUnit = "F";
                pressureUnit = "PSI";
            }
            String tempValue = String.valueOf((int) data.getMain().getTemp()) + (char) 0x00B0 + degreeUnit;
            String maxTempValue = String.valueOf((int) data.getMain().getMax()) + (char) 0x00B0 + degreeUnit;
            String minTempValue = String.valueOf((int) data.getMain().getMin()) + (char) 0x00B0 + degreeUnit;
            String humidValue = (data.getMain().getHumidity()) + " " + humidUnit;
            String pressureValue = (data.getMain().getPressure()) + " " + pressureUnit;


            String loc = data.getName();
            Weather weii = data.getWeather().get(0);
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
                max.setText(maxTempValue);
                min.setText(minTempValue);

            } catch (NullPointerException e) {
                Toast.makeText(this, "Received NULL OBJECT", Toast.LENGTH_LONG).show();
            }
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
        max = findViewById(R.id.tvMax);
        min = findViewById(R.id.tvMin);
    }

    public void setLightStatusNavBar(View view, Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.grey_3));

            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.grey_3));
        }
    }

    //Extended Floating Action Button Click method
    public void onclickxfab(View view) {
        //currentConfig();
        weatherViewModel.fetchWeather();
    }

    public void openSettingsActivity(View view) {
        startActivity(new Intent(this, SettingsActivity.class));
    }
    // Menu Bullshit Ends
}

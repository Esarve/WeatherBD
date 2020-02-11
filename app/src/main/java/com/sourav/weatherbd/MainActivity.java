package com.sourav.weatherbd;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Weather Data" ;
    private TextView location;
    private TextView temp;
    private TextView humid;
    private TextView pressure;
    private TextView condition;
    private TextView status;
    protected Data receivedData;
    private OpenWeatherAPI openWeatherAPI;
    private final String city = "Dhaka";
    private final String countryCode = "bd";
    private final String api = "6ef31b54f38ce5a3e5496e7ae5c7654f";
    private final String unit = "metric";
    private final String filename = "weatherData";
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        location = findViewById(R.id.tvLocation);
        temp = findViewById(R.id.tvTemp);
        humid = findViewById(R.id.tvHumid);
        pressure = findViewById(R.id.tvPress);
        condition = findViewById(R.id.tvCondition);
        status = findViewById(R.id.status);
        //String location = city + "," + countryCode;
        //data = parseJson();

        gson = new Gson();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        //Map <String[], String> params = new HashMap<>();
        String[] location = {city,countryCode};
        //params.put("q", location);
        //params.put("appid", "6ef31b54f38ce5a3e5496e7ae5c7654f");
        openWeatherAPI = retrofit.create(OpenWeatherAPI.class);
        getWeather(location);
    }

    private void getWeather(String[] location) {
        Call<Data> call = openWeatherAPI.getWeatherData(location,api,unit);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    Log.d(TAG, "onResponse: Response Successful");
                    status.setText("Received Code: ");
                    status.append(String.valueOf(response.code()));
                    Log.d(TAG, "onResponse: Status "+ response.code());

                    if (response.body() != null) {
                        receivedData = response.body();
                        Log.d(TAG, "onResponse: Data Received!");
                    }else{
                        status.setText("");
                        status.setText("Received NULL DATA");
                        Log.d(TAG, "onResponse: NULL data Received");
                     }

                    String json = gson.toJson(receivedData);
                    wirteJson(json);
                    populate(receivedData);
                }

            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                status.setText("");
                status.setText("Enable to connect. Using Last saved weather Data");
                String prevData = null;
                try {
                    prevData = readJson();
                }catch (IOException e){
                    Log.d(TAG, "onFailure: Failed TO Read Data from storage");
                    e.printStackTrace();
                }
                if (prevData != null)
                    receivedData = gson.fromJson(prevData,Data.class);
                else
                    Log.d(TAG, "onFailure: Failed. Can not Populate");

                populate(receivedData);
                Log.d(TAG, "onFailure: " +t.getMessage());
            }
        });
        Log.d(TAG, "getWeather: ENDED");
    }

    private void wirteJson(String json) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readJson() throws IOException {
        FileInputStream inputStream = getApplicationContext().openFileInput(filename);
        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine())!= null){
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }


    private void populate(Data data) {
        double valTemp = data.getMain().getTemp();
        double valhumid = data.getMain().getHumidity();
        double valpressure = data.getMain().getPressure();
        String loc = data.getName();
        Weather weii = data.getWeather().get(0);
        String cond = weii.getMain();

        Log.d(TAG, "populate: Temp: "+valTemp);
        Log.d(TAG, "populate: Humid: "+valhumid);
        Log.d(TAG, "populate: pressure: "+valpressure);
        Log.d(TAG, "populate: location: "+loc);
        Log.d(TAG, "populate: Condition: "+cond);

        location.setText(loc);
        temp.setText(String.valueOf(valTemp));
        humid.setText(String.valueOf(valhumid));
        pressure.setText(String.valueOf(valpressure));
        location.setText(loc);
        condition.setText(cond);

    }

    private Data parseJson(){
        Gson gson = new Gson();
        String json = "{\"coord\":{\"lon\":-122.08,\"lat\":37.39},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"base\":\"stations\",\"main\":{\"temp\":282.55,\"feels_like\":281.86,\"temp_min\":280.37,\"temp_max\":284.26,\"pressure\":1023,\"humidity\":100},\"visibility\":16093,\"wind\":{\"speed\":1.5,\"deg\":350},\"clouds\":{\"all\":1},\"dt\":1560350645,\"sys\":{\"type\":1,\"id\":5122,\"message\":0.0139,\"country\":\"US\",\"sunrise\":1560343627,\"sunset\":1560396563},\"timezone\":-25200,\"id\":420006353,\"name\":\"Mountain View\",\"cod\":200}";
        return gson.fromJson(json, Data.class);
    }
}

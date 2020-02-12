package com.sourav.weatherbd;

import com.google.gson.annotations.SerializedName;

public class Main {
    double temp;
    double humidity;
    double pressure;
    @SerializedName("temp_max")
    double max;
    @SerializedName("temp_min")
    double min;

    public Main(double temp, double humidity, double pressure, double max, double min) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.max = max;
        this.min = min;
    }

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }
}

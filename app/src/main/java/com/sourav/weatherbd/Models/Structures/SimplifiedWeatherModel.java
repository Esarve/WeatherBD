package com.sourav.weatherbd.Models.Structures;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weather")
public class SimplifiedWeatherModel {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String city;
    private double temp;
    private double humidity;
    private double pressure;
    private double max;
    private double min;
    private String datetime;

    public SimplifiedWeatherModel(String city, double temp, double humidity, double pressure, double max, double min, String datetime) {
        this.city = city;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.max = max;
        this.min = min;
        this.datetime = datetime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCity() {
        return city;
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

    public String getDatetime() {
        return datetime;
    }

    public int getId() {
        return id;
    }
}

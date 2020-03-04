package com.sourav.weatherbd.Models.Structures;

public class SimplifiedWeatherModel {
    private int id;
    private String city;
    private double temp;
    private double humidity;
    private double pressure;
    private double visibility;
    private double windSpeed;
    private String datetime;

    public SimplifiedWeatherModel(String city, double temp, double humidity, double pressure, double visibility, double windSpeed, String datetime) {
        this.city = city;
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
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

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
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

    public double getVisibility() {
        return visibility;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public String getDatetime() {
        return datetime;
    }

    public int getId() {
        return id;
    }
}

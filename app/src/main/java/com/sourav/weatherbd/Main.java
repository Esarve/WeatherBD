package com.sourav.weatherbd;

public class Main {
    double temp;
    double humidity;
    double pressure;

    public double getTemp() {
        return temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public Main(double temp, double humidity, double pressure) {
        this.temp = temp;
        this.humidity = humidity;
        this.pressure = pressure;
    }
}

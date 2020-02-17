package com.sourav.weatherbd.Models;

public class Weather {
    String main;
    String description;

    public Weather(String main, String description) {
        this.main = main;
        this.description = description;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }
}

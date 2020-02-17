package com.sourav.weatherbd.Models;

import java.util.List;

public class Data {
    String name;
    List<Weather> weather;
    Main main;

    public Data(String name, List<Weather> weather, Main main) {
        this.name = name;
        this.weather = weather;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Main getMain() {
        return main;
    }
}

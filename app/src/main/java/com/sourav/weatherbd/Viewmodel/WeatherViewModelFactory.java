package com.sourav.weatherbd.Viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

public class WeatherViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;

    public WeatherViewModelFactory(Application mApplication, String mParam) {
        this.mApplication = mApplication;
        this.mParam = mParam;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return Objects.requireNonNull(modelClass.cast(new WeatherViewModelFactory(mApplication, mParam)));
    }
}

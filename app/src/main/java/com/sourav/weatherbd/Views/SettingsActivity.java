package com.sourav.weatherbd.Views;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceFragmentCompat;

import com.sourav.weatherbd.Handlers.SettingsManager;
import com.sourav.weatherbd.Handlers.StatusNavBarColorHandler;
import com.sourav.weatherbd.R;

public class SettingsActivity extends AppCompatActivity {
    private StatusNavBarColorHandler statusNavBarColorHandler;
    private static final int DARK = 1;
    private static final int LIGHT = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        statusNavBarColorHandler = StatusNavBarColorHandler.getInstance();
        setThemeConfigs();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.pref, rootKey);
        }
    }

    private void setThemeConfigs() {
        boolean darkMode = new SettingsManager(this).getDarkMode();

        if (darkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            statusNavBarColorHandler.changeNavBarColor(getWindow().getDecorView(), this, R.color.navDark, false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            statusNavBarColorHandler.changeStatusBarColor(getWindow().getDecorView(), this, R.color.grey_3);
            statusNavBarColorHandler.changeNavBarColor(getWindow().getDecorView(), this, R.color.grey_3, true);
        }

    }
}
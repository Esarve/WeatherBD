package com.sourav.weatherbd.Handlers;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import androidx.core.content.ContextCompat;

public class StatusNavBarColorHandler {
    private static StatusNavBarColorHandler instance;

    public static StatusNavBarColorHandler getInstance(){
        if (instance == null)
            instance = new StatusNavBarColorHandler();
        return instance;
    }

    public void setLightStatusNavBar(View view, Activity activity, int color) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow()
                    .setStatusBarColor(ContextCompat.getColor(activity.getApplication()
                            .getApplicationContext(),
                            color));

            activity.getWindow()
                    .setNavigationBarColor(ContextCompat.getColor(activity.getApplication()
                            .getApplicationContext(),
                            color));
        }
    }
}

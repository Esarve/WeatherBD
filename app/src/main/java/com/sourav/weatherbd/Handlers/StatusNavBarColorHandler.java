package com.sourav.weatherbd.Handlers;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

public class StatusNavBarColorHandler {
    private static StatusNavBarColorHandler instance;

    public static StatusNavBarColorHandler getInstance(){
        if (instance == null)
            instance = new StatusNavBarColorHandler();
        return instance;
    }

    public void changeStatusBarColor(View view, Activity activity, int color) {
        int flags = view.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        }
        view.setSystemUiVisibility(flags);
        activity.getWindow()
                .setStatusBarColor(ContextCompat.getColor(activity.getApplication()
                                .getApplicationContext(),color));
    }
    public void changeNavBarColor(View view, Activity activity, int color,boolean isLightMode){
        int flags = view.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && isLightMode) {
            flags |= View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR;
        }
        view.setSystemUiVisibility(flags);
        activity.getWindow()
                .setNavigationBarColor(ContextCompat.getColor(activity.getApplication()
                                .getApplicationContext(),
                        color));
    }
}

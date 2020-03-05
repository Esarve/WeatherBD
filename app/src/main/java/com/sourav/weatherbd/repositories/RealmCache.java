package com.sourav.weatherbd.repositories;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourav.weatherbd.Models.Structures.WeatherObjectForJson;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmCache {
    private static final String TAG = "RealmCache";
    private Realm realm;
    private Gson gson;

    public void initReal() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("cache.realm")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(configuration);
        gson = new GsonBuilder().create();
    }

    public void cacheToRealm(WeatherObjectForJson weatherObjectForJson) {
        realm.executeTransactionAsync(realm -> {
            realm.createOrUpdateObjectFromJson(WeatherObjectForJson.class, gson.toJson(weatherObjectForJson, WeatherObjectForJson.class));
            Log.d(TAG, "cacheToRealm: Cache Successful");
        });
    }

    public WeatherObjectForJson getData() {
        Log.d(TAG, "getData: Loading from offline cache");
        return realm.where(WeatherObjectForJson.class).findAll().last(null);
    }


}

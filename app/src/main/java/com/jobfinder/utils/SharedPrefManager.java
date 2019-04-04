package com.jobfinder.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jobfinder.AppClass;

import org.json.JSONException;

public class SharedPrefManager {

    private static SharedPreferences preferences;

    static {
        preferences = AppClass.context.getSharedPreferences(Constants.TAG, Context.MODE_PRIVATE);
    }

    private static SharedPrefManager manager;

    public static SharedPrefManager getInstance() {
        if (manager == null) {
            manager = new SharedPrefManager();
        }
        return manager;
    }

    public void putBoolean(String field, boolean value) {
        preferences.edit().putBoolean(field, value).apply();
    }

    public boolean getBoolean(String field, boolean defaultValue) {
        return preferences.getBoolean(field, defaultValue);
    }


    public void putString(String field, String value) {
        preferences.edit().putString(field, value).apply();
    }

    public String getString(String field, String defaultValue) {
        return preferences.getString(field, defaultValue);
    }

    public void putObject(String key, Object object) throws JSONException {
        String json = null;
        json = Utils.GsonUtils.toJSON(object).toString();
        putString(key, json);
    }

    public <T> T getObject(String key, Class<T> classOfT) {
        try {
            String json = getString(key, "");
            return Utils.GsonUtils.fromJSON(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

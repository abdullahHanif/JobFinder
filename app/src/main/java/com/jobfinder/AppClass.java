package com.jobfinder;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

public class AppClass extends Application {
    private static AppClass context;
    private static Activity activity;
    private PlacesClient placesClient;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        registerLifeCycleActivity();
        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyAVNXOGb0acJBWNprT2wET1X76bBdTAWF8");
        // Create a new Places client instance.
        placesClient = Places.createClient(this);
    }

    public static AppClass getContext() {
        return context;
    }

    private void registerLifeCycleActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
              AppClass.activity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static Activity getActivity(){
        return activity;
    }
}
